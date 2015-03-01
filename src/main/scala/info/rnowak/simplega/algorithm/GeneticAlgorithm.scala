package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.{FitnessFunction, FitnessValue, IndividualWithFitness}
import info.rnowak.simplega.operators.crossover.Children
import info.rnowak.simplega.population.Population
import info.rnowak.simplega.population.context.PopulationContext

class GeneticAlgorithm[PopulationType <: Population] {
  //TODO: dodać warunki na prawdopodobieństwo mutacji, krzyżowania itd
  def run(populationContext: PopulationContext[PopulationType],
          minimumFitness: FitnessValue,
          maxIterations: Long)
         (fitness: FitnessFunction[PopulationType]): Stream[AlgorithmStepResult[PopulationType]] = {
    val currentPopulation = populationContext.createInitialPopulation()
    val algorithmSteps = populationStream(currentPopulation)(populationContext, minimumFitness, fitness, maxIterations)
    algorithmSteps.takeWhile { step =>
      //TODO: więcej mądrych warunków stopu
      step.currentGeneration < maxIterations
    }
  }
  
  //TODO: posprzątać ten bałagan
  private def populationStream(currentPopulation: PopulationType)
                      (populationContext: PopulationContext[PopulationType],
                       minimumFitness: FitnessValue,
                       fitness: FitnessFunction[PopulationType],
                       maxIterations: Long): Stream[AlgorithmStepResult[PopulationType]] = {
    lazy val populations: Stream[AlgorithmStepResult[PopulationType]] = Stream.cons(
      initialStep(currentPopulation, fitness), 
      populations map { step =>
        val individualsSortedByFitness = fitness.calculateFor(currentPopulation).sortBy(_.fitness.value)
        val newIndividuals = for {
          i <- 1 to currentPopulation.size
        } yield crossoverAndMutate(individualsSortedByFitness)(populationContext)
        val newPopulation = populationContext.createPopulationFromIndividuals(newIndividuals.flatten)
        AlgorithmStepResult[PopulationType](step.currentGeneration + 1, newPopulation, bestFitnessForPopulation(newPopulation, fitness))
      }
    )
    populations
  }
  
  private def initialStep(population: PopulationType, fitness: FitnessFunction[PopulationType]): AlgorithmStepResult[PopulationType] = 
    AlgorithmStepResult(0, population, bestFitnessForPopulation(population, fitness))
  
  private def bestFitnessForPopulation(population: PopulationType, fitness: FitnessFunction[PopulationType]): FitnessValue = {
    val individualsWithFitness = fitness.calculateFor(population)
    individualsWithFitness.map(_.fitness).min
  }
  
  private def crossoverAndMutate(individualsSorted: Seq[IndividualWithFitness[PopulationType#IndividualType]])
                                (context: PopulationContext[PopulationType]): Children[PopulationType] = {
    val parentFirst = context.selectionOperator.selection(individualsSorted)
    val parentSecond = context.selectionOperator.selection(individualsSorted)
    val children = context.crossOverOperator.crossover(parentFirst, parentSecond)
    for { 
      child <- children
    } yield context.mutationOperator.mutate(child)
  }
}

