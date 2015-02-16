package info.rnowak.simplega.population

sealed trait PopulationContext[PopulationType <: Population[_]] {
  def populationSize: Int
  def individualLength: Int
  
  def createInitialPopulation(): PopulationType
}

case class PermutationPopulationContext(populationSize: Int, individualLength: Int) extends PopulationContext[PermutationPopulation] {
  override def createInitialPopulation(): PermutationPopulation =
    PermutationPopulation.initialPopulation(populationCount = populationSize, individualLength = individualLength)
}
