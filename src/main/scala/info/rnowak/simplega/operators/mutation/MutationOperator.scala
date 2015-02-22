package info.rnowak.simplega.operators.mutation

import info.rnowak.simplega.population.individual.Individual

trait MutationOperator[IndividualType <: Individual] {
  def mutate(individual: IndividualType): IndividualType
}
