package skac.fractalizer.examples

import skac.miro.graphics._
import skac.miro.Graphic._
import skac.miro.draw.svg._
import skac.miro.attribs._
import skac.miro.attribs.colors._
import skac.miro._
import fractalizer.nodes.partitioners._
import skac.fractalizer.stylers.colorizers._
import skac.fractalizer.Node._
import skac.fractalizer.Palette._
import fractalizer.nodes._
import skac.fractalizer._
import fractalizer.nodes.filters._
import fractalizer.nodes.stripes._
import skac.miro.graphics.compounds._
import scala.math._

object Example20 {
  val endCircle = new Node(1) {
    override def procNat(input: PosGraphics): PosGraphics = {
      val (g, pt) = input(0)
      val stripe = g.asInstanceOf[Stripe]
      val rad_coeff = 1.0
      Seq((Circle(stripe.width * rad_coeff, stripe.genericAttribs), stripe.p4 + (stripe.p3 - stripe.p4) * rad_coeff + pt))
    }

    override def draw(data: NodeData): PosGraphics = data._2 ++ data._1
  }

  def group = {
    val base_color1 = Color(24.0/255.0, 207.0/255.0, 242.0/255.0)
    val base_color2 = Color(2.0/255.0, 22.0/255.0, 41.0/255.0)
    val base_color3 = Color(232.0/255.0, 182.0/255.0, 67.0/255.0)
    val c = (Circle(100.0, GenericAttribs()), Point(100.0, 100.0))
    // val r = (Ring(100, 120), Point(100, 50))
    val styler1 = Selected(Seq(base_color1, base_color2))
    val styler5 = Selected(Seq(base_color3, base_color1))
    val repl1 = PolarRad(Seq(.0, 0.9, 1.0), Some(styler1))
    val styler2 = Disperse(10, .2, .2, .1)
    val styler3 = Disperse(8, .2, .2, .1)
    val repl2 = PolarRad(Partition.random(6, 4.0), Some(styler2))
    val repl3 = PolarAng(Partition.uniform(12), Some(styler3))
    val repl4 = StripeContAlt(.15, .8*Pi, true) + StripeContAlt(1.2, .7*Pi, false)
    val repl5 = PolarRad(Seq(.0, 0.4, 1.0), Some(styler5))
    val repl6 = PolarAng(Partition.uniform(20), Some(styler2))
    // val repl = repl1 + (repl2 || (repl3 + repl4 + endCircle)) + DrawCut()
    val repl = (repl1 + ((repl5 + (repl6 || repl2) + DrawCut()) || (repl3 + repl4 + endCircle))) * 2 + DrawCut()
    // repl = repl1 + ((repl2 + DrawCut()) || (repl3 + repl4 + endCircle))
    val out = repl((Seq(c), Nil))._2
    Group(out)
  }
}
