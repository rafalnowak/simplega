package info.rnowak.simplega.algorithm

import info.rnowak.simplega.fitness.{FitnessFunction, FitnessValue, IndividualWithFitness}
import info.rnowak.simplega.operators.crossover.Children
import info.rnowak.simplega.population.Population
import info.rnowak.simplega.population.context.PopulationContext

import scala.annotation.tailrec
import scala.collection.parallel.ParSeq
import scala.util.Random

class GeneticAlgorithm[PopulationType <: Population](parameters: GeneticAlgorithmParameters, populationContext: PopulationContext[PopulationType]) {
  type IndividualType = PopulationType#IndividualType

  //TODO: użyć RNG?
  private val randomGenerator = new Random()

  def run(fitness: FitnessFunction[PopulationType]): Stream[AlgorithmStepResult[PopulationType]] = {
    val initialPopulation = populationContext.createInitialPopulation()
    algorithmStream(initialPopulation)(fitness) takeWhile (algorithmShouldContinue(_)(parameters))
  }
  
  private def algorithmStream(initialPopulation: PopulationType)
                             (fitness: FitnessFunction[PopulationType]): Stream[AlgorithmStepResult[PopulationType]] = {
    val evaluate: PopulationType => ParSeq[IndividualWithFitness[IndividualType]] = evaluatePopulation(_, fitness)
    val evaluateAndReturnBest: PopulationType => IndividualWithFitness[IndividualType] = evaluate andThen bestFitnessValue

    lazy val algorithmSteps: Stream[AlgorithmStepResult[PopulationType]] = Stream.cons(
      AlgorithmStepResult.zero[PopulationType](
        initialPopulation,
        meanFitnessValue(initialPopulation, fitness),
        evaluateAndReturnBest(initialPopulation)
      ),
      algorithmSteps map { lastStep =>
        val individualsWithFitness = evaluate(lastStep.population)
        val newIndividuals = createNewIndividuals(individualsWithFitness)
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
    algorithmSteps
  }

  private def meanFitnessValue(population: PopulationType, fitness: FitnessFunction[PopulationType]): FitnessValue =
    FitnessValue(evaluatePopulation(population, fitness).map(_.fitness.value).sum /  population.size)

  private def evaluatePopulation(population: PopulationType,
                                 fitness: FitnessFunction[PopulationType]): ParSeq[IndividualWithFitness[IndividualType]] =
    population.individuals.par.map(fitness.calculate)

  private def bestFitnessValue(individualsWithFitness: ParSeq[IndividualWithFitness[IndividualType]]): IndividualWithFitness[IndividualType] =
    individualsWithFitness.maxBy(_.fitness)

  private def generationNumberWithoutImprovement(lastStep: AlgorithmStepResult[PopulationType], currentBestFitness: FitnessValue): Long =
    if(lastStep.bestIndividual.fitness != currentBestFitness) {
      0
    } else {
      lastStep.generationWithoutImprovement + 1
    }

  private def createNewIndividuals(individualsWithFitness: ParSeq[IndividualWithFitness[IndividualType]]): Seq[IndividualType] = {
    @tailrec
    def createNewIndividualsIter(individualsSoFar: Seq[IndividualType]): Seq[IndividualType] = {
      if(individualsSoFar.size >= individualsWithFitness.size) {
        individualsSoFar
      } else {
        val selectedIndividuals = selectIndividuals(2, individualsWithFitness)
        val children = crossoverAndMutate(selectedIndividuals)
        val childrenNumberToTake = individualsWithFitness.size - individualsSoFar.size
        createNewIndividualsIter(individualsSoFar ++ children.take(childrenNumberToTake))
      }
    }
    createNewIndividualsIter(Nil)
  }

  private def selectIndividuals(desiredSelectedCount: Int,
                                individuals: ParSeq[IndividualWithFitness[IndividualType]]): Seq[IndividualType] =
    for {
      i <- 1 to desiredSelectedCount      
    } yield populationContext.selectionOperator.select(individuals)

  private def crossoverAndMutate(individuals: Seq[IndividualType]): Children[PopulationType] = {
    val parentFirst = individuals(Random.nextInt(individuals.size))
    val parentSecond = individuals(Random.nextInt(individuals.size))
    val children = crossoverWithProbability(parentFirst, parentSecond)
    for { 
      child <- children
    } yield mutateWithProbability(child)
  }

  private def crossoverWithProbability(firstParent: IndividualType, secondParent: IndividualType): Children[PopulationType] =
    if(randomGenerator.nextDouble() > parameters.crossoverProbability) {
      populationContext.crossOverOperator.crossover(firstParent, secondParent)
    } else {
      Seq(firstParent, secondParent)
    }

  private def mutateWithProbability(individual: IndividualType): IndividualType =
    if(randomGenerator.nextDouble() > parameters.mutationProbability) {
      populationContext.mutationOperator.mutate(individual)
    } else {
      individual
    }

  //TODO: więcej mądrych warunków stopu
  private def algorithmShouldContinue(step: AlgorithmStepResult[PopulationType])(parameters: GeneticAlgorithmParameters): Boolean =
    step.generationNumber < parameters.maxGenerations && step.generationWithoutImprovement <= parameters.generationsWithoutImprovement
}
