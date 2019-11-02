package fractalizer.nodes.filters

import skac.fractalizer._
import skac.fractalizer.Node._
import scala.math._

case class FilterOutFirst(dropNum: Int) extends Node(1) {
  var rejected: PosGraphics = _

  override def proc(graphics: PosGraphics) = {
    val passed = graphics drop dropNum
    rejected = graphics take dropNum
    passed
  }

  override def draw(data: NodeData) = data._2 ++ rejected
}
