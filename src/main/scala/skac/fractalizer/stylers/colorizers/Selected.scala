package skac.fractalizer.stylers.colorizers

import skac.fractalizer._
import skac.fractalizer.Node._
import skac.miro.attribs.colors._
import skac.miro.Graphic._
import skac.miro._
import skac.miro.attribs.colors.MiroColor._
import skac.miro.attribs.colors._
import scala.math._
import com.github.skac112.vgutils._

object Selected {
  lazy val rand = scala.util.Random
}

/**
 * Styler which applies colors from given sequence to out graphics. Colors
 * can be randomly modified in each application by giving nonzero values to
 * arguments hDisp, sDisp and lDisp.
 */
case class Selected(
 colors: Seq[MiroColor],
 hDisp: Angle = Angle(0),
 sDisp: Double = 0,
 lDisp: Double = 0) extends Styler {
  import Selected._

  def apply(in: Ensemble, out: Ensemble) = {
    val loop_len = max(out.size, colors.size)
    val col_repeat_cnt = (loop_len.toDouble / colors.size).ceil.toInt
    val mod_colors = (hDisp, sDisp, lDisp) match {
      case (Angle(.0, .0, .0), 0, 0) => colors
      case (h_d, s_d, l_d) => {
        val h_delta = h_d * rand.nextGaussian
        val s_delta = s_d * rand.nextGaussian
        val l_delta = l_d * rand.nextGaussian
        colors map {_.addH(h_delta).addS(s_delta).addL(l_delta)}
      }
    }
    (for (i <- 1 to col_repeat_cnt; color <- mod_colors) yield GenericAttribs(Some(color))) take loop_len
  }
}
