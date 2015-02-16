package info.rnowak.simplega.population

import org.scalatest.{Matchers, FlatSpec}

class PermutationPopulationTest extends FlatSpec with Matchers {
  it should "generate initial population with given individuals count" in {
    val populationSize = 5
    
    val population = PermutationPopulation.initialPopulation(populationCount = populationSize, individualLength = 5)
    
    population.size shouldEqual populationSize
  }
}
