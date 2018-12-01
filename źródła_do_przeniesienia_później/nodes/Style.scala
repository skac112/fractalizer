package skac.fractalizer.nodes

import skac.fractalizer.Node._
import skac.fractalizer._

/**
 * Node which passes both channels and only uses styler to proc channel.
 */
case class Style(styler: Styler) extends Node(1) {
  override val stylerO = Some(styler)
  override def procNat(graphics: PosGraphics): PosGraphics = graphics
  override def draw(data: NodeData) = data._2
}
