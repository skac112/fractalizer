package skac.fractalizer.nodes

import skac.fractalizer.Node._
import skac.fractalizer._
import skac.miro._
import skac.miro.graphics._
import skac.miro.graphics.compounds._
import skac.miro.Dsl._
import scala.math._

/**
 * For given square creates two other squares touching input square in such a
 * manner that three sides of these three triangles create right triangle.
 * Original square is sent to draw channel and not passed to proc channel. Two
 * new squares are passed to proc channel.
 * @param d Relative side length of a first generated square due to the side
 * length of original square.
 */
case class PitagorasTree(
  override val stylerO: Option[Styler] = None,
  addTriangle: Boolean = false) extends Node(1) {
  lazy val r = scala.util.Random
//   def randD = min(0.9, max(0, .5*sqrt(2) + 0.1 * sqrt(2) * r.nextGaussian))
  def randD = sqrt(if (random > .5) random *.4 else 1 - random*.3)
//   def randD = sqrt(random)


//   override def apply(data: NodeData): NodeData = {
//       d = randD
//       super.apply(data)
//   }

  override def procNat(input: PosGraphics): PosGraphics = {
    val (g, pt) = input(0)
    val s = g.toS
    if (s.size > .5) {
        val d = randD
        val h = d*sqrt(1 - d*d)
        val f = sqrt(d*d - h*h)
        // normalized vector from 1-st point of original square to other point of
        // side of 1-st generated square in a coordinate system where original
        // square side being used is horizontal
        val vec1 = Point(f, -h) * s.size
        // angle of vector vec1
        val vec1_ang = atan2(vec1.y, vec1.x)
        // 1-st point of a 1-st generated square
        val p1 = s.tl + ((vec1 rot -.5*Pi) rot s.rotation)
        // normalized vector from 2-nd point of original square to other point of
        // side of 1-st generated square in a coordinate system where original
        // square side being used is horizontal
        // rotation of first generated square
        val rot1 = s.rotation + vec1_ang
        val vec2 = Point(f - 1, -h) * s.size
        val vec2_ang = atan2(vec2.y, vec2.x)
        // 1-st point of a 2-nd generated square
        val p2 = s.tr + ((vec2 + (vec2 rot .5*Pi)) rot s.rotation)
        val rot2 = s.rotation + vec2_ang - Pi
        // side length of first square
        val size1 = d*s.size
        // side length of second square
        val size2 = sqrt(1 - d*d)*s.size
        Seq((Square(size1, rot1), p1 + pt),(Square(size2, rot2), p2 + pt))
    }
    else {
        Nil
    }
  }

  /**
   * Implementation of draw adds proc channel to draw channel
   */
  override def draw(data: NodeData): PosGraphics = {
//     val squares = data._1
//     val new_elems = if (addTriangle) {
//       squares flatMap {case (g, pt) => {
//         val s = g.toS

//             val h = d*sqrt(1 - d*d)
//             val f = sqrt(d*d - h*h)
//             // normalized vector from 1-st point of original square to other point of
//             // side of 1-st generated square in a coordinate system where original
//             // square side being used is horizontal
//             val vec1 = Point(f, -h) * s.size
//             // right triangle between squares
//             val triangle = Triangle(s.tl + vec1 rot s.rotation, s.tr)
//             Seq((g, pt), (triangle, pt))

//       }}
//     }
//     else {
//         squares
//     }
//     data._2 ++ new_elems
      data._2 ++ data._1
  }
}
