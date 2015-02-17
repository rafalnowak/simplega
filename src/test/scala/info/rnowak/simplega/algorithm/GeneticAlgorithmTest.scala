package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.FitnessFunction
import info.rnowak.simplega.population.individual.PermutationIndividual
import info.rnowak.simplega.population.{PermutationPopulation, PermutationPopulationContext}
import org.scalatest.{FlatSpec, Matchers}

class GeneticAlgorithmTest extends FlatSpec with Matchers {

  class ConstantFitness(constant: Int) extends FitnessFunction[PermutationPopulation] {
    override def calculate(individual: PermutationIndividual): BigDecimal = constant
  }

  "GA" should "perform one iteration in run" in {
    val iterations = 1
    val context = PermutationPopulationContext(populationSize = 3, individualLength = 3)
    val ga = new GeneticAlgorithm()
    
    val result = ga.run(populationContext = context, maxIterations = iterations)(fitness = new ConstantFitness(7))

    result.totalIterations shouldEqual iterations
  }
}
