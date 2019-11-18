package com.github.skac112.fractalizer.nodes.extenders

import com.github.skac112.vgutils.{Angle, LineSec, MathUtils, Point}
import com.github.skac112.fractalizer.{Partition, Styler}
import com.github.skac112.miro.{Ensemble, GenericSegCurve, Graphic}
import com.github.skac112.miro.Graphic.GenericAttribs
import com.github.skac112.miro.graphics.Rect
import com.github.skac112.miro.graphics.compounds.{ArcSection, DirectedStripe, Stripe}
import com.github.skac112.miro.segments.LineSeg
import com.github.skac112.fractalizer.Partition

import scala.collection.Set
import scala.math._

object Broom {

  /**
    * @param normPos normalized coordinate of middle of a twig on a base line
    * @param normWidth width of twig normalized for unit base line length (actual width is equal to
    *                   widthCoeff * <base_line_length>)
    * @param normCurv reciprocal of radius coefficient of arc going through middle of twig (radius is equal
    * to radius coefficient multiplied by base length).
    * @param normLen Length of a middle of twig (its' axis line) normalized for baseline of length 1. In case of straight
    * twig it is a normalized height of twig rectangle
    */
  case class Twig(normPos: Double, normWidth: Double, normCurv: Double, normLen: Double) {

    /**
      * Option of signed normalized radius of middle of a twig. It is nomralized for unit baseline length.
      */
    lazy val radiusCoeffO: Option[Double] = normCurv match {
      case 0.0 => None
      case nc => Some(1.0 / nc)
    }

    def width(baseLen: Double) = normWidth * baseLen
    def length(baseLen: Double) = normLen * baseLen
    def pos(baseLen: Double) = normPos * baseLen
    def left(baseLen: Double) = (normPos - .5*normWidth) * baseLen
    def right(baseLen: Double) = (normPos + .5*normWidth) * baseLen

    /**
      * Option of unsigned radius.
      * @param baseLen
      * @return
      */
    def radiusO(baseLen: Double) = radiusCoeffO.map(r => abs(r * baseLen))

    def curvature(baseLen: Double) = normCurv / baseLen
  }

  /**
    * Creates "regularly" looking broom for given parameters. It creates twigs sequence with twig curvature changing
    * with accordance to corresponding displacements between given start and end curvature (given curvatures correspond
    * to 'left' and 'right' arcs of first and last twig stripes, so they are changed accordingly to get curvature of
    * corresponding twigs - i. e. curvatures of 'middle' arcs of stripes.
    * @param startCurvature curvature of a 'left' arc of first twig stripe.
    * @param endCurvature curvature of a 'right' arc of last twig stripe.
    * @param arcLen
    */
  def regular(displacements: Partition,
              startNormCurv: Double,
              endNormCurv: Double,
              boundingCircleNormRadius: Double,
              boundingCircleNormCx: Double,
              boundingCircleNormCy: Double,
              side: Int,
              stylerO: Option[Styler] = None): Broom = {
    // creating twigs
    val twigs = regularTwigs(displacements, startNormCurv, endNormCurv, boundingCircleNormRadius,
      boundingCircleNormCx, boundingCircleNormCy)
    Broom(twigs, side, stylerO)
  }

