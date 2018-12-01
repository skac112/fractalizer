package skac.fractalizer.nodes.special

import cats.data.Writer
import skac.fractalizer._
import skac.miro._

/**
  * Node which sends contents of a proc channel to draw and clears proc channel.
  * @param arity
  */
case class DrawCut(override val arity: Int = 0) extends SimpleNode {
  override def apply(ens: Ensemble) = Writer(ens, Nil)
}
