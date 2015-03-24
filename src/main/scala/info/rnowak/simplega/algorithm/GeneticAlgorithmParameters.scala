package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.FitnessValue

case class GeneticAlgorithmParameters(desiredFitness: FitnessValue,
                                      maxGenerations: Long,
                                      crossoverProbability: BigDecimal,
                                      mutationProbability: BigDecimal)
