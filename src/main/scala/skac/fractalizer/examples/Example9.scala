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
import skac.fractalizer._

/**
 * Pierścienie z losową paletą kolorów.
 */
object Example9 {
  def group = {
    val c = (Circle(100.0), Point(100.0, 100.0))
    val part = Partition.random(20, -10)
    val base_color = Color.blue()
    println(base_color.h)
    val styler = Selected(Palette.randSimHsl(base_color, 10, 0.4, .2, .2))
    val repl = PolarRad(part, Some(styler))
    val out = repl((Seq(c), Nil))._1
    Group(out)
  }
}
