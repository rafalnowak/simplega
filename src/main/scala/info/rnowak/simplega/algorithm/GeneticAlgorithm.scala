package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.FitnessFunction
import info.rnowak.simplega.population.individual.Individual
import info.rnowak.simplega.population.{Population, PopulationContext}

class GeneticAlgorithm {
  def run[PopulationType <: Population](populationContext: PopulationContext[PopulationType],
                                        maxIterations: Int)
                                       (fitness: FitnessFunction[PopulationType]): GeneticAlgorithmResult = {
    val currentPopulation = populationContext.createInitialPopulation()
    val individualsSortedByFitness = currentPopulation.individuals map { individual =>
      IndividualWithFitness(individual, fitness.calculate(individual))
    } sortBy { _.fitness }
    GeneticAlgorithmResult(totalIterations = maxIterations)
  }
}

case class IndividualWithFitness[IndividualType <: Individual](individual: IndividualType, fitness: BigDecimal)
