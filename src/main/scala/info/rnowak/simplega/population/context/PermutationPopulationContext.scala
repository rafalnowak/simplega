package info.rnowak.simplega.population.context

import info.rnowak.simplega.population.PermutationPopulation
import info.rnowak.simplega.population.individual.PermutationIndividual

case class PermutationPopulationContext(populationSize: Int, individualLength: Int) extends PopulationContext[PermutationPopulation] {
  override def createPopulationFromIndividuals(individuals: Seq[PermutationIndividual]): PermutationPopulation =
    PermutationPopulation(individuals)

  override def createInitialPopulation(): PermutationPopulation =
    PermutationPopulation.initialPopulation(populationCount = populationSize, individualLength = individualLength)
}
