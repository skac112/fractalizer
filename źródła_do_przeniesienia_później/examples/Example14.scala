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
import skac.fractalizer._
import skac.fractalizer.nodes._

object Example14 {
  def group = {
    val c = (Circle(100.0), Point(100.0, 100.0))
    val part = Seq(.0, .4, .6, 1.0)
    val base_color = Color.red()
    val styler = Selected(Palette.randSimHsl(base_color, 10, 0.4, .2, .2))
    val repl1 = PolarRad(part, Some(styler))
    val repl2 = PolarAng(part, Some(styler))
    // val repl = (repl1 || DrawCut() || repl1) * 2 + DrawCut()
    val repl = ((repl1 || DrawCut() || repl1) + (repl2 || DrawCut() || repl2)) * 3 + DrawCut()
    val out = repl((Seq(c), Nil))._2
    Group(out)
  }
}
