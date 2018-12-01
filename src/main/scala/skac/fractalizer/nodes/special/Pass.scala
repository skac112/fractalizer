package skac.fractalizer.nodes.special

import cats.data.Writer
import skac.fractalizer._
import skac.miro._

/**
  * Node which simply passes both channels (no-op node). It can be helpful in making some
  * combined nodes.
  */
class Pass extends SimpleNode {
  override def apply(ens: Ensemble) = Writer(Nil, ens)
}
