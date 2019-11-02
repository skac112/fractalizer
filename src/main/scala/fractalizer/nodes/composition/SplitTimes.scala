package com.github.skac112.fractalizer.nodes.composition

import com.github.skac112.funnodes.Split.QuickSplit
import com.github.skac112.funnodes._
import com.github.skac112.fractalizer.Node

case class SplitTimes(node: Node, times: Int) extends Node {
  override def n = node.n.splitTimes(node.splitFun _, times)
  override val internalArity = node.internalArity * times
}
