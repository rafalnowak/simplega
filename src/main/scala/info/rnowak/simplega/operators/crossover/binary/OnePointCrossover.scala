package info.rnowak.simplega.operators.crossover.binary

import info.rnowak.simplega.operators.crossover.{Children, CrossoverOperator}
import info.rnowak.simplega.population.BinaryPopulation
import info.rnowak.simplega.population.individual.BinaryIndividual

import scala.util.Random

class OnePointCrossover extends CrossoverOperator[BinaryPopulation] {
  override def crossover(first: BinaryIndividual, second: BinaryIndividual): Children[BinaryPopulation] = {
    require(first.length == second.length, "Individuals must have same length")
    val crossOverPoint = Random.nextInt(first.length)
    Seq(BinaryIndividual(first.bits.take(crossOverPoint) ++ second.bits.drop(crossOverPoint)),
      BinaryIndividual(second.bits.take(crossOverPoint) ++ first.bits.drop(crossOverPoint)))
  }
}
