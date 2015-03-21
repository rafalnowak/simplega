package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.FitnessValue
import info.rnowak.simplega.population.Population

case class AlgorithmStepResult[PopulationType <: Population](currentGeneration: Long, population: PopulationType, bestFitness: FitnessValue) {
}

object AlgorithmStepResult {
  def zero[PopulationType <: Population](startPopulation: PopulationType, bestFitness: FitnessValue): AlgorithmStepResult[PopulationType] =
    AlgorithmStepResult(currentGeneration = 0, population = startPopulation, bestFitness = bestFitness)
}
