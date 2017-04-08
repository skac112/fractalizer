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

object Example1 {
  def group = {
    val c = (Circle(100.0), Point(100.0, 100.0))
    val part = Partition.uniform(9)
    val styler = Selected(Seq(Color.red(), Color.green(), Color.blue()))
    val repl = PolarRad(part, Some(styler))
    val out = repl((Seq(c), Nil))._1
    Group(out)
  }
}
