package info.rnowak.simplega.operators.selection

import info.rnowak.simplega.fitness.IndividualWithFitness
import info.rnowak.simplega.population.Population

trait SelectionOperator[PopulationType <: Population] {
  def selection(individualsSorted: List[IndividualWithFitness[PopulationType#IndividualType]]): PopulationType#IndividualType
}
