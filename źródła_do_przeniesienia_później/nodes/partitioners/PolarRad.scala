package skac.fractalizer.nodes.partitioners

import skac.fractalizer._
import skac.fractalizer.Node._
import skac.miro.graphics._
import skac.miro.graphics.compounds._
import skac.miro.Graphic._
import skac.miro._

object PolarRad {
  def uniform(parts: Int, stylerO: Option[Styler] = None, randomRot: Boolean = true) =
    PolarRad(Partition.uniform(parts - 1), stylerO)
}

/**
 * Dzieli ksztalt "polarny" (Circle, Ring, ArcSection lub Stripe) "promieniowo"
 * z uzyciem zadanego podzialu.
 */
case class PolarRad(partition: () => Seq[Double],
 override val stylerO: Option[Styler] = None) extends Node(1) {

  def this(parts: Int, stylerO: Option[Styler], randomRot: Boolean ) =
    this(Partition.uniform(parts - 1), stylerO)

  override def procNat(input: PosGraphics): PosGraphics = {
    def procNatCircle(r: Double, ga: GenericAttribs, pt: Point) = {
      // kolo dzielone jest na kolo + pierscienie
      // unormowanie podzialu do zakresu wyznaczonego <0; <promien okregu>>
      val normPart = Partition.mapToRange(partition(), .0, r).drop(1)
      val circle = Circle(normPart(0), ga)
      val rings = normPart.sliding(2) map {low_high =>
        val ring = Ring(low_high(0), low_high(1), ga)
        val ring_pt = pt - ring.c
        (ring, ring_pt)
      } toSeq;
      (circle, pt) +: rings
    }

    def procNatArcSection(r: Double, sa: Angle, ar: Angle, ga: GenericAttribs, pt: Point) = {
      // wycinek kola dzielony jest na wycinek kola w srodku oraz wycinki
      // pierscieni
      // unormowanie podzialu do zakresu <0; promien>
      val normPart = Partition.mapToRange(partition(), .0, r).drop(1)
      val arc_section = ArcSection(normPart(0), sa, ar, ga)
      val stripes = normPart.sliding(2) map {low_high =>
        val stripe = Stripe(low_high(0), low_high(1), sa, ar, ga)
        val stripe_pt = pt - stripe.c
        (stripe, stripe_pt)
      } toSeq;
      (arc_section, pt) +: stripes
    }

    def procNatRing(rl: Double, rh: Double, ga: GenericAttribs, pt: Point) = {
      // pierscien dzielony jest na pierscienie
      // unormowanie podzialu do zakresu <rl; rh>
      val normPart = Partition.mapToRange(partition(), rl, rh)  
      normPart.sliding(2) map {low_high =>
        val ring = Ring(low_high(0), low_high(1), ga)
        val ring_pt = pt - ring.c
        (ring, ring_pt)
      } toSeq;
    }

    def procNatStripe(rl: Double, rh: Double, sa: Angle, ar: Angle, ga: GenericAttribs, pt: Point) = {
      // wycinek piersciena dzielony jest na wycinki pierscienia
      // unormowanie podzialu o zakresu <rl; rh>
      val normPart = Partition.mapToRange(partition(), rl, rh)
      val arc_section = ArcSection(normPart(0), sa, ar, ga)
      normPart.sliding(2) map {low_high =>
        val stripe = Stripe(low_high(0), low_high(1), sa, ar, ga)
        val stripe_pt = pt - stripe.c
        (stripe, stripe_pt)
      } toSeq;
    }

    val (g, pt) = input(0)
    g match {
      case c @ Circle(r, ga) => procNatCircle(r, ga, pt)
      case as @ ArcSection(r, sa, ar, ga) => procNatArcSection(r, sa, ar, ga, pt + as.c)
      case r @ Ring(rl, rh, ga) => procNatRing(rl, rh, ga, pt + r.c)
      case s @ Stripe(rl, rh, sa, ar, ga) => procNatStripe(rl, rh, sa, ar, ga, pt + s.c)
    }
  }
}
