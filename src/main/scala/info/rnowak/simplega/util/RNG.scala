package info.rnowak.simplega.util

trait RNG {
  def nextInt: (RNG, Int)
  def nonNegativeInt: (RNG, Int)
  def nextDouble: (RNG, Double)
}

case class SimpleRNG(seed: Long) extends RNG {
  override def nextInt: (RNG, Int) = {
    val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
    val nextRNG = SimpleRNG(newSeed)
    val n = (newSeed >>> 16).toInt
    (nextRNG, n)
  }

  override def nonNegativeInt: (RNG, Int) = {
    val (nextRng, randomInt) = this.nextInt
    (nextRng, if (randomInt < 0) -(randomInt + 1) else randomInt)
  }

  override def nextDouble: (RNG, Double) = {
    val (nextRng, randomInt) = this.nonNegativeInt
    (nextRng, randomInt / (Int.MaxValue.toDouble + 1))
  }
}