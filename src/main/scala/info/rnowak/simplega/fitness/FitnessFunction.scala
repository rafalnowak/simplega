package info.rnowak.simplega.fitness

import info.rnowak.simplega.population.Population

trait FitnessFunction[PopulationType <: Population] {
  def calculateFor(population: PopulationType): Seq[IndividualWithFitness[PopulationType#IndividualType]]
}
