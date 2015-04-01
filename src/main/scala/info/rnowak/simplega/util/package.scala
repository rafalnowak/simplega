package info.rnowak.simplega

import scalaz.State

package object util {
  type Rand[A] = State[RNG, A]
}
