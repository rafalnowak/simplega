package info.rnowak.simplega.fitness

case class FitnessValue(value: BigDecimal) extends Ordered[FitnessValue] {
  override def toString = value.toString()

  override def compare(that: FitnessValue): Int = value compare that.value
}
