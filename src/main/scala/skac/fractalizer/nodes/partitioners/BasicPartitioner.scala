package skac.fractalizer.nodes.partitioners

import skac.fractalizer.Node.makeSingleInNodeFun
import skac.fractalizer.{Node, SimpleNode, Styler, WrappingNode}
import skac.miro._
import skac.miro.graphics._
import com.github.skac112.vgutils._

/**
  * Base class for large part of partitioners. It has 'proc only' node fun - draws nothing. Proc function
  * processes only single input graphic (has arity 1). It has optional styler.
  */
abstract class BasicPartitioner extends WrappingNode {
  override lazy val internalNode = SimpleNode(makeSingleInNodeFun(procFun), 1)

  def procFun(g: Graphic, pt: Point): Ensemble
}
