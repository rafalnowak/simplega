package info.rnowak.simplega.population

import info.rnowak.simplega.population.individual.PermutationIndividual

case class PermutationPopulation(individuals: List[PermutationIndividual]) extends Population {
  override type IndividualType = PermutationIndividual

  override def toString = individuals.mkString("\n")
  
  override def size: Int = individuals.size
}

object PermutationPopulation {
  def initialPopulation(populationCount: Int, individualLength: Int): PermutationPopulation = {
    val individuals = for {
      i <- 1 to populationCount
    } yield PermutationIndividual(individualLength)
    PermutationPopulation(individuals.toList)
  }
}
