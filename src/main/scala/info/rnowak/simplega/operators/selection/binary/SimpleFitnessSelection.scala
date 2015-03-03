package info.rnowak.simplega.operators.selection.binary

import info.rnowak.simplega.fitness.IndividualWithFitness
import info.rnowak.simplega.operators.selection.SelectionOperator
import info.rnowak.simplega.population.BinaryPopulation
import info.rnowak.simplega.population.individual.BinaryIndividual

//TODO: zastąpić czymś sensowniejszym
class SimpleFitnessSelection extends SelectionOperator[BinaryPopulation] {
  override def selection(individualsWithFitness: Seq[IndividualWithFitness[BinaryIndividual]]): BinaryIndividual = {
    individualsWithFitness.head.individual
  }
}