  /**
    * Remark: all geometric values (curvatures, radii etc.) normalized for baseline of unique length.
    * @param displacements
    * @param startNormCurv
    * @param endNormCurv
    * @param boundCircleNormRad
    * @param boundCircleNormCx
    * @param boundCircleNormCy
    * @return
    */
  def regularTwigs(displacements: Partition,
                   startNormCurv: Double,
                   endNormCurv: Double,
                   boundCircleNormRad: Double,
                   boundCircleNormCx: Double,
                   boundCircleNormCy: Double): Seq[Twig] = {
    // first twig curvature coefficient (for arc passing through middle of twig width)
    val tc_start = addWidth(startNormCurv, 0.5*displacements.increases.head / displacements.totalIncrease)
    // last twig curvature coefficient (for arc passing through middle of twig width)
    val tc_end = subWidth(endNormCurv, 0.5*displacements.increases.last / displacements.totalIncrease)
    val tc_range = tc_end - tc_start
    val mids: Seq[Double] = displacements.rangeSpans(0.0, 1.0).map(span => .5*(span._1  + span._2))
    val mids_range = mids.last - mids.head
    // "stretching" mids to range <tc_start; tc_end>
    val c_coeffs: Seq[Double] = mids map {mid => tc_start + (mid - mids.head) / mids_range * tc_range}

    (displacements.rangeSpans(0.0, 1.0) zip c_coeffs) map {case (span, norm_curv) => {
      // displacement of mid of twig from baseline first end (normalized for baseline of unit length)
      val disp = .5*(span._1 + span._2)
//      val width_coeff = (span._2 - span._1)
      val twig_len = lenForRegularTwig(boundCircleNormRad, boundCircleNormCx, boundCircleNormCy, disp, norm_curv)
      Twig(disp, span._2 - span._1, norm_curv, twig_len)

//      c_coeff match {
//        // straight twig
//        case 0.0 => Twig(width_coeff, 0.0, heightForStraightTwig(boundCircleNormRad, boundCircleNormCx,
//          boundCircleNormCy, disp))
//        case _ => {
//          // radius is signed
//          val radius = 1.0 / c_coeff
//          val ar = arcRangeForTwig(boundCircleNormRad, boundCircleNormCx, boundCircleNormCy,
//            disp + radius, radius)
//          Twig(width_coeff, c_coeff, ar)
//        }
//      }
    }}
  }

  /**
    * Calculates normalized length of a twig for regular twigs.
    * @param boundCircleNormRad
    * @param boundCircleNormCx
    * @param boundCircleNormCy
    * @param displacement
    * @param normCurv
    * @return
    */
  private def lenForRegularTwig(boundCircleNormRad: Double,
                                boundCircleNormCx: Double,
                                boundCircleNormCy: Double,
                                displacement: Double,
                                normCurv: Double): Double = normCurv match {
    case 0.0 => heightForStraightTwig(boundCircleNormRad, boundCircleNormCx, boundCircleNormCy, displacement)
    case nc => {
      // signed normalized radius of a middle of a twig
      val radius = 1 / normCurv
      // angle range
      val ar = arcRangeForTwig(boundCircleNormRad, boundCircleNormCx, boundCircleNormCy, displacement + radius, radius)
      ar * abs(radius)
    }
  }

  /**
    * Remark: radius is signed value normalized for for baseline of unique length.
    * @param boundingCircleRadiusCoeff
    * @param boundingCircleCxCoeff
    * @param boundingCircleCyCoeff
    * @param centerDisp
    * @param radius
    * @return
    */
  private def arcRangeForTwig(boundingCircleRadiusCoeff: Double,
                              boundingCircleCxCoeff: Double,
                              boundingCircleCyCoeff: Double,
                              centerDisp: Double,
                              radius: Double): Double = {
    // center of bounding circle
    val bc = Point(0.5 + boundingCircleCxCoeff, boundingCircleCyCoeff)
    // center of twig arc circle
    val tc = Point(centerDisp, 0.0)
    val intersects: Set[Point] = MathUtils.c2i(bc, boundingCircleRadiusCoeff, tc, abs(radius))
    // point of intersection (or max. proximity) of twig arc circle width bounding circle
    val b_pt: Point = selectBoundPt(intersects, tc, bc, abs(radius), boundingCircleRadiusCoeff)
    val bpt_angle = (b_pt - tc).angle.value

    // non-normalized arc range
    val ar_nn = if (radius > 0) {
        // range is counted from twig "base" point (touching baseline) to point of intersection (or max. proximity)
        // with bounding circle
        Pi + bpt_angle
    }
    else
    {
      // range is counted from point of intersection (or max. proximity) with bounding circle to the twig "base" point
      // (touching baseline)
      2.0*Pi - bpt_angle
    }

    // normalizing
    if (ar_nn > 2.0*Pi) ar_nn - 2*Pi else ar_nn
  }

