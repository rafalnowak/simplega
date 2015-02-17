package info.rnowak.simplega.population

import info.rnowak.simplega.population.individual.Individual

trait Population {
  type IndividualType <: Individual

  val individuals: List[IndividualType]
  
  def size: Int
}
