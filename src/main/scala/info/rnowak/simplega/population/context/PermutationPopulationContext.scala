package info.rnowak.simplega.population.context

import info.rnowak.simplega.population.PermutationPopulation

case class PermutationPopulationContext(populationSize: Int, individualLength: Int) extends PopulationContext[PermutationPopulation] {
  override def createInitialPopulation(): PermutationPopulation =
    PermutationPopulation.initialPopulation(populationCount = populationSize, individualLength = individualLength)
}
