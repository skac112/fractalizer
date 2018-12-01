package skac.fractalizer.nodes

import skac.fractalizer.Node._
import skac.fractalizer._

/**
 * Node which simply passes both channels. It can be helpful in making some
 * combined nodes.
 */
case class Pass() extends Node(1) {
  override def proc(graphics: PosGraphics) = graphics
  override def draw(data: NodeData) = data._2
}
