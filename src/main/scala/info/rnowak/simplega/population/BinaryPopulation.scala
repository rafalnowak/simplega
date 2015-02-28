package info.rnowak.simplega.population

import info.rnowak.simplega.population.individual.BinaryIndividual

case class BinaryPopulation(individuals: Seq[BinaryIndividual]) extends Population {
  override type IndividualType = BinaryIndividual

  override def toString = individuals.mkString("\n")
  
  override def size: Int = individuals.size
}

object BinaryPopulation {
  //TODO: uwsplonic z permutacyjnymi
  def initialPopulation(populationCount: Int, individualLength: Int): BinaryPopulation = {
    val individuals = for {
      i <- 1 to populationCount
    } yield BinaryIndividual(individualLength)
    BinaryPopulation(individuals)
  }
}