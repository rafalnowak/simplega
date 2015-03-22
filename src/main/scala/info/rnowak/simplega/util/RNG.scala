package info.rnowak.simplega.util

trait RNG {
  def nextInt: (Int, RNG)
  def nextDouble: (Double, RNG)
}

case class SimpleRNG(seed: Long) extends RNG {
  def nextInt: (Int, RNG) = {
    val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
    val nextRNG = SimpleRNG(newSeed)
    val n = (newSeed >>> 16).toInt
    (n, nextRNG)
  }

  def nonNegativeInt: (Int, RNG) = {
    val (newInt, nextRng) = this.nextInt
    (if (newInt < 0) -(newInt + 1) else newInt, nextRng)
  }

  def nextDouble: (Double, RNG) = {
    val (newInt, newRng) = this.nonNegativeInt
    (newInt / (Int.MaxValue.toDouble + 1), newRng)
  }
}