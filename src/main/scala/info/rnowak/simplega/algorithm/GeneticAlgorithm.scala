package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.{FitnessFunction, IndividualWithFitness}
import info.rnowak.simplega.operators.crossover.Children
import info.rnowak.simplega.population.Population
import info.rnowak.simplega.population.context.PopulationContext

import scala.annotation.tailrec
import scala.util.Random

class GeneticAlgorithm[PopulationType <: Population] {
  type IndividualType = PopulationType#IndividualType

  private val randomGenerator = new Random()

  def run(populationContext: PopulationContext[PopulationType])
         (parameters: GeneticAlgorithmParameters)
         (fitness: FitnessFunction[PopulationType]): Stream[AlgorithmStepResult[PopulationType]] = {
    val initialPopulation = populationContext.createInitialPopulation()
    populationStream(initialPopulation)(parameters, populationContext)(fitness) takeWhile { step =>
      //TODO: więcej mądrych warunków stopu
      step.generationNumber < parameters.maxGenerations
    }
  }
  
  private def populationStream(initialPopulation: PopulationType)
                              (parameters: GeneticAlgorithmParameters,
                               populationContext: PopulationContext[PopulationType])
                              (fitness: FitnessFunction[PopulationType]): Stream[AlgorithmStepResult[PopulationType]] = {
    lazy val populations: Stream[AlgorithmStepResult[PopulationType]] = Stream.cons(
      AlgorithmStepResult[PopulationType](0, initialPopulation, bestIndividualForPopulation(initialPopulation, fitness)),
      populations map { step =>
        val individualsWithFitness = step.population.individuals map(fitness.calculate(_))
        val newIndividuals = createNewIndividuals(individualsWithFitness)(parameters, populationContext)
        val newPopulation = populationContext.createPopulationFromIndividuals(newIndividuals)
        AlgorithmStepResult[PopulationType](step.generationNumber + 1, newPopulation, bestIndividualForPopulation(newPopulation, fitness))
      }
    )
    populations
  }

  private def bestIndividualForPopulation(population: PopulationType,
                                          fitness: FitnessFunction[PopulationType]): IndividualWithFitness[IndividualType] =
    population.individuals map(fitness.calculate(_)) maxBy(_.fitness)

  private def createNewIndividuals(individualsWithFitness: Seq[IndividualWithFitness[IndividualType]])
                                  (parameters: GeneticAlgorithmParameters,
                                   populationContext: PopulationContext[PopulationType]): Seq[IndividualType] = {
    @tailrec
    def createNewIndividualsIter(acc: Seq[IndividualType]): Seq[IndividualType] = {
      if(acc.size >= individualsWithFitness.size) {
        acc
      } else {
        val selectedIndividuals = selectIndividuals(populationContext, 2, individualsWithFitness)
        val children = crossoverAndMutate(selectedIndividuals)(parameters, populationContext)
        val childrenNumberToTake = individualsWithFitness.size - acc.size
        createNewIndividualsIter(acc ++ children.take(childrenNumberToTake))
      }
    }
    createNewIndividualsIter(Nil)
  }

  private def selectIndividuals(populationContext: PopulationContext[PopulationType],
                                desiredSelectedCount: Int,
                                individuals: Seq[IndividualWithFitness[IndividualType]]): Seq[IndividualType] =
    for {
      i <- 1 to desiredSelectedCount      
    } yield populationContext.selectionOperator.select(individuals)

  //TODO: dodać prawdopodobieństwa krzyżowania
  private def crossoverAndMutate(individuals: Seq[IndividualType])
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

