package skac.fractalizer.stylers.colorizers

import skac.fractalizer.Node._
import skac.miro.attribs.colors._
import skac.miro.Graphic._
import skac.miro._
import scala.math._

object RandomSelected {
  lazy val rand = scala.util.Random
}

/**
 * Styler, which randomly applies colors from given sequence to out graphics.
 */
case class RandomSelected(
 colors: Seq[Color],
 hDisp: Angle = Angle(0),
 sDisp: Double = 0,
 lDisp: Double = 0) extends Styler {
  import Selected._

  def apply(in: PosGraphics, out: PosGraphics) = {
    (1 to out.size) map {i =>
      val col = colors(rand.nextInt(colors.size))
      val h_delta = hDisp * rand.nextGaussian
      val s_delta = sDisp * rand.nextGaussian
      val l_delta = lDisp * rand.nextGaussian
      val res_col = col.addH(h_delta).addS(s_delta).addL(l_delta)
      GenericAttribs(Some(res_col))
    }
  }
}
