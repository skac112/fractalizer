package skac.fractalizer.examples

import skac.miro.graphics._
import skac.miro.Graphic._
import skac.miro.draw.svg._
import skac.miro.attribs._
import skac.miro.attribs.colors._
import skac.miro._
import fractalizer.nodes._
import fractalizer.nodes.partitioners._
import skac.fractalizer.stylers.colorizers._
import skac.fractalizer.Node._
import skac.fractalizer._

object Example1 {
  def group = {
    val c = (Circle(100.0), Point(100.0, 100.0))
    val base_c = new Color("f600e9")
    val styler = RandSimHsl(base_c, 100, 0, 0.1, 0.1)
    val repl = PolarRad.uniform(10) + PolarAng.uniform(10) + Style(styler) + DrawCut()
    val out = repl((Seq(c), Nil))._2
    Group(out)
  }
}
