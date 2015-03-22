package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.{FitnessFunction, FitnessValue, IndividualWithFitness}
import info.rnowak.simplega.operators.crossover.binary.OnePointCrossover
import info.rnowak.simplega.operators.mutation.binary.SimpleInversionMutation
import info.rnowak.simplega.operators.selection.binary.SimpleTournamentSelection
import info.rnowak.simplega.population.BinaryPopulation
import info.rnowak.simplega.population.context.BinaryPopulationContext
import info.rnowak.simplega.population.individual.{BinaryIndividual, One}
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.prop.TableDrivenPropertyChecks._

class GeneticAlgorithmTest extends FlatSpec with Matchers {
  
  class QuadraticFunctionFitness() extends FitnessFunction[BinaryPopulation] {
    override def calculateFor(population: BinaryPopulation): Seq[IndividualWithFitness[BinaryIndividual]] = {
      population.individuals.map { individual =>
        val (_, indexes) = (individual.bits zip (0 to individual.length - 1).reverse filter { case (bit, _) => bit == One }).unzip
        val bitsValue = indexes.foldLeft(0) { (acc, index) => acc + Math.pow(2, index).toInt }
        val x = -1.0 + bitsValue * (3.0 / (Math.pow(2, individual.length) - 1))
        val fValue = x * Math.sin(10*Math.PI * x) + 1.0
        IndividualWithFitness(individual, FitnessValue(fValue))
      }
    }
  }

  //"Every population in next generations" should "have the same size" in {
  ignore should "bla" in {
    val generations = 3
    val populationsSize = Table("size", 3, 6, 10, 13)

    forAll(populationsSize) { populationSize =>
      val parameters = new GeneticAlgorithmParameters(FitnessValue(4),
        generations,
        crossoverProbability = BigDecimal(0.1),
        mutationProbability = BigDecimal(0.3))
      val context = BinaryPopulationContext(populationSize = populationSize,
        individualLength = 5,
        selectionOperator = new SimpleTournamentSelection(),
        crossOverOperator = new OnePointCrossover(),
        mutationOperator = new SimpleInversionMutation())
      val ga = new GeneticAlgorithm[BinaryPopulation]()

      val result = ga.run(context)(parameters)(new QuadraticFunctionFitness())

      result foreach { algorithmStep =>
        algorithmStep.population.size shouldEqual populationSize
      }
    }
  }

  "GA" should "find optimal solution" in {
    val parameters = new GeneticAlgorithmParameters(FitnessValue(4),
      250,
      crossoverProbability = BigDecimal(0.25),
      mutationProbability = BigDecimal(0.1))
    val context = BinaryPopulationContext(populationSize = 50,
      individualLength = 22,
      selectionOperator = new SimpleTournamentSelection(),
      crossOverOperator = new OnePointCrossover(),
      mutationOperator = new SimpleInversionMutation())
    val ga = new GeneticAlgorithm[BinaryPopulation]()

    val result = ga.run(context)(parameters)(new QuadraticFunctionFitness())

    val resultCalculated = result.toList
    val bestValue = resultCalculated.last.bestIndividual.fitness.value
    val expectedBestValue = 2.85
    (bestValue - expectedBestValue).abs.toDouble should be < 0.001
  }
}
