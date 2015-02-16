package info.rnowak.simplega.population

import info.rnowak.simplega.population.individual.PermutationIndividual

case class PermutationPopulation(individuals: List[PermutationIndividual]) extends Population[PermutationIndividual] {
  override def toString = individuals.mkString("\n")
  
  override def size: Int = individuals.size
}

object PermutationPopulation {
  def initialPopulation(populationCount: Int, individualSize: Int): PermutationPopulation = {
    val individuals = for {
      i <- 1 to populationCount
    } yield PermutationIndividual(individualSize)
    PermutationPopulation(individuals.toList)
  }
}
