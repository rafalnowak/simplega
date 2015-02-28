package info.rnowak.simplega.population

package object individual {
  sealed trait Bit {
    def value: Int    
  }
  
  case object One extends Bit {
    override val value = 1
    override def toString = value.toString    
  }
  
  case object Zero extends Bit {
    override val value = 0
    override def toString = value.toString
  }
  
  object Bit {
    def apply(value: Int): Bit = if(value == 0) {
      Zero
    } else {
      One
    }
  }
}
