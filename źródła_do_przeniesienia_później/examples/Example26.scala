package skac.fractalizer.examples

import skac.miro.graphics._
import skac.miro.Graphic._
import skac.miro.draw.svg._
import skac.miro.attribs._
import skac.miro.attribs.colors._
import skac.miro.attribs.colors.Color._
import skac.miro._
import skac.fractalizer.nodes.tangent._
import skac.fractalizer.nodes.partitioners._
import skac.fractalizer.stylers.colorizers._
import skac.fractalizer.Node._
import skac.fractalizer.Palette._
import skac.fractalizer.nodes._
import skac.fractalizer._
import skac.fractalizer.nodes.filters._
import skac.fractalizer.nodes.stripes._
import skac.miro.graphics.compounds._
import scala.math._
import skac.miro.Dsl._

object Example26 {
  def group = {
    // losowy kolor bazowy
    val base_c = Color(scala.math.random, .9, .4)
    val c0 = C(10000, GenericAttribs(Some(Color(0, 0, 0))))
    val uni_factor = 2*sqrt(3) - 3
    val color1 = Color(.0, .1, .6)
    val color2 = Color(.7, 0, .2)
    val color3 = Color(.5, .5, 0)
    val color4 = Color.blue(.2)
    val styler1 = Selected(Seq(color1, color2, color3, color4), .1, .1, .1)
    val styler2 = ConstantDisperse(6, .05, .3, .3)
    // val node = AppGasket(uni_factor, uni_factor + .01, .0, 4, Some(styler)) + DrawCut()
    val node1 = Eye(0, 0.5, 0.25, Some(styler1))
    val node2 = Pass()||*2 || PolarAng.uniform(6, Some(styler2)) || Pass()
    val node = node1 + node2 + DrawCut()
    val c_p = (c0, Point(.0, .0))
    val out = node((Seq(c_p), Nil))._2
    Group(out)
  }
}
