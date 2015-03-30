package info.rnowak.simplega.operators.selection

import info.rnowak.simplega.fitness.IndividualWithFitness
import info.rnowak.simplega.population.Population

import scala.collection.parallel.ParSeq
import scala.util.Random

class SimpleTournamentSelection[PopulationType <: Population] extends SelectionOperator[PopulationType] {
  type IndividualType = PopulationType#IndividualType

  override def select(individualsWithFitness: ParSeq[IndividualWithFitness[IndividualType]]): IndividualType = {
    val first = individualsWithFitness(Random.nextInt(individualsWithFitness.size))
    val second = individualsWithFitness(Random.nextInt(individualsWithFitness.size))
    if(first.fitness > second.fitness) first.individual else second.individual
  }
}
