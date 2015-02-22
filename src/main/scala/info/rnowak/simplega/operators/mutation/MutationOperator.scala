package info.rnowak.simplega.operators.mutation

import info.rnowak.simplega.population.Population

trait MutationOperator[PopulationType <: Population] {
  def mutate(individual: PopulationType#IndividualType): PopulationType#IndividualType
}
