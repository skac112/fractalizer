package fractalizer.nodes

import skac.fractalizer._
import skac.fractalizer.Node._
import skac.miro._

/**
 * Node which proceeds single PosGraphics and generates single PosGraphic.
 */
case class SingleProc(trans: Transform) extends Node(1) {
  override def procNat(graphics: PosGraphics) = Seq(trans(graphics(0)))
  override def draw(data: NodeData) = data._2
}
