package info.rnowak.simplega.population.individual

sealed trait Bit {
  def value: Int
  def flip: Bit
}

case object One extends Bit {
  override val value = 1
  override val flip = Zero
  override def toString = value.toString
}

case object Zero extends Bit {
  override val value = 0
  override val flip = One
  override def toString = value.toString
}

object Bit {
  def apply(value: Int): Bit = if(value == 0) {
    Zero
  } else {
    One
  }
}
