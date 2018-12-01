package skac.fractalizer.nodes.partitioners.rectangles

import skac.fractalizer._
import skac.miro._
import skac.miro.graphics._
import skac.fractalizer.Node._
import skac.fractalizer.nodes.partitioners.BasicPartitioner

/**
 * Partitions rectangle into smaller rectangles ("homo-partition") along one of
 * the sides of the original rectangle.
 */
case class Bars(numBars: Int,
                sideType: Symbol = 'SIDETYPE_WIDTH,
                override val stylerO: Option[Styler] = None)
  extends BasicPartitioner {

  override def procFun(g: Graphic, pt: Point): Ensemble = {
    val rect: Rect = g.toR
    sideType match {
      case 'SIDETYPE_WIDTH => {
        val new_width = rect.width / numBars
        val vec: Point = new_width rot rect.rotation
        (1 to numBars) map {i =>
          Rect(new_width, rect.height, rect.rotation, rect.genericAttribs) at (pt + vec*(i - 1))
        }
      }
      case 'SIDETYPE_HEIGHT => {
        val new_height = rect.height / numBars
        val vec: Point = Point(0, new_height) rot rect.rotation
        (1 to numBars) map {i =>
          Rect(rect.width, new_height, rect.rotation, rect.genericAttribs) at (pt + vec*(i - 1))
        }
      }
    }
  }
}
