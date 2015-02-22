package info.rnowak.simplega.fitness

import info.rnowak.simplega.population.individual.Individual

case class IndividualWithFitness[IndividualType <: Individual](individual: IndividualType, fitness: BigDecimal) {
  override def toString: String = s"Individual: $individual, fitness: $fitness"
}
