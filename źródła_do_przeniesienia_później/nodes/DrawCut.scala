package skac.fractalizer.nodes

import skac.fractalizer.Node._
import skac.fractalizer._

/**
 * Node which adds proc channel to draw channel and clears proc channel.
 */
case class DrawCut() extends Node(1) {
  override def proc(graphics: PosGraphics) = Nil
  override def draw(data: NodeData) = data._2 ++ data._1
}
