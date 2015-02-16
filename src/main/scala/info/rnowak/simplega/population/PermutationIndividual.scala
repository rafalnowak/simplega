package info.rnowak.simplega.population

import scala.util.Random

case class PermutationIndividual(permutation: List[Int]) extends Individual {
  override def size: Int = permutation.size

  override def toString = permutation.mkString(", ")
}

object PermutationIndividual {
  def apply(individualSize: Int): PermutationIndividual =
    PermutationIndividual(Random.shuffle(1 to individualSize).toList)
}