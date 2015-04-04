package info.rnowak.simplega

import scalaz.State

package object util {
  type Rand[A] = State[RNG, A]

  val randomInt: Rand[Int] = State(_.nextInt)
  val randomDouble: Rand[Double] = State(_.nextDouble)
}
