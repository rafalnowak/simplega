package info.rnowak.simplega.population.context

import info.rnowak.simplega.population.Population

//TODO: wywalić tę klasę
trait PopulationContext[PopulationType <: Population] {
  def populationSize: Int
  def individualLength: Int

  def createPopulationFromIndividuals(individuals: Seq[PopulationType#IndividualType]): PopulationType
  def createInitialPopulation(): PopulationType
}
