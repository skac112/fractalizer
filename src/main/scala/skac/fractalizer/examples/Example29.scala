package skac.fractalizer.examples

import skac.miro.graphics._
import skac.miro.Graphic._
import skac.miro.draw.svg._
import skac.miro.attribs._
import skac.miro.attribs.colors._
import skac.miro._
import skac.miro.attribs.colors._
import skac.miro.graphics.compounds._
import skac.fractalizer.Node._
import skac.fractalizer._
import skac.fractalizer.nodes.partitioners.rectangles._
import stylers.colorizers._
import com.github.skac112.vgutils._


object Example29 {
  def group = {
    val rp = (Rect(100, 100).fill(MiroColor.red(0.8)), Point(0, 0))
    val styler1 = Disperse(5, 1.4, 0.3, 0.3)
    val styler2 = Disperse(5, 0.4, 0.05, 0.05)
    val node1 = Bars.uniform(5, 'SIDETYPE_WIDTH, Some(styler1))
    val node2 = Bars.uniform(5,'SIDETYPE_HEIGHT, Some(styler2))
    val node = node1 + node2
    val out = node(Seq(rp)).run._2
    Group(out)
  }
}