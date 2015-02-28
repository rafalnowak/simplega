package info.rnowak.simplega.population

import org.scalatest.{FlatSpec, Matchers}

class PermutationPopulationTest extends FlatSpec with Matchers {
  "Permutation population" should "generate initial population with given individuals count" in {
    val populationSize = 5
    
    val population = PermutationPopulation.initialPopulation(populationCount = populationSize, individualLength = 5)
    
    population.size shouldEqual populationSize
  }
  
  "Permutation population" should "contain individuals with permutation within given range" in {
    val populationSize = 1
    val individualLength = 5
    
    val population = PermutationPopulation.initialPopulation(populationSize, individualLength)

    population.size shouldEqual 1
    val permutation = population.individuals.head.permutation
    permutation.sorted should contain theSameElementsInOrderAs (1 to individualLength)
  }
}
