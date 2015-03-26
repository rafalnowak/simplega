package info.rnowak.simplega.population

import org.scalatest.{FlatSpec, Matchers}

class BinaryPopulationTest extends FlatSpec with Matchers {
  "Binary population" should "generate initial population with given individuals count" in {
    val populationSize = 5

    val population = BinaryPopulation.initialPopulation(populationCount = populationSize, individualLength = 5)

    population.size shouldEqual populationSize
  }
}
