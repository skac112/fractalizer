package com.github.skac112.fractalizer.nodes.composition
import com.github.skac112.funnodes.Split._
import com.github.skac112.miro._
import com.github.skac112.fractalizer._
import com.github.skac112.fractalizer.Node

case class Split(n1: Node, n2: Node) extends Node {
  override def n = QuickSplit(n1.n, n2.n, n1.splitFun _)
  override val internalArity = n1.internalArity + n2.internalArity
}
