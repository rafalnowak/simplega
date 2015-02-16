package info.rnowak.simplega.algorithm

import info.rnowak.simplega.population.{Population, PopulationContext}

class GeneticAlgorithm {
  def run[PopulationType <: Population[_]](populationContext: PopulationContext[PopulationType],
                                          iterations: Int): GeneticAlgorithmResult = {
    val initialPopulation = populationContext.createInitialPopulation()
    GeneticAlgorithmResult(totalIterations = iterations)
  }  
}
