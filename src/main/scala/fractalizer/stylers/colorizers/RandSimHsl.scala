package com.github.skac112.fractalizer.stylers.colorizers

import com.github.skac112.fractalizer.Node._
import com.github.skac112.miro.attribs.colors._
import com.github.skac112.miro.Graphic._
import com.github.skac112.miro._
import com.github.skac112.miro.attribs.colors.MiroColor._
import com.github.skac112.fractalizer._
import com.github.skac112.vgutils._
import com.github.skac112.fractalizer.Palette._

import scala.math._


/**
  * Styler, ktory grafikom wynikowym nadaje kolory losowo rozniace sie od
  * podanego koloru bazowego.
  */
case class RandSimHsl(base: Color, num: Int, hDisp: Angle, sDisp: Double, lDisp: Double) extends Styler {
  override def apply(in: Ensemble, out: Ensemble) = {
    val colors = Palette.randSimHsl(base, num, hDisp, sDisp, lDisp)
    val loop_len = max(out.size, colors.size)
    val col_repeat_cnt = (loop_len.toDouble / colors.size).ceil.toInt
    (for (i <- 1 to col_repeat_cnt; color <- colors) yield GenericAttribs(Some(toMiroColor(color)))) take loop_len
  }
}
