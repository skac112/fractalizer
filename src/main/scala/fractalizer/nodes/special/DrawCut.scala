package com.github.skac112.fractalizer.nodes.special

import cats.data.Writer
import com.github.skac112.fractalizer._
import com.github.skac112.miro._
import com.github.skac112.fractalizer.SimpleNode

/**
  * Node which sends contents of a proc channel to draw and clears proc channel.
  * @param arity
  */
case class DrawCut(override val arity: Int = 0) extends SimpleNode {
  override def apply(ens: Ensemble) = Writer(ens, Nil)
}
