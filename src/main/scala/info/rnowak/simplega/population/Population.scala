package info.rnowak.simplega.population

import info.rnowak.simplega.population.individual.Individual

trait Population[IndividualType <: Individual] {
  val individuals: List[IndividualType]
  
  def size: Int
}
