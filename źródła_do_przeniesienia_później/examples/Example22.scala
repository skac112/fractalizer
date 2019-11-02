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
import skac.fractalizer.Palette._
import fractalizer.nodes._
import skac.fractalizer._
import fractalizer.nodes.filters._
import fractalizer.nodes.stripes._
import skac.miro.graphics.compounds._
import scala.math._

object Example22 {
  def group = {
    val colors = Seq(Color(.0, 20.0/255.0, 39.0/255.0),
     Color(112.0/255.0, 141.0/255.0, 129.0/255.0),
     Color(244.0/255.0, 213.0/255.0, 141.0/255.0),
     Color(191.0/255.0, 6.0/255.0, 3.0/255.0),
     Color(141.0/255.0, 8.0/255.0, 1.0/255.0))
    val outer_styler = Selected(colors)
    val inner_c = colors(4)
    val c = (Circle(100.0, GenericAttribs(Some(inner_c))), Point(100.0, 100.0))
    val inner_styler = Disperse(10, .2, .2, .1)
    val repl1 = PolarRad(Seq(.0, 0.5, 1.0))
    val repl2 = PolarAng(Partition.uniform(12), Some(inner_styler))
    val repl3 = PolarAng(Partition.uniform(4), Some(outer_styler))
    val repl4 = PolarRad(Seq(.0, .45, .55, 1.0))
    val repl5 = PolarAng(Partition.uniform(4), Some(outer_styler))
    val repl = repl1 + (repl2  || (repl3 + (repl4 + (DrawCut() || repl5 || DrawCut())))) + DrawCut()
    // repl = repl1 + ((repl2 + DrawCut()) || (repl3 + repl4 + endCircle))
    val out = repl((Seq(c), Nil))._2
    Group(out)
  }
}
