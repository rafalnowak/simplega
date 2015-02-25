package info.rnowak.simplega.population.context

import info.rnowak.simplega.operators.crossover.CrossOverOperator
import info.rnowak.simplega.operators.mutation.MutationOperator
import info.rnowak.simplega.operators.selection.SelectionOperator
import info.rnowak.simplega.population.PermutationPopulation
import info.rnowak.simplega.population.individual.PermutationIndividual

case class PermutationPopulationContext(populationSize: Int, 
                                        individualLength: Int,
                                        selectionOperator: SelectionOperator[PermutationPopulation],
                                        crossOverOperator: CrossOverOperator[PermutationPopulation],
                                        mutationOperator: MutationOperator[PermutationPopulation]) extends PopulationContext[PermutationPopulation] {
  override def createPopulationFromIndividuals(individuals: Seq[PermutationIndividual]): PermutationPopulation =
    PermutationPopulation(individuals)

  override def createInitialPopulation(): PermutationPopulation =
    PermutationPopulation.initialPopulation(populationCount = populationSize, individualLength = individualLength)
}
