package info.rnowak.simplega.fitness

case class FitnessValue(value: BigDecimal) {
  override def toString = value.toString
}
