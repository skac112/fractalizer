package com.github.skac112.fractalizer.nodes.partitioners

import com.github.skac112.fractalizer.Node.makeSingleInNodeFun
import com.github.skac112.fractalizer.{Node, SimpleNode, Styler, WrappingNode}
import com.github.skac112.miro._
import com.github.skac112.miro.graphics._
import com.github.skac112.vgutils._
import com.github.skac112.fractalizer.WrappingNode

/**
  * Base class for large part of partitioners. It has 'proc only' node fun - draws nothing. Proc function
  * processes only single input graphic (has arity 1). It has optional styler.
  */
abstract class BasicPartitioner extends WrappingNode {
  override lazy val internalNode = SimpleNode(makeSingleInNodeFun(procFun), 1)

  def procFun(g: Graphic, pt: Point): Ensemble
}
