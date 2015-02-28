package info.rnowak.simplega.population.individual

import scala.util.Random

case class BinaryIndividual(bits: Seq[Bit]) extends Individual {
  override def length: Int = bits.size
  
  override def toString = bits.mkString("")
}

object BinaryIndividual {
  def apply(individualLength: Int): BinaryIndividual = 
    BinaryIndividual((1 to individualLength) map { i => Random.nextInt(2) })
}