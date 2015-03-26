package info.rnowak.simplega.fitness

import info.rnowak.simplega.population.Population

trait FitnessFunction[PopulationType <: Population] {
  type IndividualType = PopulationType#IndividualType

  def calculate(individual: IndividualType): IndividualWithFitness[IndividualType]
}
