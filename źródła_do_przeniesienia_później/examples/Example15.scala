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
import skac.fractalizer._
import fractalizer.nodes._

object Example15 {
  def group = {
    val c = (Circle(100.0), Point(100.0, 100.0))
    val part1 = Partition.random(10, -10.0)
    val part2 = Partition.random(20, -10.0)
    val base_color = Color(.5, .2, .1)
    val styler = Selected(Palette.randSimHsl(base_color, 10, .1, .1, .1))
    val repl1 = PolarRad.uniform(10, Some(styler))
    val repl2 = PolarAng(part2, Some(styler))
    // val repl = (repl1 || DrawCut() || repl1) * 2 + DrawCut()
    val repl = repl1 + (repl2 || (DrawCut()||*3) || repl2) + DrawCut()
    val out = repl((Seq(c), Nil))._2
    Group(out)
  }
}
