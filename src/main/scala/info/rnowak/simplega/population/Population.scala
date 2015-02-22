package info.rnowak.simplega.population

import info.rnowak.simplega.population.individual.Individual

trait Population {
  type IndividualType <: Individual

  val individuals: Seq[IndividualType]
  
  def size: Int
}
