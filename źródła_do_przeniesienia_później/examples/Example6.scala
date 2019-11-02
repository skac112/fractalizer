package skac.fractalizer.examples

import skac.miro.graphics._
import skac.miro.graphics.compounds._
import scala.math._
import skac.miro.Graphic._
import skac.miro.draw.svg._
import skac.miro.attribs._
import skac.miro.attribs.colors._
import skac.miro._
import fractalizer.nodes.partitioners._
import skac.fractalizer.stylers.colorizers._
import skac.fractalizer.Node._
import skac.fractalizer._

object Example6 {
  def group = {
    val as = (ArcSection(100.0, .0, 0.5 * Pi), Point(.0, .0))
    val part = Partition.uniform(8)
    val styler = Selected(Seq(Color.red(), Color.green(), Color.blue()))
    val repl = PolarAng(part, Some(styler))
    val out = repl((Seq(as), Nil))._1
    Group(out)
  }
}
