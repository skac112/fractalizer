package skac.fractalizer.nodes.special

import cats.data.Writer
import skac.fractalizer._
import skac.miro.Ensemble

/**
  * Node which draws contents of a proc channel doesn't change proc channel (passes it through).
  */
case class DrawPass(override val arity: Int = 0) extends SimpleNode {
  override def apply(ens: Ensemble) = Writer(ens, ens)
}
