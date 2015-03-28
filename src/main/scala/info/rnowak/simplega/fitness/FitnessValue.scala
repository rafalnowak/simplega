package info.rnowak.simplega.fitness

case class FitnessValue(value: BigDecimal) extends Ordered[FitnessValue] {
  override def toString = value.toString()

  override def compare(that: FitnessValue): Int = value compare that.value
}

object FitnessValue {
  implicit class FitnessFromBigDecimal(value: BigDecimal) {
    def fitness: FitnessValue = FitnessValue(value)
  }

  implicit class FitnessFromBigDouble(value: Double) {
    def fitness: FitnessValue = FitnessValue(value)
  }
}