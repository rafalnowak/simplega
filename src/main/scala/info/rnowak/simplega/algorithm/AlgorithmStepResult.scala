package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.FitnessValue
import info.rnowak.simplega.population.Population

case class AlgorithmStepResult[PopulationType <: Population](currentGeneration: Long, population: Population, bestFitness: FitnessValue)
