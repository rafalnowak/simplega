package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.{FitnessFunction, FitnessValue, IndividualWithFitness}
import info.rnowak.simplega.operators.crossover.Children
import info.rnowak.simplega.population.Population
import info.rnowak.simplega.population.context.PopulationContext

import scala.util.Random

class GeneticAlgorithm[PopulationType <: Population] {
  //TODO: dodać warunki na prawdopodobieństwo mutacji, krzyżowania itd
  def run(populationContext: PopulationContext[PopulationType],
          minimumFitness: FitnessValue,
          maxIterations: Long)
         (fitness: FitnessFunction[PopulationType]): Stream[AlgorithmStepResult[PopulationType]] = {
    val initialPopulation = populationContext.createInitialPopulation()
    populationStream(initialPopulation)(populationContext, minimumFitness, fitness, maxIterations) takeWhile { step =>
      //TODO: więcej mądrych warunków stopu
      step.currentGeneration < maxIterations
    }
  }
  
  //TODO: posprzątać ten bałagan
  private def populationStream(initialPopulation: PopulationType)
                              (populationContext: PopulationContext[PopulationType],
                               minimumFitness: FitnessValue,
                               fitness: FitnessFunction[PopulationType],
                               maxIterations: Long): Stream[AlgorithmStepResult[PopulationType]] = {
    lazy val populations: Stream[AlgorithmStepResult[PopulationType]] = Stream.cons(
      initialStep(initialPopulation, fitness),
      populations map { step =>
        val individualsWithFitness = fitness.calculateFor(initialPopulation)
        //TODO: parametr liczby wybieranych osobników
        val selectedIndividuals = selectIndividuals(populationContext, initialPopulation.size, individualsWithFitness)
        val newIndividuals = for {
          i <- 1 to initialPopulation.size
        } yield crossoverAndMutate(selectedIndividuals)(populationContext)
        val newPopulation = populationContext.createPopulationFromIndividuals(newIndividuals.flatten)
        AlgorithmStepResult[PopulationType](step.currentGeneration + 1, newPopulation, bestFitnessForPopulation(newPopulation, fitness))
      }
    )
    populations
  }
  
  private def initialStep(population: PopulationType, fitness: FitnessFunction[PopulationType]): AlgorithmStepResult[PopulationType] = 
    AlgorithmStepResult(0, population, bestFitnessForPopulation(population, fitness))
  
  private def bestFitnessForPopulation(population: PopulationType, fitness: FitnessFunction[PopulationType]): FitnessValue = 
    fitness.calculateFor(population).map(_.fitness).min
  
  private def selectIndividuals(populationContext: PopulationContext[PopulationType],
                                desiredSelectedCount: Int,
                                individuals: Seq[IndividualWithFitness[PopulationType#IndividualType]]): Seq[PopulationType#IndividualType] = 
    for {
      i <- 1 to desiredSelectedCount      
    } yield populationContext.selectionOperator.select(individuals)
  
  private def crossoverAndMutate(individuals: Seq[PopulationType#IndividualType])
                                (context: PopulationContext[PopulationType]): Children[PopulationType] = {
    val parentFirst = individuals(Random.nextInt(individuals.size))
    val parentSecond = individuals(Random.nextInt(individuals.size))
    val children = context.crossOverOperator.crossover(parentFirst, parentSecond)
    for { 
      child <- children
    } yield context.mutationOperator.mutate(child)
  }
}

