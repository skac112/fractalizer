package skac.fractalizer.stylers.colorizers

import skac.fractalizer.Node._
import skac.miro.attribs.colors._
import skac.miro.Graphic._
import scala.math._
import skac.miro._
import skac.fractalizer._

/**
 * Styler, ktory grafikom wynikowym nadaje kolory losowo rozniace sie od
 * podanego koloru bazowego.
 */
case class RandSimHsl(base: Color, num: Int, hDisp: Angle, sDisp: Double, lDisp: Double) extends Styler {
  def apply(in: PosGraphics, out: PosGraphics) = {
    val colors = Palette.randSimHsl(base, num, hDisp, sDisp, lDisp)
    val loop_len = max(out.size, colors.size)
    val col_repeat_cnt = (loop_len.toDouble / colors.size).ceil.toInt
    (for (i <- 1 to col_repeat_cnt; color <- colors) yield GenericAttribs(Some(color))) take loop_len
  }
}
