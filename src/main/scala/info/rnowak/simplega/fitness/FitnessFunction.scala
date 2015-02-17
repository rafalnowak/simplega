package info.rnowak.simplega.fitness

import info.rnowak.simplega.population.Population

trait FitnessFunction[PopulationType <: Population] {
  def calculate(individual: PopulationType#IndividualType): BigDecimal
}
