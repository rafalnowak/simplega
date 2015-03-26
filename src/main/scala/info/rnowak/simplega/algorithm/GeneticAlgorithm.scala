package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.{FitnessFunction, FitnessValue, IndividualWithFitness}
import info.rnowak.simplega.operators.crossover.Children
import info.rnowak.simplega.population.Population
import info.rnowak.simplega.population.context.PopulationContext

import scala.annotation.tailrec
import scala.collection.parallel.ParSeq
import scala.util.Random

class GeneticAlgorithm[PopulationType <: Population] {
  type IndividualType = PopulationType#IndividualType

  //TODO: użyć RNG?
  private val randomGenerator = new Random()

  def run(populationContext: PopulationContext[PopulationType])
         (parameters: GeneticAlgorithmParameters)
         (fitness: FitnessFunction[PopulationType]): Stream[AlgorithmStepResult[PopulationType]] = {
    val initialPopulation = populationContext.createInitialPopulation()
    populationStream(initialPopulation)(parameters, populationContext)(fitness) takeWhile (algorithmShouldContinue(_)(parameters))
  }
  
  private def populationStream(initialPopulation: PopulationType)
                              (parameters: GeneticAlgorithmParameters,
                               populationContext: PopulationContext[PopulationType])
                              (fitness: FitnessFunction[PopulationType]): Stream[AlgorithmStepResult[PopulationType]] = {
    val evaluate: PopulationType => ParSeq[IndividualWithFitness[IndividualType]] = evaluatePopulation(_, fitness)
    val evaluateAndReturnBest: PopulationType => IndividualWithFitness[IndividualType] = evaluate andThen bestFitnessValue

    lazy val populations: Stream[AlgorithmStepResult[PopulationType]] = Stream.cons(
      AlgorithmStepResult[PopulationType](0,
        initialPopulation,
        meanFitnessValue(initialPopulation, fitness),
        evaluateAndReturnBest(initialPopulation),
        0),
      populations map { lastStep =>
        val individualsWithFitness = evaluatePopulation(lastStep.population, fitness)
        val newIndividuals = createNewIndividuals(individualsWithFitness)(parameters, populationContext)
        val newPopulation = populationContext.createPopulationFromIndividuals(newIndividuals)
        val newBestFitness = evaluateAndReturnBest(newPopulation)
        AlgorithmStepResult[PopulationType](lastStep.generationNumber + 1,
          newPopulation,
          meanFitnessValue(newPopulation, fitness),
          newBestFitness,
          generationNumberWithoutImprovement(lastStep, newBestFitness.fitness)
        )
      }
    )
    populations
  }

  private def meanFitnessValue(population: PopulationType, fitness: FitnessFunction[PopulationType]): FitnessValue =
    FitnessValue(population.individuals.map(fitness.calculate(_)).map(_.fitness.value).sum /  population.size)

  private def evaluatePopulation(population: PopulationType,
                                 fitness: FitnessFunction[PopulationType]): ParSeq[IndividualWithFitness[IndividualType]] =
    population.individuals.par.map(fitness.calculate(_))

  private def bestFitnessValue(individualsWithFitness: ParSeq[IndividualWithFitness[IndividualType]]): IndividualWithFitness[IndividualType] =
    individualsWithFitness.maxBy(_.fitness)

  private def generationNumberWithoutImprovement(lastStep: AlgorithmStepResult[PopulationType], currentBestFitness: FitnessValue): Long =
    if(lastStep.bestIndividual.fitness != currentBestFitness) {
      0
    } else {
      lastStep.generationWithoutImprovement + 1
    }

  private def createNewIndividuals(individualsWithFitness: ParSeq[IndividualWithFitness[IndividualType]])
                                  (parameters: GeneticAlgorithmParameters,
                                   populationContext: PopulationContext[PopulationType]): Seq[IndividualType] = {
    @tailrec
    def createNewIndividualsIter(individualsSoFar: Seq[IndividualType]): Seq[IndividualType] = {
      if(individualsSoFar.size >= individualsWithFitness.size) {
        individualsSoFar
      } else {
        val selectedIndividuals = selectIndividuals(populationContext, 2, individualsWithFitness)
        val children = crossoverAndMutate(selectedIndividuals)(parameters, populationContext)
        val childrenNumberToTake = individualsWithFitness.size - individualsSoFar.size
        createNewIndividualsIter(individualsSoFar ++ children.take(childrenNumberToTake))
      }
    }
    createNewIndividualsIter(Nil)
  }

  private def selectIndividuals(populationContext: PopulationContext[PopulationType],
                                desiredSelectedCount: Int,
                                individuals: ParSeq[IndividualWithFitness[IndividualType]]): Seq[IndividualType] =
    for {
      i <- 1 to desiredSelectedCount      
    } yield populationContext.selectionOperator.select(individuals)

  private def crossoverAndMutate(individuals: Seq[IndividualType])
                                (parameters: GeneticAlgorithmParameters,
                                 context: PopulationContext[PopulationType]): Children[PopulationType] = {
    val parentFirst = individuals(Random.nextInt(individuals.size))
    val parentSecond = individuals(Random.nextInt(individuals.size))
    val children = crossoverWithProbability(parentFirst, parentSecond)(parameters, context)
    for { 
      child <- children
    } yield mutateWithProbability(child)(parameters, context)
  }

  private def crossoverWithProbability(firstParent: IndividualType, secondParent: IndividualType)
                                      (parameters: GeneticAlgorithmParameters,
                                       context: PopulationContext[PopulationType]): Children[PopulationType] =
    if(randomGenerator.nextDouble() > parameters.crossoverProbability) {
      context.crossOverOperator.crossover(firstParent, secondParent)
    } else {
      Seq(firstParent, secondParent)
    }

  private def mutateWithProbability(individual: IndividualType)
                                   (parameters: GeneticAlgorithmParameters,
                                    context: PopulationContext[PopulationType]): IndividualType =
    if(randomGenerator.nextDouble() > parameters.mutationProbability) {
      context.mutationOperator.mutate(individual)
    } else {
      individual
    }

  //TODO: więcej mądrych warunków stopu
  private def algorithmShouldContinue(step: AlgorithmStepResult[PopulationType])(parameters: GeneticAlgorithmParameters): Boolean =
    step.generationNumber < parameters.maxGenerations || step.generationWithoutImprovement >= parameters.generationsWithoutImprovement
}
