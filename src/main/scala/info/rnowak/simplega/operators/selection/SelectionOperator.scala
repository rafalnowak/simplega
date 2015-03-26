package info.rnowak.simplega.operators.selection

import info.rnowak.simplega.fitness.IndividualWithFitness
import info.rnowak.simplega.population.Population

import scala.collection.parallel.ParSeq

trait SelectionOperator[PopulationType <: Population] {
  def select(individualsWithFitness: ParSeq[IndividualWithFitness[PopulationType#IndividualType]]): PopulationType#IndividualType
}
