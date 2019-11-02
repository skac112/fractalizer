package skac.fractalizer.examples

import skac.miro.graphics._
import skac.miro.Graphic._
import skac.miro.draw.svg._
import skac.miro.attribs._
import skac.miro.attribs.colors._
import skac.miro._
import skac.miro.graphics.compounds._
import fractalizer.nodes.partitioners._
import skac.fractalizer.stylers.colorizers._
import skac.fractalizer.Node._
import skac.fractalizer._
import fractalizer.nodes.filters._
import fractalizer.nodes._

object Example28 {
  def group = {
    val c = (Circle(100.0), ori)

    val pal = Seq(
      new Color("2274a5"),
      new Color("f75c03"),
      new Color("f1c40f"),
      new Color("d90368"),
      new Color("00cc66"))

    val styler = RandomSelected(pal, 0.1, 0.2, 0.2)
    val cols1 = Seq(Color(0, 0, 0), Color.blue(.05))
    val thinner = SingleProc(Stripe.scaleWidth(_, .5))
    val repl1 = PolarRad(Seq(0, 0.7, 1), Some(Selected(cols1))) + DrawPass()
    val repl2 = PolarRad.uniform(3) + PolarAng.uniform(12) + RandomPass(0.5, false) + thinner + Style(styler) + DrawCut()
    val repl = repl1 + (DrawCut() || repl2)
    val out = repl((Seq(c), Nil))._2
    Group(out)
  }
}
