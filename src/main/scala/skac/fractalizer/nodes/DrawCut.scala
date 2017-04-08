package skac.fractalizer.nodes

import skac.fractalizer.Node._
import skac.fractalizer._

/**
 * Węzeł, który dodaje kanał przetwarzania do kanału rysowania i oczyszcza kanał
 * przetwarzania.
 */
case class DrawCut() extends Node(1) {
  override def proc(graphics: PosGraphics) = Nil
  override def draw(data: NodeData) = data._2 ++ data._1
}
