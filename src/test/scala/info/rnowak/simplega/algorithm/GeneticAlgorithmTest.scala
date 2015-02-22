package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.{IndividualWithFitness, FitnessFunction}
import info.rnowak.simplega.operators.crossover.CrossOverOperator
import info.rnowak.simplega.operators.mutation.MutationOperator
import info.rnowak.simplega.operators.selection.SelectionOperator
import info.rnowak.simplega.population.context.PermutationPopulationContext
import info.rnowak.simplega.population.individual.PermutationIndividual
import info.rnowak.simplega.population.PermutationPopulation
import org.scalatest.{FlatSpec, Matchers}

import scala.util.Random

class GeneticAlgorithmTest extends FlatSpec with Matchers {

  class RandomSelection extends SelectionOperator[PermutationPopulation] {
    override def selection(individualsSorted: List[IndividualWithFitness[PermutationIndividual]]): PermutationIndividual = {
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
    override def calculate(population: PermutationPopulation): List[IndividualWithFitness[PermutationIndividual]] = {
      population.individuals.map { individual =>
        IndividualWithFitness(individual, 5)
      }
    }
  }

  "GA" should "perform one iteration in run" in {
    val iterations = 1
    val context = PermutationPopulationContext(populationSize = 3, individualLength = 3)
    val ga = new GeneticAlgorithm()
    
    val result = ga.run(populationContext = context,
      selectionOperator = new RandomSelection(),
      crossOverOperator = new SimpleCrossover(),
      mutationOperator = new SimpleMutation(),
      maxIterations = iterations)(fitness = new ConstantFitness(7))

    result.totalIterations shouldEqual iterations
  }
  
  "Fitness function" should "return individuals with their rate" in {
    val population = PermutationPopulation.initialPopulation(populationCount = 3, individualLength = 3)
    val fitnessFunction = new ConstantFitness(7)
    
    val ratedIndividuals = fitnessFunction.calculate(population)
    
    ratedIndividuals should have size population.size
    ratedIndividuals.map(_.fitness) should equal(List(5, 5, 5))
  }
}
