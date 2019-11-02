package skac.fractalizer.examples

import skac.miro.graphics._
import skac.miro.Graphic._
import skac.miro.draw.svg._
import skac.miro.attribs._
import skac.miro.attribs.colors._
import skac.miro.attribs.colors.Color._
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
import java.io.File

object Example21 {
  val endCircle = new Node(1) {
    override def procNat(input: PosGraphics): PosGraphics = {
      val (g, pt) = input(0)
      val stripe = g.asInstanceOf[Stripe]
      val rad_coeff = 1.0
      Seq((Circle(stripe.width * rad_coeff, stripe.genericAttribs), stripe.p4 + (stripe.p3 - stripe.p4) * rad_coeff + pt))
    }

    override def draw(data: NodeData): PosGraphics = data._2 ++ data._1
  }

  val base_color = Color.green(.6) +: Color.blue(.2)
  val c = (Circle(100.0, GenericAttribs(Some(base_color))), Point(100.0, 100.0))
  // val r = (Ring(100, 120), Point(100, 50))

  val styler = ConstantDisperse(5, 3.0, .4, .1)
  val repl2 = PolarAng(Partition.uniform(10), Some(styler), false)
  val repl3 = PolarAng(Partition.uniform(8), Some(styler), false)
  val repl4 = StripeContAlt(.15, .8*Pi, true) + StripeContAlt(1.2, .7*Pi, false)
  // val repl = repl1 + (repl2 || (repl3 + repl4 + endCircle)) + DrawCut()

  def group(splitCoeff: Double) = {
    // repl = repl1 + ((repl2 + DrawCut()) || (repl3 + repl4 + endCircle))
    val repl1 = PolarRad(Seq(.0, splitCoeff, 1.0))
    val repl = (repl1 + ((repl2 + DrawCut()) || (repl3 + repl4 + endCircle))) * 3 + DrawCut()
    val out = repl((Seq(c), Nil))._2
    Group(out)
  }

  def makePics() {
    val dir = new File("example21");
    dir.mkdir()
    val start_coeff = 0.95
    val end_coeff = 0.6
    val step_num = 30
    val step = (end_coeff - start_coeff) / (step_num - 1)
    val draw = new Draw()
    (0 until step_num) foreach {i =>
      val coeff = start_coeff + i * step
      val g = group(coeff)
      draw.saveToFile(g, s"example21/step$i.svg")
    }
  }
}
