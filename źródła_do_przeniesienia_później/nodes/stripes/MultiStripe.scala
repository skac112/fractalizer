package fractalizer.nodes.stripes

import skac.miro._
import skac.fractalizer._
import skac.miro.graphics.compounds._
import skac.fractalizer.Node._
import scala.math._

/**
 * Node "lenghtening" arc stripe by adding other adjacent stripes
 */
case class MultiStripe(stripeNum: Int,
  joinDir: Boolean,
  override val stylerO: Option[Styler] = None) extends Node(1) {
  lazy val rand = scala.util.Random

  override def procNat(input: PosGraphics): PosGraphics = {
    val (g, pt) = input(0)
    val stripe = g.asInstanceOf[Stripe]
    var new_stripes = Seq(input(0))
    for (i <- 1 to stripeNum) {
      val last = new_stripes.last
      val d = -0.3
      val e = -0.1
      val r_low_coeff = max(0, 1 + scala.util.Random.nextGaussian*d - e)
      val a_coeff = max(0, 1 + scala.util.Random.nextGaussian*0.4)
      new_stripes = new_stripes :+ nextStripe(last, r_low_coeff, a_coeff,
        joinDir != (i % 2 == 0))
    }
    new_stripes
  }

  def nextStripe(stripePos: PosGraphic,
   rLowCoeff: Double,
   angleCoeff: Double,
   joinDir: Boolean): PosGraphic = {
    val (g, pt) = stripePos
    val stripe = g.asInstanceOf[Stripe]
    val rLow = stripe.rLow * rLowCoeff
    val rHigh = rLow + stripe.width
    val new_ar = stripe.ar*angleCoeff
    val new_sa = if (joinDir) (stripe.ea + Angle(Pi) - new_ar) else (stripe.sa + Angle(Pi))
    val new_stripe = Stripe(rLow, rHigh, new_sa, new_ar, stripe.genericAttribs)
    val new_pos = if (joinDir) stripe.p4 - new_stripe.p3 + pt else stripe.p2 + pt
    (new_stripe, new_pos)
  }
}
