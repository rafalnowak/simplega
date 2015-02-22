package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.{IndividualWithFitness, FitnessFunction}
import info.rnowak.simplega.operators.crossover.CrossOverOperator
import info.rnowak.simplega.operators.mutation.MutationOperator
import info.rnowak.simplega.operators.selection.SelectionOperator
import info.rnowak.simplega.population.Population
import info.rnowak.simplega.population.context.PopulationContext

class GeneticAlgorithm[PopulationType <: Population] {
  def run(populationContext: PopulationContext[PopulationType],
          selectionOperator: SelectionOperator[PopulationType],
          crossOverOperator: CrossOverOperator[PopulationType],
          mutationOperator: MutationOperator[PopulationType],
          maxIterations: Int)
         (fitness: FitnessFunction[PopulationType]): GeneticAlgorithmResult = {
    val currentPopulation = populationContext.createInitialPopulation()
    val individualsSortedByFitness = fitness.calculate(currentPopulation).sortBy(_.fitness)
    val newIndividuals = for {
      i <- 1 to currentPopulation.size      
    } yield crossOverAndMutate(individualsSortedByFitness)(selectionOperator, crossOverOperator, mutationOperator)
    val newPopulation = populationContext.createPopulationFromIndividuals(newIndividuals)
    GeneticAlgorithmResult(totalIterations = maxIterations)
  }
  
  private def crossOverAndMutate(individualsSorted: Seq[IndividualWithFitness[PopulationType#IndividualType]])
                                (selectionOperator: SelectionOperator[PopulationType],
                                 crossOverOperator: CrossOverOperator[PopulationType],
                                 mutationOperator: MutationOperator[PopulationType]): PopulationType#IndividualType = {
    val parentFirst = selectionOperator.selection(individualsSorted)
    val parentSecond = selectionOperator.selection(individualsSorted)
    val newChild = crossOverOperator.crossover(parentFirst, parentSecond)
    val mutated = mutationOperator.mutate(newChild)
    mutated
  }
}