  private def heightForStraightTwig(boundingCircleRadiusCoeff: Double,
                                    boundingCircleCxCoeff: Double,
                                    boundingCircleCyCoeff: Double,
                                    twigDisp: Double): Double = {
    // Calculates height as a smaller of y coordinate of point of bounding circle for x equal to twigDisp
    val x_diff = (twigDisp - boundingCircleCxCoeff - 0.5)
    val s = boundingCircleRadiusCoeff*boundingCircleRadiusCoeff - x_diff*x_diff
    if (s >= 0) {
      abs(boundingCircleCyCoeff - sqrt(s))
    }
    else {
      0.0
    }
  }

  private def selectBoundPt(intersects: Set[Point],
                            tCenter: Point,
                            bcCenter: Point,
                            tRadius: Double,
                            bcRadius: Double): Point = {
    intersects.size match {
      case 0 => findNearestPt(tCenter, tRadius, bcCenter)
      case 1 => intersects.head
      case _ => selectBountPtFromTwo(intersects.toSeq(0), intersects.toSeq(1), tCenter, bcCenter, tRadius, bcRadius)
    }
  }

  // Finds point of circle (tCenter, tRadius) nearest to outer circle with center bcCenter
  private def findNearestPt(tCenter: Point, tRadius: Double, bcCenter: Point): Point =
    tCenter + new Point(tRadius, (tCenter - bcCenter).angle.opposite)

  private def selectBountPtFromTwo(iPt1: Point,
                                   iPt2: Point,
                                   tCenter: Point,
                                   bcCenter: Point,
                                   tRadius: Double,
                                   bcRadius: Double): Point = {
    // point from which twig grows ("stem" point)
    val stem_pt = tCenter - Point(tRadius, 0.0)

    val slope1 = (iPt1 - tCenter).angle
    val slope2 = (iPt2 - tCenter).angle
    if (ptInsideCircle(stem_pt, bcCenter, bcRadius)) {
        if (tRadius > 0.0) {
          if (slope1.between(Angle(Pi), slope2)) {
            iPt1
          }
          else {
            iPt2
          }
        }
        else {
          if (slope1.between(slope2, Angle(0.0))) {
            iPt1
          }
          else {
            iPt2
          }
        }
    }
    else {
      // stem point outside bounding circle
      if (tRadius > 0.0) {
        if (slope1.between(Angle(Pi), slope2)) {
          iPt2
        }
        else {
          iPt1
        }
      }
      else {
        if (slope1.between(slope2, Angle(0.0))) {
          iPt2
        }
        else {
          iPt1
        }
      }
    }
  }

  def ptInsideCircle(pt: Point, c: Point, radius: Double): Boolean = (pt - c).modulus2 < radius*radius

  def addWidth(curvature: Double, width: Double) = 1.0 / (1.0 / curvature - width)

  def subWidth(curvature: Double, width: Double) = 1.0 / (1.0 / curvature + width)
}

import Broom._

/**
  * Creates a broom - set of stripe twigs which "grows" from input twig which can be Rect, Stripe or ArcSection.
  * It overrides basic extender, so it draws input twig and generates output stripes on the proc channel.
  * @param displacements
  * @param twigs
  * @param side determines which side of input graphics the twigs grow from. For each input graphics type, only straight
  * segments are taken into consideration - so for rect valid values are in range 0..3, for stripe - 0 and 1 and so on.
  * @param stylerO
  */
