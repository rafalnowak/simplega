package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.IndividualWithFitness
import info.rnowak.simplega.population.Population

case class AlgorithmStepResult[PopulationType <: Population](generationNumber: Long,
                                                             population: PopulationType,
                                                             bestIndividual: IndividualWithFitness[PopulationType#IndividualType]) {
  override def toString = s"$generationNumber : best = $bestIndividual"
}
