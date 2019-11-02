package fractalizer.nodes.stripes

import skac.miro._
import skac.fractalizer._
import skac.miro.graphics.compounds._
import skac.fractalizer.Node._
import scala.math._

/**
 * Node "lenghtening" arc stripe bending in opposite direction to current stripe.
 * Parameter joinDir specifies to which part of stripe a new stripe should be joined:
 * (p3,p4) for true and (p1, p2) for false.
 * Sends input stripe to draw channel.
 */
case class StripeContAlt(rLowCoeff: Double, ar: Angle, joinDir: Boolean) extends Node(1) {
  override def procNat(input: PosGraphics): PosGraphics = {
    val (g, pt) = input(0)
    val stripe = g.asInstanceOf[Stripe]
    val rLow = stripe.rLow * rLowCoeff
    val rHigh = rLow + stripe.width
    val new_sa = if (joinDir) (stripe.ea + Angle(Pi) - ar) else (stripe.sa + Angle(Pi))
    val new_stripe = Stripe(rLow, rHigh, new_sa, ar, stripe.genericAttribs)
    val new_pos = if (joinDir) stripe.p4 - new_stripe.p3 + pt else stripe.p2 + pt
    Seq((new_stripe, new_pos))
  }

  override def draw(data: NodeData): PosGraphics = data._2 ++ data._1
}
