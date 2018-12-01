/**
 * Node which puts on a proc channel circles tangent to three input circles
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

case class CirclesTan3(outer1: Boolean, outer2: Boolean, outer3: Boolean) extends Node(3) {
  override def procNat(input: PosGraphics) = {
    val ga = input(0)._1.genericAttribs
    val c1 = input(0)._1.toC
    val s1 = input(0)._2
    val c2 = input(1)._1.toC
    val s2 = input(1)._2
    val c3 = input(2)._1.toC
    val s3 = input(2)._2
    cTanTo3c(s1.x, s1.y, c1.r, outer1, s2.x, s2.y, c2.r, outer2, s3.x, s3.y, c3.r, outer3) map {solution =>
      (Circle(solution._3, ga), Point(solution._1, solution._2))
    } toSeq
  }
}
