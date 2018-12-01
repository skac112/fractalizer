package skac.fractalizer

import skac.miro._
import skac.miro.Graphic._

/**
  * Node with styler.
  * @param n
  * @param styler
  */
case class StyledNode(override val n: Node, styler: Styler) extends Node {
  override def apply(ens: Ensemble) = {
    for {
      out <- n(ens)
      attrss = styler(ens, out)
    } yield (out zip attrss).map(_ match {
      case ((g, pt), attrs) => (g.setGenericAttribs(attrs), pt)
    })
  }

  override def internalArity = n.internalArity
  override def arity = n.arity
}
