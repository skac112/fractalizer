package skac.fractalizer.stylers.colorizers

import skac.fractalizer.Node._
import skac.miro.attribs.colors._
import skac.miro.Graphic._
import scala.math._

/**
 * Styler, ktory grafikom wynikowym nadaje kolory z podanej sekwencji.
 */
case class Selected(colors: Seq[Color]) extends Styler {
  def apply(in: PosGraphics, out: PosGraphics) = {
    val loop_len = max(out.size, colors.size)
    val col_repeat_cnt = (loop_len.toDouble / colors.size).ceil.toInt
    (for (i <- 1 to col_repeat_cnt; color <- colors) yield GenericAttribs(Some(color))) take loop_len
  }
}
