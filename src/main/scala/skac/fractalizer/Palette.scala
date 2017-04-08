package skac.fractalizer

import skac.miro.attribs.colors._
import scala.util._
import skac.miro._

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
      base.addH(h_delta).addS(s_delta).addL(l_delta)
    }
}