case class Broom(twigs: Seq[Twig],
                 side: Int,
                 override val stylerO: Option[Styler] = None) extends BasicExtender {

  override def procFun(g: Graphic, pt: Point) = {
    val base_line = g match {
      case r: Rect => baseLineFromSegCurve(r, pt)
      case s: Stripe => baseLineFromSegCurve(s.subpaths(0)._1, s.subpaths(0)._2 + pt)
      case ds: DirectedStripe => baseLineFromSegCurve(ds.subpaths(0)._1, ds.subpaths(0)._2 + pt)
      case a: ArcSection => baseLineFromSegCurve(a.subpaths(0)._1, a.subpaths(0)._2 + pt)
    }
    generate(base_line, twigs, g.genericAttribs)
  }

  private def baseLineFromSegCurve(g: GenericSegCurve, pt: Point): LineSec = {
    // number of straight line segments in path
    val line_segs_num = g.segments.count(_ match {
      case ls: LineSeg => true
      case _ => false
    })

    // side index modulo number of line segments
    val side_mod = side % line_segs_num

    // line segment counting collection
    val ls_counts: Seq[Int] = g.segments.scanLeft(-1){(ls_num, seg) => { seg match {
      case ls: LineSeg => ls_num + 1
      case _ => ls_num
    }}}

    // finding index of appropriate straight line segment within segments sequence
    val ls_idx = ls_counts.indexWhere(_ == side_mod) - 1
    // suming up endpoints of segments to calculate appropriate endpoints of result line section
    // second endpoint - it corresponds to endpoint of appropriate segment
    val p2 = g.segments.view.take(ls_idx + 1).foldLeft(pt){(curr_pt, seg) => curr_pt + seg.end}
    // first endpoint calculated by subtracting end point of appropriate segment from second endpoint
    val p1 = p2 - g.segments(ls_idx).end
    LineSec(p1, p2)
  }

  /**
    *
    * @param startPt start point of a base line
    * @param endPt end point of a base line
    * @param displacements
    * @param angleRanges
    * @param genericAttribs
    * @return
    */
  private def generate(baseLine: LineSec,
                        twigs: Seq[Twig],
                        genericAttribs: GenericAttribs): Ensemble = {
    val base_len = baseLine.len

    twigs map { twig =>
      val pt = baseLine.p1 + new Point(twig.right(base_len), baseLine.slope)

      val stripe = DirectedStripe(twig.width(base_len), twig.length(base_len), twig.curvature(base_len),
        baseLine.slope.opposite, genericAttribs)

      (stripe, pt)
    }


//    (displacements.rangeValues(0, base_len).dropRight(1) zip twigs) map {
//      case (disp, twig) => {
//        val pt = baseLine.p1 + new Point(disp + twig.width(base_len), baseLine.slope)
//
//        val stripe = DirectedStripe(twig.width(base_len), twig.length(base_len), twig.curvature(base_len),
//          baseLine.slope.opposite, genericAttribs)
//
//        (stripe, pt)
//      }
//    }
  }

  /**
    * Creates new broom with twigs width changed by specified factor.
    * @param factor
    */
  def changeWidth(factor: Double): Broom = {
    val new_twigs = twigs map { case Twig(normPos, normWidth, normCurv, normLen) => {
      Twig(changeTwigNormPos(normPos, normWidth, factor), Math.min(normWidth * factor, 1.0), normCurv, normLen)
    }}
    this.copy(twigs = new_twigs)
  }

  private def changeTwigNormPos(normPos: Double, normWidth: Double, factor: Double): Double = {
    // side choice
    normPos match {
      case 0.5 => normPos
      case pos @ _ if pos < .5 => {
        val force = .5 - normPos + .5 * normWidth
        Math.max(normPos + force * normWidth * (factor - 1.0), 0)
      }
      case _ => {
        val force = normPos + .5 * normWidth - .5
        Math.min(normPos - force * normWidth * (factor - 1.0), 1)
      }
    }
  }
}