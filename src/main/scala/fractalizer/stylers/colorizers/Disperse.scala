package com.github.skac112.fractalizer.stylers.colorizers

import com.github.skac112.fractalizer.Node._
import com.github.skac112.miro.attribs.colors._
import com.github.skac112.miro.Graphic._
import scala.math._
import com.github.skac112.miro._
import com.github.skac112.fractalizer._
import com.github.skac112.vgutils._

/**
  * Styler, ktory grafikom wynikowym nadaje kolory rozniace sie losowo od
  * koloru grafiki wejsciowej.
  */
case class Disperse(num: Int, hDisp: Angle, sDisp: Double, lDisp: Double) extends Styler {
  override def apply(in: Ensemble, out: Ensemble) = {
    if (!in.isEmpty) {
      val base_color = in(0)._1.genericAttribs.fillO.get.asInstanceOf[Color]
      RandSimHsl(base_color, num, hDisp, sDisp, lDisp)(in, out)
    }
    else {
      Nil
    }
  }
}