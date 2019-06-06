package skac.fractalizer

import skac.miro.attribs.colors._
import scala.util._
import skac.miro._
import scala.math._
import com.github.skac112.vgutils._
import com.github.skac112.vgutils.Color._
import skac.miro.attribs.colors._
import skac.miro.attribs.colors.MiroColor._

/**
 * Obiekt do tworzenia palet kolorów. Kolory mogą byc randomizowane.
 */
object Palette {
  lazy val rand = scala.util.Random

  def normalize(v: Double) = v match {
    case v if v > 1.0 => 1.0
    case v if v < 0.0 => 0.0
    case v => v
  }

  /**
   * Generuje randomizowana palete w oparciu o dyspersje w osiach h, s i l.
   */
  def randSimHsl(base: Color, num: Int, hDisp: Angle, sDisp: Double, lDisp: Double) =
    (1 to num) map {_ =>
      val h_delta = hDisp * rand.nextGaussian
      val s_delta = sDisp * rand.nextGaussian
      val l_delta = lDisp * rand.nextGaussian
      toMiroColor(base.addH(h_delta).addS(s_delta).addL(l_delta))
    }

  /**
   * Generuje palete kolorow rownomiernie rozlozonych na "okregu" hue.
   */
  def hueSpan(startColor: Color, num: Int) = {
    val start_h = startColor.h
    val step = Angle(2.0 * Pi / num)
    for (i <- 1 to num) yield toMiroColor(Color.hsla(start_h + (step * (i - 1)), startColor.s, startColor.l))
  }
}
