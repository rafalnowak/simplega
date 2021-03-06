package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.{FitnessFunction, FitnessValue, IndividualWithFitness}
import info.rnowak.simplega.operators.crossover.binary.OnePointCrossover
import info.rnowak.simplega.operators.mutation.binary.SimpleInversionMutation
import info.rnowak.simplega.operators.selection.SimpleTournamentSelection
import info.rnowak.simplega.population.BinaryPopulation
import info.rnowak.simplega.population.context.BinaryPopulationContext
import info.rnowak.simplega.population.individual.{BinaryIndividual, One}
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{FlatSpec, Matchers}
import info.rnowak.simplega.fitness.FitnessValue._

class GeneticAlgorithmTest extends FlatSpec with Matchers {

  class SomeFunctionFitness() extends FitnessFunction[BinaryPopulation] {

    override def calculate(individual: BinaryIndividual): IndividualWithFitness[BinaryIndividual] = {
      val (_, indexes) = (individual.bits zip (0 to individual.length - 1).reverse filter { case (bit, _) => bit == One }).unzip
      val bitsValue = indexes.foldLeft(0) { (acc, index) => acc + Math.pow(2, index).toInt }
      val range = 3.0
      val x = -1.0 + bitsValue * (range / (Math.pow(2, individual.length) - 1))
      val fValue = x * Math.sin(10*Math.PI * x) + 1.0
      IndividualWithFitness(individual, fValue.fitness)
    }
  }

  "Every population in next generations" should "have the same size" in {
    val generations = 3
    val populationsSize = Table("size", 3, 6, 10, 13)

    forAll(populationsSize) { populationSize =>
      val parameters = new GeneticAlgorithmParameters(desiredFitness = FitnessValue(4),
        maxGenerations = generations,
        generationsWithoutImprovement = 10,
        crossoverProbability = BigDecimal(0.1),
        mutationProbability = BigDecimal(0.3))
      val context = buildDefaultContext(populationSize, 5)
      val ga = new GeneticAlgorithm[BinaryPopulation](parameters, context, new SomeFunctionFitness())

      val result = ga.run()

      result foreach { algorithmStep =>
        algorithmStep.population.size shouldEqual populationSize
      }
    }
  }

  "GA" should "find optimal solution" in {
    val parameters = new GeneticAlgorithmParameters(desiredFitness = FitnessValue(4),
      maxGenerations = 250,
      generationsWithoutImprovement = 5,
      crossoverProbability = BigDecimal(0.01),
      mutationProbability = BigDecimal(0.1))
    val context = buildDefaultContext(50, 22)
    val ga = new GeneticAlgorithm[BinaryPopulation](parameters, context, new SomeFunctionFitness())

    val result = ga.run()

    val resultCalculated = result.toList

    resultCalculated.foreach { step =>
      println(step)
    }
    
    val bestValue = resultCalculated.last.bestIndividual.fitness.value
    val expectedBestValue = 2.85
    (bestValue - expectedBestValue).abs.toDouble should be < 0.001
  }

  private def buildDefaultContext(populationSize: Int, individualLength: Int): BinaryPopulationContext =
    BinaryPopulationContext(populationSize = populationSize,
      individualLength = individualLength,
      selectionOperator = new SimpleTournamentSelection(),
      crossOverOperator = new OnePointCrossover(),
      mutationOperator = new SimpleInversionMutation())
}
