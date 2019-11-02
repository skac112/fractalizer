package com.github.skac112.fractalizer.nodes.partitioners.rectangles

import com.github.skac112.fractalizer._
import com.github.skac112.miro._
import com.github.skac112.miro.graphics._
import com.github.skac112.fractalizer.Node._
import com.github.skac112.fractalizer.nodes.partitioners.BasicPartitioner
import com.github.skac112.vgutils._
import com.github.skac112.fractalizer.Partition

object Bars {
  def uniform(numBars: Int,
              sideType: Symbol = 'SIDETYPE_WIDTH,
              stylerO: Option[Styler] = None) =
    Bars(Partition.uniform(numBars), sideType, stylerO)
}

/**
 * Partitions rectangle into smaller rectangles ("homo-partition") along one of
 * the sides of the original rectangle.
 */
case class Bars(partition: Partition,
                sideType: Symbol = 'SIDETYPE_WIDTH,
                override val stylerO: Option[Styler] = None)
  extends BasicPartitioner {

//  def this(numBars: Int,
//           sideType: Symbol = 'SIDETYPE_WIDTH,
//           stylerO: Option[Styler] = None)
//  {
//    this(Partition(Partition.uniform(numBars - 1)), sideType, stylerO)
//  }

  override def procFun(g: Graphic, pt: Point): Ensemble = {
    val rect: Rect = g.toR
    sideType match {
      case 'SIDETYPE_WIDTH => {
        val slope = Angle(rect.rotation)
        partition.rangeSpans(0, rect.width) map {span =>
          rect.copy(width = span._2 - span._1) at (pt + new Point(span._1, slope))}
      }
      case 'SIDETYPE_HEIGHT => {
        val slope = Angle(rect.rotation) + Angle(.5 * math.Pi)
        partition.rangeSpans(0, rect.height) map { span =>
          rect.copy(height = span._2 - span._1) at (pt + new Point(span._1, slope))}
      }
    }
  }
}
