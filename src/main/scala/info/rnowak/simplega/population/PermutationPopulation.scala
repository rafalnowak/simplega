package info.rnowak.simplega.population

case class PermutationPopulation(individuals: List[PermutationIndividual]) extends Population[PermutationIndividual] {
  override def toString = individuals.mkString("\n")
}

object PermutationPopulation {
  def initialPopulation(populationCount: Int, individualSize: Int): PermutationPopulation = {
    val individuals = for {
      i <- 1 to populationCount
    } yield PermutationIndividual(individualSize)
    PermutationPopulation(individuals.toList)
  }
}
