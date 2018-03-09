package skac.fractalizer.nodes

import skac.fractalizer.Node._
import skac.fractalizer._

/**
 * Node which simply clears both channels. It can be helpful in making some
 * combined nodes.
 */
case class Cut() extends Node(1) {
  override def proc(graphics: PosGraphics) = Seq()
  override def draw(data: NodeData) = Seq()
}
