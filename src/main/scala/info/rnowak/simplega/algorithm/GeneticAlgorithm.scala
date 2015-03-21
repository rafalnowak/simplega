package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.{FitnessFunction, FitnessValue, IndividualWithFitness}
import info.rnowak.simplega.operators.crossover.Children
import info.rnowak.simplega.population.Population
import info.rnowak.simplega.population.context.PopulationContext

import scala.util.Random

class GeneticAlgorithm[PopulationType <: Population] {
  private val randomGenerator = new Random()

  def run(populationContext: PopulationContext[PopulationType])
         (parameters: GeneticAlgorithmParameters)
         (fitness: FitnessFunction[PopulationType]): Stream[AlgorithmStepResult[PopulationType]] = {
    val initialPopulation = populationContext.createInitialPopulation()
    populationStream(initialPopulation)(parameters, populationContext)(fitness) takeWhile { step =>
      //TODO: więcej mądrych warunków stopu
      step.currentGeneration < parameters.maxGenerations
    }
  }
  
  private def populationStream(initialPopulation: PopulationType)
                              (parameters: GeneticAlgorithmParameters,
                               populationContext: PopulationContext[PopulationType])
                              (fitness: FitnessFunction[PopulationType]): Stream[AlgorithmStepResult[PopulationType]] = {
    lazy val populations: Stream[AlgorithmStepResult[PopulationType]] = Stream.cons(
      AlgorithmStepResult.zero(initialPopulation, bestFitnessForPopulation(initialPopulation, fitness)),
      populations map { step =>
        val individualsWithFitness = fitness.calculateFor(step.population)
        //TODO: parametr liczby wybieranych osobników
        val newIndividuals = for {
          i <- 1 to step.population.size
        } yield {
          val selectedIndividuals = selectIndividuals(populationContext, 2, individualsWithFitness)
          crossoverAndMutate(selectedIndividuals)(parameters, populationContext)
        }
        val newPopulation = populationContext.createPopulationFromIndividuals(newIndividuals.flatten)
        AlgorithmStepResult[PopulationType](step.currentGeneration + 1, newPopulation, bestFitnessForPopulation(newPopulation, fitness))
      }
    )
    populations
  }

  private def bestFitnessForPopulation(population: PopulationType, fitness: FitnessFunction[PopulationType]): FitnessValue =
    fitness.calculateFor(population).map(_.fitness).min

  private def selectIndividuals(populationContext: PopulationContext[PopulationType],
                                desiredSelectedCount: Int,
                                individuals: Seq[IndividualWithFitness[PopulationType#IndividualType]]): Seq[PopulationType#IndividualType] = 
    for {
      i <- 1 to desiredSelectedCount      
    } yield populationContext.selectionOperator.select(individuals)

  //TODO: dodać prawdopodobieństwa krzyżowania
  private def crossoverAndMutate(individuals: Seq[PopulationType#IndividualType])
                                (parameters: GeneticAlgorithmParameters,
                                 context: PopulationContext[PopulationType]): Children[PopulationType] = {
    val parentFirst = individuals(Random.nextInt(individuals.size))
    val parentSecond = individuals(Random.nextInt(individuals.size))
    val children = context.crossOverOperator.crossover(parentFirst, parentSecond)
    for { 
      child <- children
    } yield if(randomGenerator.nextDouble() > parameters.mutationProbability) {
      context.mutationOperator.mutate(child)
    } else {
      child
    }
  }
}

