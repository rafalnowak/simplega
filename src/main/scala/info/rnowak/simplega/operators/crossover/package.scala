package info.rnowak.simplega.operators

import info.rnowak.simplega.population.Population

package object crossover {
  type Children[PopulationType <: Population] = Seq[PopulationType#IndividualType]
}
