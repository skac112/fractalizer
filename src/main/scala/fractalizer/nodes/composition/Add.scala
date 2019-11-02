package com.github.skac112.fractalizer.nodes.composition
import com.github.skac112.fractalizer._
import com.github.skac112.funnodes.FunNode._
import com.github.skac112.fractalizer.Node

/**
  * Sequential compostion of nodes. n1 is applied first (is internal). This convention is opposite to used
  * in classical notation of function composition in mathematics.
  * @param n1
  * @param n2
  */
case class Add(n1: Node, n2: Node) extends Node {
  override def n = n2.chain(n1.n)
  override def internalArity = n1.internalArity
  override def arity = n1.arity
}