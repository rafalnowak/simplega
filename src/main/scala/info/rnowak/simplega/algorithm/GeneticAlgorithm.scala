package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.FitnessFunction
import info.rnowak.simplega.operators.crossover.CrossOverOperator
import info.rnowak.simplega.operators.mutation.MutationOperator
import info.rnowak.simplega.operators.selection.SelectionOperator
import info.rnowak.simplega.population.Population
import info.rnowak.simplega.population.context.PopulationContext

class GeneticAlgorithm {
  def run[PopulationType <: Population](populationContext: PopulationContext[PopulationType],
                                        selectionOperator: SelectionOperator[PopulationType],
                                        crossOverOperator: CrossOverOperator[PopulationType],
                                        mutationOperator: MutationOperator[PopulationType],
                                        maxIterations: Int)
                                       (fitness: FitnessFunction[PopulationType]): GeneticAlgorithmResult = {
    val currentPopulation = populationContext.createInitialPopulation()
    val individualsSortedByFitness = fitness.calculate(currentPopulation).sortBy(_.fitness)
    val parentFirst = selectionOperator.selection(individualsSortedByFitness)
    val parentSecond = selectionOperator.selection(individualsSortedByFitness)
    GeneticAlgorithmResult(totalIterations = maxIterations)
  }
}

