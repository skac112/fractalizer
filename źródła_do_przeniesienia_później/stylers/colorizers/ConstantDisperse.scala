package skac.fractalizer.stylers.colorizers

import skac.fractalizer.Node._
import skac.miro.attribs.colors._
import skac.miro.Graphic._
import scala.math._
import skac.miro._
import skac.fractalizer._
import scala.util._

/**
 * Styler, ktory grafikom wynikowym nadaje kolory rozniace sie losowo od
 * koloru grafiki wejsciowej. W przeciwienstwie do Disperse, losowanie
 * przesuniec h, s i l w stosunku do koloru bazowego odbywa sie tylko raz.
 */
 case class ConstantDisperse(num: Int, hDisp: Angle, sDisp: Double, lDisp: Double) extends Styler {
   val rand = scala.util.Random
   val dh = (1 to num) map {i => hDisp * rand.nextGaussian}
   val ds = (1 to num) map {i => sDisp * rand.nextGaussian}
   val dl = (1 to num) map {i => lDisp * rand.nextGaussian}

   def apply(in: PosGraphics, out: PosGraphics) = {
     if (!in.isEmpty) {
       val base_color = in(0).genericAttribs.fillO.get.asInstanceOf[Color]
       val colors = (0 until num) map {i => (dh(i), ds(i), dl(i))} map {triple => base_color.addH(triple._1).addS(triple._2).addL(triple._3)}
       val loop_len = max(out.size, colors.size)
       val col_repeat_cnt = (loop_len.toDouble / colors.size).ceil.toInt
       (for (i <- 1 to col_repeat_cnt; color <- colors) yield GenericAttribs(Some(color))) take loop_len
     }
     else {
       Nil
     }
   }
 }
