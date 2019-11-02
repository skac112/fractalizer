package skac.fractalizer.examples

import skac.miro.graphics._
import skac.miro.Graphic._
import skac.miro.draw.svg._
import skac.miro.attribs._
import skac.miro.attribs.colors._
import skac.miro.attribs.colors.Color._
import skac.miro._
import fractalizer.nodes.tangent._
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
import skac.miro.Dsl._

object Example24 {
  def group = {
    // losowy kolor bazowy
    val base_c = Color(scala.math.random, .9, .4)
    // paleta kolorow rownomiernie rozlozonych wzgledem barwy (hue)
    val palettes = Palette.hueSpan(base_c, 4) map {c => Palette.randSimHsl(c, 5, 0.2, 0.15, 0.15)}
    // val nodes = colorizers map {cr => PolarRad({() => Partition.random(4, -2.0)}, Some(cr))}
    val node1 = PolarAng(Partition.uniform(5), Some(Selected(palettes(0))))
    val node2 = PolarRad(Partition.uniform(4), Some(Selected(palettes(1))))
    val node3 = PolarAng(Partition.uniform(2), Some(Selected(palettes(2))))
    val node4 = PolarRad(Partition.uniform(3), Some(Selected(palettes(3))))
    // val nodes = palettes map {p => PolarRad({() => Partition.uniform(4)}, Some(Selected(p)))}
    // wejsciowe kolo
    val c0 = C(10000)
    // val base_color = Color(.2, .6, .9)
    // val styler = Selected(Palette.randSimHsl(base_color, 20, 0.1, .3, .7))
    // val node = AppGasket(.5, .4, .0, 300, None) + (Node.splitInput(nodes: _*)) + DrawCut()
    val uni_factor = 2*sqrt(3) - 3
    // val uni_factor = 0.4
    println(uni_factor)
    // val node = AppGasket(uni_factor, uni_factor + .01, .0, 300) + (node1 || node2 || node3 || node4) + DrawCut()
    val styler = Selected(Palette.hueSpan(base_c, 4))
    val node = AppGasket(uni_factor, uni_factor + .01, .0, 300, Some(styler)) + DrawCut()
    val out = node((Seq((c0, Point(.0, .0))), Nil))._2
    Group(out)
  }
}
