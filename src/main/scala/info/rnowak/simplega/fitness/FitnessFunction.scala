package info.rnowak.simplega.fitness

import info.rnowak.simplega.population.Population

case class FitnessFunction[PopulationType <: Population](calculate: PopulationType#IndividualType => IndividualWithFitness[PopulationType#IndividualType])