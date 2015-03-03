package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.{FitnessFunction, FitnessValue, IndividualWithFitness}
import info.rnowak.simplega.operators.crossover.binary.OnePointCrossover
import info.rnowak.simplega.operators.mutation.binary.SimpleInversionMutation
import info.rnowak.simplega.operators.selection.binary.SimpleFitnessSelection
import info.rnowak.simplega.population.BinaryPopulation
import info.rnowak.simplega.population.context.BinaryPopulationContext
import info.rnowak.simplega.population.individual.{BinaryIndividual, One}
import org.scalatest.{FlatSpec, Matchers}

class GeneticAlgorithmTest extends FlatSpec with Matchers {
  
  class QuadraticFunctionFitness() extends FitnessFunction[BinaryPopulation] {
    override def calculateFor(population: BinaryPopulation): Seq[IndividualWithFitness[BinaryIndividual]] = {
      population.individuals.map { individual =>
        val (_, indexes) = (individual.bits zip (0 to individual.length - 1).reverse filter { case (bit, _) => bit == One }).unzip
        val value = indexes.foldLeft(0) { (acc, index) => acc + Math.pow(2, index).toInt }
        IndividualWithFitness(individual, FitnessValue(value*value))
      }
    }
  }

  "GA" should "perform one iteration in run" in {
    val iterations = 10
    val context = BinaryPopulationContext(populationSize = 4,
      individualLength = 3,
      selectionOperator = new SimpleFitnessSelection(),
      crossOverOperator = new OnePointCrossover(),
      mutationOperator = new SimpleInversionMutation())
    val ga = new GeneticAlgorithm[BinaryPopulation]()
    
    val result = ga.run(populationContext = context, maxIterations = iterations, minimumFitness = FitnessValue(4))(fitness = new QuadraticFunctionFitness())
    val test = result.toList
    ()
  }
}
