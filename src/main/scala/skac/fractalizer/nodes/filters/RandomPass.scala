package skac.fractalizer.nodes.filters

import skac.fractalizer._
import skac.fractalizer.Node._
import scala.math._

case class RandomPass(passProb: Double, drawRejected: Boolean = true) extends Node(1) {
  var rejected: PosGraphics = _
  override def proc(graphics: PosGraphics) = {
    val passed = graphics filter {_ => random < passProb}
    rejected = graphics diff passed
    passed
  }
  override def draw(data: NodeData) = data._2 ++ rejected
}
