package info.rnowak.simplega.operators.crossover

import info.rnowak.simplega.population.Population

trait CrossOverOperator[PopulationType <: Population] {
  def crossover(individualFirst: PopulationType#IndividualType, individualSecond: PopulationType#IndividualType): Children[PopulationType]
}
