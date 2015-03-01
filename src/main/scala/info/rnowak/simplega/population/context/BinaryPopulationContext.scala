package info.rnowak.simplega.population.context

import info.rnowak.simplega.operators.crossover.CrossoverOperator
import info.rnowak.simplega.operators.mutation.MutationOperator
import info.rnowak.simplega.operators.selection.SelectionOperator
import info.rnowak.simplega.population.BinaryPopulation
import info.rnowak.simplega.population.individual.BinaryIndividual

case class BinaryPopulationContext(populationSize: Int,
                                   individualLength: Int,
                                   selectionOperator: SelectionOperator[BinaryPopulation],
                                   crossOverOperator: CrossoverOperator[BinaryPopulation],
                                   mutationOperator: MutationOperator[BinaryPopulation]) extends PopulationContext[BinaryPopulation] {
  override def createPopulationFromIndividuals(individuals: Seq[BinaryIndividual]): BinaryPopulation =
    BinaryPopulation(individuals)

  override def createInitialPopulation(): BinaryPopulation =
    BinaryPopulation.initialPopulation(populationCount = populationSize, individualLength = individualLength)
}
