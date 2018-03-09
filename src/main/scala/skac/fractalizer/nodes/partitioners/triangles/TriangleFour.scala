package skac.fractalizer.nodes.partitioners.triangles

import skac.fractalizer._
import skac.fractalizer.Node._
import skac.miro.graphics._
import skac.miro.graphics.compounds._
import skac.miro.Graphic._
import skac.miro._
import scala.math._

/**
 * Partitions triangle into four triangles. They are determined by three points
 * on three sides of original triangle together with three vertices of original
 * triangle. From these four triangles one of them ("middle triangle") has
 * as vertices three points on sides of original triangles and each of three
 * other ("corner triangles") has original triangle vertex as one vertex and two
 * of points on sides  as two other vertices. Points on sides are specified by
 * numbers equal to relative length (normalized to 1) due to side length. So,
 * points on sides are defined as:
 * pm1 = pt1 + m1 * (pt2 - pt1) = m1 * pt2 (pt1 = Point(0, 0))
 * pm2 = pt2 + m2 * (pt3 - pt2) = m2 * pt3 + (1 - m2) * pt2
 * pm3 = pt3 + m3 * (pt1 - pt3) = m3 * pt1 + (1 - m3) * pt3 = (1 - m3) * pt3 (pt1 = Point(0, 0))
 * Default values of m1, m2 and m3 generate triangles similar to original one.
 */
case class TriangleFour(override val stylerO: Option[Styler] = None,
 m1: Double = .5,
 m2: Double = .5,
 m3: Double = .5) extends Node(1) {
  override def procNat(input: PosGraphics): PosGraphics = {
    val (g, pt) = input(0)
    val t = g toT
    val pm1 = t.pt2 * m1
    val pm2 = t.pt3 * m2 + t.pt2 * (1 - m2)
    val pm3 = t.pt3 * (1 - m3)
    // "middle triangle"
    val tm = Triangle(pm2 - pm1, pm3 - pm1)
    // 1-st "corner" triangle - has pt1 (ori) as one of its vertices
    val tc1 = Triangle(pm1, pm3)
    // 2-nd "corner" triangle - has pt2 as one of its vertices
    val tc2 = Triangle(t.pt2 - pm1, pm2 - pm1)
    // 3-rd "corner" triangle - has pt3 as one of its vertices
    val tc3 = Triangle(pm2 - pm3, t.pt3 - pm3)
    Seq((tm, pt + pm1), (tc1, pt), (tc2, pt + pm1), (tc3, pt + pm3))
  }
}
