package skac.fractalizer.examples

import skac.miro.graphics._
import skac.miro.graphics.compounds._
import scala.math._
import skac.miro.Graphic._
import skac.miro.draw.svg._
import skac.miro.attribs._
import skac.miro.attribs.colors._
import skac.miro._
import skac.fractalizer.partitioners._
import skac.fractalizer.stylers.colorizers._
import skac.fractalizer.Node._
import skac.fractalizer._

object Example7 {
  def group = {
    val ring = (Ring(100.0, 150.0), Point(.0, .0))
    val part = Partition.uniform(2)
    val styler = Selected(Seq(Color.red(), Color.green(), Color.blue()))
    val repl = PolarAng(part, Some(styler))
    val out = repl((Seq(ring), Nil))._1
    Group(out)
  }
}
