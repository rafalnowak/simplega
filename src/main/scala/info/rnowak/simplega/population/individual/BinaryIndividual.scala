package info.rnowak.simplega.population.individual

case class BinaryIndividual(bits: List[Bit]) extends Individual {
  override def length: Int = bits.size
  
  override def toString = bits.mkString("")
}
