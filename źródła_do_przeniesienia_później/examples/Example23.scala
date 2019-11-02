package skac.fractalizer.examples

import skac.miro.graphics._
import skac.miro.Graphic._
import skac.miro.draw.svg._
import skac.miro.attribs._
import skac.miro.attribs.colors._
import skac.miro.attribs.colors.Color._
import skac.miro._
import fractalizer.nodes.tangent._
import skac.fractalizer.stylers.colorizers._
import skac.fractalizer.Node._
import skac.fractalizer.Palette._
import fractalizer.nodes._
import skac.fractalizer._
import fractalizer.nodes.filters._
import fractalizer.nodes.stripes._
import skac.miro.graphics.compounds._
import scala.math._
import skac.miro.Dsl._

object Example23 {
  def group = {
    val c0 = C(100) fill Color(.8, .8, .8) toC
    val base_color = Color(.2, .6, .9)
    val styler = Selected(Palette.randSimHsl(base_color, 20, 0.1, .3, .7))
    val node = AppGasket(.5, .4, .0, 500, Some(styler))
    val out = node((Seq((c0, Point(.0, .0))), Nil))._1
    Group(out)
  }
}
