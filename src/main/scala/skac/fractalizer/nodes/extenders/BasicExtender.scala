package skac.fractalizer.nodes.extenders

import com.github.skac112.vgutils._
import skac.fractalizer.Node._
import skac.fractalizer.{SimpleNode, WrappingNode}
import skac.miro._

/**
  * Base class for large part of extenders. It draws only input and has procFun to override with only single input
  * graphic (has arity 1). It has optional styler.
  */
abstract class BasicExtender extends WrappingNode {
  override lazy val internalNode = SimpleNode(makeNodeFun(drawFun, makeSingleInProcFun(procFun)), 1)

  def procFun(g: Graphic, pt: Point): Ensemble

  override def drawFun(ens: Ensemble) = ens
}
