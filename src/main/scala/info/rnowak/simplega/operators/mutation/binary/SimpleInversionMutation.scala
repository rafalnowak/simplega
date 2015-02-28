package info.rnowak.simplega.operators.mutation.binary

import info.rnowak.simplega.operators.mutation.MutationOperator
import info.rnowak.simplega.population.BinaryPopulation
import info.rnowak.simplega.population.individual.BinaryIndividual

import scala.util.Random

class SimpleInversionMutation extends MutationOperator[BinaryPopulation] {
  override def mutate(individual: BinaryIndividual): BinaryIndividual = {
    val mutatedLocus = Random.nextInt(individual.length)
    val genes = individual.bits
    val mutatedGenes = genes.slice(0, mutatedLocus) ++ Seq(genes(mutatedLocus).flip) ++ genes.slice(mutatedLocus + 1, genes.length)
    BinaryIndividual(mutatedGenes)
  }
}
