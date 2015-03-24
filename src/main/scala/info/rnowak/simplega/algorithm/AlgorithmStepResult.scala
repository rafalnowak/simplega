package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.{FitnessValue, IndividualWithFitness}
import info.rnowak.simplega.population.Population

case class AlgorithmStepResult[PopulationType <: Population](generationNumber: Long,
                                                             population: PopulationType,
                                                             meanValue: FitnessValue,
                                                             bestIndividual: IndividualWithFitness[PopulationType#IndividualType]) {
  override def toString = s"$generationNumber : mean value = $meanValue, best = $bestIndividual"
}
