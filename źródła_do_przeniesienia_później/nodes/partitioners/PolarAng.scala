package skac.fractalizer.nodes.partitioners

import skac.fractalizer._
import skac.fractalizer.Node._
import skac.miro.graphics._
import skac.miro.graphics.compounds._
import skac.miro.Graphic._
import skac.miro._
import scala.math._

object PolarAng {
  def uniform(parts: Int, stylerO: Option[Styler] = None, randomRot: Boolean = true) =
    PolarAng(Partition.uniform(parts - 1), stylerO, randomRot)
}

/**
 * Dzieli ksztalt "polarny" (Circle, Ring, ArcSection lub Stripe) "kątowo"
 * z użyciem zadanego podzialu. Mozliwe jest zadanie wykonywania losowego obrotu
 * przy kazdym elemencie przetwarzanym, dla ktorego ma to zastosowanie
 */
case class PolarAng(partition: () => Seq[Double],
 override val stylerO: Option[Styler] = None,
 randomRot: Boolean = true) extends Node(1) {

   def this(parts: Int, stylerO: Option[Styler], randomRot: Boolean ) =
     this(Partition.uniform(parts - 1), stylerO, randomRot)

   /**
    * Cyrkularna wersja podzialu partition. Tylko jedna wersja w ramach
    * zycia obiektu, dzieki czemu losowy obrot bedzie ten sam przy kazdym uzyciu.
    */
  def circPart = Partition.circularize(partition(), randomRot)

  def procNatCircle(r: Double, ga: GenericAttribs, pt: Point) = {
    val rand_rot: Double = if (randomRot) random * 2 * Pi else .0
    // kolo dzielone jest na wycinki kola
    circPart.sliding(2) map {low_high =>
      val (a1, a2) = (Angle(low_high(0) + rand_rot), Angle(low_high(1) + rand_rot))
      val arc_sec = ArcSection(r, a1, a2 - a1, ga)
      val as_pt = pt - arc_sec.c
      (arc_sec, as_pt)
    } toSeq;
  }

  def procNatArcSection(r: Double, sa: Angle, ar: Angle, ga: GenericAttribs, pt: Point) = {
    // wycinek kola dzielony jest na wycinki kola
    Partition.angularize(partition(), sa, (sa + ar)).sliding(2) map {low_high =>
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

  def procNatStripe(rl: Double, rh: Double, sa: Angle, ar: Angle, ga: GenericAttribs, pt: Point) = {
    // wycinek pierscienia dzielony jest na wycinki pierscienia
    Partition.angularize(partition(), sa, (sa + ar)).sliding(2) map {low_high =>
      val (a1, a2) = (Angle(low_high(0)), Angle(low_high(1)))
      val stripe = Stripe(rl, rh, a1, a2 - a1, ga)
      val stripe_pt = pt - stripe.c
      (stripe, stripe_pt)
    } toSeq;
  }

  override def procNat(input: PosGraphics): PosGraphics = {
    val (g, pt) = input(0)
    g match {
      case c @ Circle(r, ga) => procNatCircle(r, ga, pt)
      case as @ ArcSection(r, sa, ar, ga) => procNatArcSection(r, sa, ar, ga, pt + as.c)
      case r @ Ring(rl, rh, ga) => procNatRing(rl, rh, ga, pt + r.c)
      case s @ Stripe(rl, rh, sa, ar, ga) => procNatStripe(rl, rh, sa, ar, ga, pt + s.c)
    }
  }
}
