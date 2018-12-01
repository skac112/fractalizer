/**
 * Node which puts on a proc channel circles tangent to two input circles
 * (on input of proc channel). Input circles are not transferred to output.
 * Output circles get their generic attributes from first input circle. Output
 * can contain 0 to 2 circles.
 */
package skac.fractalizer.nodes.tangent

import skac.miro._
import skac.miro.graphics._
import skac.miro.MathUtils._
import skac.fractalizer._
import skac.fractalizer.Node._

case class CirclesTan2(r: Double, outer1: Boolean, outer2: Boolean) extends Node(2) {
  override def procNat(input: PosGraphics) = {
    val ga = input(0)._1.genericAttribs
    val c1 = input(0)._1.toC
    val s1 = input(0)._2
    val c2 = input(1)._1.toC
    val s2 = input(1)._2
    cTanTo2c(s1.x, s1.y, c1.r, outer1, s2.x, s2.y, c2.r, outer2, r) map {solution =>
      (Circle(r, ga), Point(solution._1, solution._2))
    } toSeq
  }
}
