package skac.fractalizer.nodes

import skac.fractalizer.Node._
import skac.fractalizer._

/**
 * Node which adds proc channel to draw channel and doesn't change proc channel.
 */
case class DrawPass() extends Node(1) {
  override def proc(graphics: PosGraphics) = graphics
  override def draw(data: NodeData) = data._2 ++ data._1
}
