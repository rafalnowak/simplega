package info.rnowak.simplega.operators.selection.binary

import info.rnowak.simplega.fitness.IndividualWithFitness
import info.rnowak.simplega.operators.selection.SelectionOperator
import info.rnowak.simplega.population.BinaryPopulation
import info.rnowak.simplega.population.individual.BinaryIndividual

import scala.collection.parallel.ParSeq
import scala.util.Random

class SimpleTournamentSelection extends SelectionOperator[BinaryPopulation] {
  override def select(individualsWithFitness: ParSeq[IndividualWithFitness[BinaryIndividual]]): BinaryIndividual = {
    val first = individualsWithFitness(Random.nextInt(individualsWithFitness.size))
    val second = individualsWithFitness(Random.nextInt(individualsWithFitness.size))
    if(first.fitness > second.fitness) first.individual else second.individual
  }
}
