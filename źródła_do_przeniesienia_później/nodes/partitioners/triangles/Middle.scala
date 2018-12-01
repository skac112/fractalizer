package skac.fractalizer.nodes.partitioners.triangles

import skac.fractalizer._
import skac.fractalizer.Node._
import skac.miro.graphics._
import skac.miro.graphics.compounds._
import skac.miro.Graphic._
import skac.miro._
import scala.math._
import skac.miro.segments.LineSeg._

/**
 * Partitions triangle into three triangles ("homo-partition") determined by
 * vertices of input triangle and middle point.
 */
case class Middle(override val stylerO: Option[Styler] = None) extends Node(1) {
  override def procNat(input: PosGraphics): PosGraphics = {
    val (g, pt) = input(0)
    val t = g toT
    val mid = t mid
    val t1 = Triangle(-mid, t.pt2 - mid)
    val t2 = Triangle(t.pt2 - mid, t.pt3 - mid)
    val t3 = Triangle(t.pt3 - mid, -mid)
    val new_pt = mid + pt
    Seq((t1, new_pt), (t2, new_pt), (t3, new_pt))
  }
}
