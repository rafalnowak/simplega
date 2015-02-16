package info.rnowak.simplega.population

trait Population[IndividualType <: Individual] {
  val individuals: List[IndividualType]
}
