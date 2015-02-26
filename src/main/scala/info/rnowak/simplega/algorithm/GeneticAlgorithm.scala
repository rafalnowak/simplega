package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.{FitnessValue, FitnessFunction, IndividualWithFitness}
import info.rnowak.simplega.population.Population
import info.rnowak.simplega.population.context.PopulationContext

class GeneticAlgorithm[PopulationType <: Population] {
  //TODO: dodać warunki na prawdopodobieństwo mutacji, krzyżowania itd
  def run(populationContext: PopulationContext[PopulationType],
          minimumFitness: FitnessValue,
          maxIterations: Int)
         (fitness: FitnessFunction[PopulationType]): GeneticAlgorithmResult = {
    val currentPopulation = populationContext.createInitialPopulation()
    //TODO: tutaj umieścić stream
    GeneticAlgorithmResult(totalIterations = maxIterations)
  }
  
  def populationStream(currentPopulation: PopulationType)
                      (populationContext: PopulationContext[PopulationType],
                       minimumFitness: FitnessValue,
                       fitness: FitnessFunction[PopulationType]): Stream[PopulationType] = {
    lazy val populations: Stream[PopulationType] = Stream.cons(currentPopulation, populations map { population =>
      val individualsSortedByFitness = fitness.calculateFor(currentPopulation).sortBy(_.fitness.value)
      val newIndividuals = for {
        i <- 1 to currentPopulation.size
      } yield crossOverAndMutate(individualsSortedByFitness)(populationContext)
      val newPopulation = populationContext.createPopulationFromIndividuals(newIndividuals)
      newPopulation
    })
    val x = populations.takeWhile { population =>
      val individualsWithFitness = fitness.calculateFor(population)
      val bestFitness = individualsWithFitness.map(_.fitness.value).min
      bestFitness >= minimumFitness.value
    }
    x
  }
  
  private def crossOverAndMutate(individualsSorted: Seq[IndividualWithFitness[PopulationType#IndividualType]])
                                (context: PopulationContext[PopulationType]): PopulationType#IndividualType = {
    val parentFirst = context.selectionOperator.selection(individualsSorted)
    val parentSecond = context.selectionOperator.selection(individualsSorted)
    val newChild = context.crossOverOperator.crossover(parentFirst, parentSecond)
    val mutated = context.mutationOperator.mutate(newChild)
    mutated
  }
}

