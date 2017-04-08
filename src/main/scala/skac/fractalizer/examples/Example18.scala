package skac.fractalizer.examples

import skac.miro.graphics._
import skac.miro.Graphic._
import skac.miro.draw.svg._
import skac.miro.attribs._
import skac.miro.attribs.colors._
import skac.miro._
import skac.fractalizer.partitioners._
import skac.fractalizer.stylers.colorizers._
import skac.fractalizer.Node._
import skac.fractalizer.Palette._
import skac.fractalizer.nodes._
import skac.fractalizer._
import skac.fractalizer.nodes.filters._
import skac.fractalizer.nodes.stripes._
import skac.miro.graphics.compounds._
import scala.math._

object Example18 {
  val endCircle = new Node(1) {
    override def procNat(input: PosGraphics): PosGraphics = {
      val (g, pt) = input(0)
      val stripe = g.asInstanceOf[Stripe]
      val rad_coeff = 2.0
      Seq((Circle(stripe.width * rad_coeff, stripe.genericAttribs), stripe.p1 + (stripe.p2 - stripe.p1) * rad_coeff + pt))
    }

    override def draw(data: NodeData): PosGraphics = data._2 ++ data._1
  }

  def group = {
    val c = (Circle(100.0), Point(100.0, 100.0))
    // val r = (Ring(100, 120), Point(100, 50))
    val base_color = Color.red()
    // val styler1 = Selected(Seq(Color.red(0.7), Color.blue(0.6)))
    // val styler = RandSimHsl(base_color, 5, 0.2, .1, .3)
    val repl1 = PolarRad(Seq(.0, 0.95, 1.0))
    val styler2 = RandSimHsl(Color.red(.7), 8, .1, .1, .1)
    val repl2 = PolarAng(Partition.uniform(7), Some(styler2))
    val styler3 = RandSimHsl(Color.red(.7), 6, .1, .1, .1)
    val repl3 = PolarAng(Partition.uniform(15), Some(styler3))
    val repl4 = StripeContAlt(.15, Pi, true)
    // val repl = repl1 + (repl2 || (repl3 + repl4 + endCircle)) + DrawCut()
    val repl = (repl1 + ((repl2 + DrawCut()) || (repl3 + repl4 + endCircle))) * 2 + DrawCut()
    // repl = repl1 + ((repl2 + DrawCut()) || (repl3 + repl4 + endCircle))
    val out = repl((Seq(c), Nil))._2
    Group(out)
  }
}
