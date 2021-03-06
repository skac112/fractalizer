package com.github.skac112.fractalizer.nodes.partitioners

import com.github.skac112.fractalizer._
import com.github.skac112.fractalizer.Node._
import com.github.skac112.miro.graphics._
import com.github.skac112.miro.graphics.compounds._
import com.github.skac112.miro.Graphic._
import com.github.skac112.miro._
import com.github.skac112.vgutils.{Point, Angle}
import scala.math._

object PolarAng {
  def uniform(parts: Int, stylerO: Option[Styler] = None, randomRot: Boolean = true) =
    new PolarAng(Partition.uniform(parts - 1), stylerO, randomRot)
}

/**
 * Dzieli ksztalt "polarny" (Circle, Ring, ArcSection lub Stripe) "kątowo"
 * z użyciem zadanego podzialu. Mozliwe jest zadanie wykonywania losowego obrotu
 * przy kazdym elemencie przetwarzanym, dla ktorego ma to zastosowanie
 */
case class PolarAng(partition: Partition,
 override val stylerO: Option[Styler] = None, randomRot: Boolean = true) extends BasicPartitioner {

   def this(parts: Int, stylerO: Option[Styler], randomRot: Boolean) =
     this(Partition.uniform(parts - 1), stylerO, randomRot)

   /**
    * Cyrkularna wersja podzialu partition. Tylko jedna wersja w ramach
    * zycia obiektu, dzieki czemu losowy obrot bedzie ten sam przy kazdym uzyciu.
    */
  lazy val circPart = partition.circleValues(rotVal)

  lazy val rotVal = if (randomRot) random * 2 * Pi else .0

  def procNatCircle(r: Double, ga: GenericAttribs, pt: Point) = {
    // kolo dzielone jest na wycinki kola
    circPart.sliding(2) map {low_high =>
      val (a1, a2) = (low_high(0), low_high(1))
      val arc_sec = ArcSection(r, a1, a2 - a1, ga)
      val as_pt = pt - arc_sec.c
      (arc_sec, as_pt)
    } toSeq;
  }

  def procNatArcSection(r: Double, sa: Angle, ar: Angle, ga: GenericAttribs, pt: Point) = {
    // wycinek kola dzielony jest na wycinki kola
    partition.angleValues(sa, (sa + ar)).sliding(2) map {low_high =>
      val (a1, a2) = (Angle(low_high(0)), Angle(low_high(1)))
      val arc_sec = ArcSection(r, a1, a2 - a1, ga)
      val as_pt = pt - arc_sec.c
      (arc_sec, as_pt)
    } toSeq;
  }

  def procNatRing(rl: Double, rh: Double, ga: GenericAttribs, pt: Point) = {
    val rand_rot: Double = if (randomRot) Angle(random * 2 * Pi) else .0
    // pierscien dzielony jest na wycinki pierscienia
    circPart.sliding(2) map {low_high =>
      val (a1, a2) = (Angle(low_high(0) + rand_rot), Angle(low_high(1) + rand_rot))
      val stripe = Stripe(rl, rh, a1, a2 - a1, ga)
      val stripe_pt = pt - stripe.c
      (stripe, stripe_pt)
    } toSeq;
  }

  def procNatStripe(rl: Double, rh: Double, sa: Angle, ar: Angle, ga: GenericAttribs, center: Point) = {
    // wycinek pierscienia dzielony jest na wycinki pierscienia
    partition.angleValues(sa, (sa + ar)).sliding(2) map {low_high =>
      val (a1, a2) = (Angle(low_high(0)), Angle(low_high(1)))
      val stripe = Stripe(rl, rh, a1, a2 - a1, ga)
      // Using the fact that stripe circle centers are the same
      val stripe_pt = center - stripe.c
      (stripe, stripe_pt)
    } toSeq;
  }

  def procNatDirectedStripe(width: Double, length: Double, curvature: Double, sa: Angle, ga: GenericAttribs,
                            center: Point) = {
    partition.rangeValues(0, length).sliding(2) map { low_high =>
      // start angle of current produced directed stripe
      val a_start = sa + Angle(low_high(0) / curvature)
      // length of current produced directed stripe
      val len = low_high(1) - low_high(0)
      val stripe = DirectedStripe(width, len, curvature, a_start, ga)
      // Using the fact that stripe circle centers are the same
      val stripe_pt = center - stripe.cO.get
      (stripe, stripe_pt)
    } toSeq;
  }

  def procNatDirectedStripeRect(width: Double, length: Double, sa: Angle, ga: GenericAttribs, pt: Point) = {
    partition.rangeValues(0, length).sliding(2) map { low_high =>
      // length of current produced rectangular (degenerated) directed stripe
      val len = low_high(1) - low_high(0)
      // current rectangular (degenerated) directed stripe
      val stripe = DirectedStripe(width, len, 0, sa, ga)
      (stripe, pt + Math.signum(sa)*len)
    } toSeq;
  }

  override def procFun(g: Graphic, pt: Point): Ensemble = {
    g match {
      case c @ Circle(r, ga) => procNatCircle(r, ga, pt)
      case as @ ArcSection(r, sa, ar, ga) => procNatArcSection(r, sa, ar, ga, pt + as.c)
      case r @ Ring(rl, rh, ga) => procNatRing(rl, rh, ga, pt + r.c)
      case s @ Stripe(rl, rh, sa, ar, ga) => procNatStripe(rl, rh, sa, ar, ga, pt + s.c)

      case ds @ DirectedStripe(width, length, curvature, sa, ga) => curvature match {
        case 0.0 => procNatDirectedStripeRect(width, length, sa, ga, pt)
        case curv =>  procNatDirectedStripe(width, length, curv, sa, ga, pt + ds.cO.get)
      }
    }
  }
}
