package fractalizer.nodes

import skac.fractalizer.Node._
import skac.fractalizer._
import skac.miro._
import skac.miro.graphics._
import skac.miro.graphics.compounds._
import skac.miro.Dsl._

/**
 * Creates graphics vaguely resembling an eye (rather abstract looking one).
 * @param angle Angle of leaf
 * @param r1Coeff r1 / r where r1 is a radius of circle inscribed in leaf and r
 * is a radius of an input circle
 * @param r2Coeff r2 / r where r2 is a radius of circle withing r1 circle and r
 * is a radius of an input circle
 */
case class Eye(
 angle: Angle,
 r1Coeff: Double,
 r2Coeff: Double,
 override val stylerO: Option[Styler] = None) extends Node(1) {
  override def procNat(input: PosGraphics): PosGraphics = {
    val (g, pt) = input(0)
    val c = g.toC
    val r1 = r1Coeff * c.r
    val leaf = Leaf.uniform(Point(2*c.r, 0) rot angle, r1)
    val leaf_p = (leaf, (Point(-c.r, 0) rot angle) + pt)
    val c1_p = (C(r1), pt)
    val r2 = r2Coeff * c.r
    val c2_p = (C(r2), pt)
    Seq(input(0), leaf_p, c1_p, c2_p)
  }
}
