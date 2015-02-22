package info.rnowak.simplega.operators.crossover

import info.rnowak.simplega.population.individual.Individual

trait CrossOverOperator[IndividualType <: Individual] {
  def crossover(individualFirst: IndividualType, individualSecond: IndividualType): IndividualType
}
