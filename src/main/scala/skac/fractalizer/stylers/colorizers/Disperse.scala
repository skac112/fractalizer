package skac.fractalizer.stylers.colorizers

import skac.fractalizer.Node._
import skac.miro.attribs.colors._
import skac.miro.Graphic._
import scala.math._
import skac.miro._
import skac.fractalizer._

/**
 * Styler, ktory grafikom wynikowym nadaje kolory rozniace sie losowo od
 * koloru grafiki wejsciowej.
 */
 case class Disperse(num: Int, hDisp: Angle, sDisp: Double, lDisp: Double) extends Styler {
   def apply(in: PosGraphics, out: PosGraphics) = {
     if (!in.isEmpty) {
       val base_color = in(0).genericAttribs.fillO.get.asInstanceOf[Color]
       RandSimHsl(base_color, num, hDisp, sDisp, lDisp)(in, out)
     }
     else {
       Nil
     }
   }
 }
