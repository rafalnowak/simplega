package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.{FitnessValue, IndividualWithFitness}
import info.rnowak.simplega.population.Population

case class AlgorithmStepResult[PopulationType <: Population](generationNumber: Long,
                                                             population: PopulationType,
                                                             meanValue: FitnessValue,
                                                             bestIndividual: IndividualWithFitness[PopulationType#IndividualType],
                                                             generationWithoutImprovement: Long) {
  override def toString = s"$generationNumber : mean value = $meanValue, " +
    s"best = $bestIndividual, generation # without improvement = $generationWithoutImprovement"
}

object AlgorithmStepResult {
  def zero[PopulationType <: Population](population: PopulationType,
                                         meanValue: FitnessValue,
                                         bestIndividual: IndividualWithFitness[PopulationType#IndividualType]): AlgorithmStepResult[PopulationType] =
    AlgorithmStepResult[PopulationType](0, population, meanValue, bestIndividual, 0)
}