package skac.fractalizer.examples

import skac.miro.graphics._
import skac.miro.Graphic._
import skac.miro.draw.svg._
import skac.miro.attribs._
import skac.miro.attribs.colors._
import skac.miro._
import skac.fractalizer.nodes.partitioners._
import skac.fractalizer.stylers.colorizers._
import skac.fractalizer.Node._
import skac.fractalizer.Palette._
import skac.fractalizer.nodes._
import skac.fractalizer._
import skac.fractalizer.nodes.filters._

object Example17 {
  def group = {
    val c = (Circle(100.0), Point(100.0, 100.0))
    val base_color = Color.red()
    val styler = RandSimHsl(base_color, 5, 0.2, .1, .3)
    val repl1 = PolarAng({() => Partition.random(10, -10.0)}, Some(styler))
    val repl2 = PolarRad(Partition.random(10, -10.0), Some(styler))
    val repl = repl2 + repl1 + DrawCut()
    val out = repl((Seq(c), Nil))._2
    Group(out)
  }
}
