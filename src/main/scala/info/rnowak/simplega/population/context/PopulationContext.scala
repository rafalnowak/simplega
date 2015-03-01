package info.rnowak.simplega.population.context

import info.rnowak.simplega.operators.crossover.CrossoverOperator
import info.rnowak.simplega.operators.mutation.MutationOperator
import info.rnowak.simplega.operators.selection.SelectionOperator
import info.rnowak.simplega.population.Population

trait PopulationContext[PopulationType <: Population] {
  def populationSize: Int
  def individualLength: Int

  def createPopulationFromIndividuals(individuals: Seq[PopulationType#IndividualType]): PopulationType
  def createInitialPopulation(): PopulationType

  def selectionOperator: SelectionOperator[PopulationType]
  def crossOverOperator: CrossoverOperator[PopulationType]
  def mutationOperator: MutationOperator[PopulationType]
}
