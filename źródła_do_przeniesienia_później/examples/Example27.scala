package skac.fractalizer.examples

import skac.miro.graphics._
import skac.miro.Graphic._
import skac.miro.draw.svg._
import skac.miro.attribs._
import skac.miro.attribs.colors._
import skac.miro.attribs.colors.Color._
import skac.miro._
import fractalizer.nodes.stripes._
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

object Example27 {
  def group = {
    val s_p = (Stripe(20, 40, 0, Pi), ori)
    val base_c = Color(.5, 0, 0)
    val styler = Selected(Palette.hueSpan(base_c, 8))
    val node = MultiStripe(7, false, Some(styler)) + DrawCut()
    val out = node((Seq(s_p), Nil))._2
    Group(out)
  }
}
