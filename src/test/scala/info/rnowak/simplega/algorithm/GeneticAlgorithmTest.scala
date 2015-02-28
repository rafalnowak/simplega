package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.{FitnessFunction, FitnessValue, IndividualWithFitness}
import info.rnowak.simplega.operators.crossover.CrossOverOperator
import info.rnowak.simplega.operators.mutation.MutationOperator
import info.rnowak.simplega.operators.selection.SelectionOperator
import info.rnowak.simplega.population.PermutationPopulation
import info.rnowak.simplega.population.context.PermutationPopulationContext
import info.rnowak.simplega.population.individual.PermutationIndividual
import org.scalatest.{FlatSpec, Matchers}

import scala.util.Random

class GeneticAlgorithmTest extends FlatSpec with Matchers {

  class RandomSelection extends SelectionOperator[PermutationPopulation] {
    override def selection(individualsSorted: Seq[IndividualWithFitness[PermutationIndividual]]): PermutationIndividual = {
      val random = Random.nextInt(individualsSorted.size)
      individualsSorted(random).individual      
    }
  }
  
  class SimpleCrossover extends CrossOverOperator[PermutationPopulation] {
    override def crossover(individualFirst: PermutationIndividual, individualSecond: PermutationIndividual): PermutationIndividual = {
      individualSecond      
    }
  }
  
  class SimpleMutation extends MutationOperator[PermutationPopulation] {
    override def mutate(individual: PermutationIndividual): PermutationIndividual = {
      individual      
    }
  }
  
  class ConstantFitness(constant: Int) extends FitnessFunction[PermutationPopulation] {
    override def calculateFor(population: PermutationPopulation): Seq[IndividualWithFitness[PermutationIndividual]] = {
      population.individuals.map { individual =>
        IndividualWithFitness(individual, FitnessValue(5))
      }
    }
  }

  "GA" should "perform one iteration in run" in {
    val iterations = 1
    val context = PermutationPopulationContext(populationSize = 3, 
      individualLength = 3,
      selectionOperator = new RandomSelection(),
      crossOverOperator = new SimpleCrossover(),
      mutationOperator = new SimpleMutation())
    val ga = new GeneticAlgorithm[PermutationPopulation]()
    
    val result = ga.run(populationContext = context, maxIterations = iterations, minimumFitness = FitnessValue(4))(fitness = new ConstantFitness(7))

  }
  
  "Fitness function" should "return individuals with their rate" in {
    val population = PermutationPopulation.initialPopulation(populationCount = 3, individualLength = 3)
    val fitnessFunction = new ConstantFitness(7)
    
    val ratedIndividuals = fitnessFunction.calculateFor(population)
    
    ratedIndividuals should have size population.size
    ratedIndividuals.map(_.fitness.value) should equal(Seq(5, 5, 5))
  }
}
