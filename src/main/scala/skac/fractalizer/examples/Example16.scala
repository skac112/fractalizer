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

object Example16 {
  def group = {
    val c = (Circle(100.0), Point(100.0, 100.0))
    val base_color = Color.blue()
    val styler = RandSimHsl(base_color, 50, 0.4, .2, .2)
    val repl1 = PolarAng({() => Partition.random(50, -10.0)}, Some(styler))
    val repl2 = PolarRad(Partition.random(30, -10.0), Some(styler))
    val repl = repl1 + repl2 + DrawCut()
    val out = repl((Seq(c), Nil))._2
    Group(out)
  }
}
