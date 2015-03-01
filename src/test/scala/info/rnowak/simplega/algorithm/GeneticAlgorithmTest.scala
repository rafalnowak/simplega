package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.{FitnessFunction, FitnessValue, IndividualWithFitness}
import info.rnowak.simplega.operators.crossover.binary.OnePointCrossover
import info.rnowak.simplega.operators.crossover.{Children, CrossoverOperator}
import info.rnowak.simplega.operators.mutation.binary.SimpleInversionMutation
import info.rnowak.simplega.operators.selection.SelectionOperator
import info.rnowak.simplega.population.BinaryPopulation
import info.rnowak.simplega.population.context.BinaryPopulationContext
import info.rnowak.simplega.population.individual.{One, BinaryIndividual}
import org.scalatest.{FlatSpec, Matchers}

import scala.util.Random

class GeneticAlgorithmTest extends FlatSpec with Matchers {

  class RandomSelection extends SelectionOperator[BinaryPopulation] {
    override def selection(individualsSorted: Seq[IndividualWithFitness[BinaryIndividual]]): BinaryIndividual = {
      val random = Random.nextInt(individualsSorted.size)
      individualsSorted(random).individual      
    }
  }
  
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
      selectionOperator = new RandomSelection(),
      crossOverOperator = new OnePointCrossover(),
      mutationOperator = new SimpleInversionMutation())
    val ga = new GeneticAlgorithm[BinaryPopulation]()
    
    val result = ga.run(populationContext = context, maxIterations = iterations, minimumFitness = FitnessValue(4))(fitness = new QuadraticFunctionFitness())
  }
}
