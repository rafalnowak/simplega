package info.rnowak.simplega.operators.crossover

import info.rnowak.simplega.population.Population

trait CrossoverOperator[PopulationType <: Population] {
  def crossover(first: PopulationType#IndividualType, second: PopulationType#IndividualType): Children[PopulationType]
}
