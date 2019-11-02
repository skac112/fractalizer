package com.github.skac112.fractalizer.nodes.extenders

import com.github.skac112.vgutils._
import com.github.skac112.fractalizer.Node._
import com.github.skac112.fractalizer.SimpleNode
import com.github.skac112.miro._
import com.github.skac112.fractalizer.WrappingNode

/**
  * Base class for large part of extenders. It draws only input and has procFun to override with only single input
  * graphic (has arity 1). It has optional styler.
  */
abstract class BasicExtender extends WrappingNode {
  override lazy val internalNode = SimpleNode(makeNodeFun(drawFun, makeSingleInProcFun(procFun)), 1)

  def procFun(g: Graphic, pt: Point): Ensemble

  override def drawFun(ens: Ensemble) = ens
}
