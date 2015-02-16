package info.rnowak.simplega.algorithm

import info.rnowak.simplega.population.{PermutationPopulation, PermutationPopulationContext}
import org.scalatest.{Matchers, FlatSpec}

class GeneticAlgorithmTest extends FlatSpec with Matchers {
  "GA" should "perform one iteration in run" in {
    val iterations = 1
    val context = PermutationPopulationContext(populationSize = 3, individualLength = 3)
    val ga = new GeneticAlgorithm()
    
    val result = ga.run(populationContext = context, iterations = iterations)
    
    result.totalIterations shouldEqual iterations
  }
}
