package com.github.skac112.fractalizer.nodes.special

import cats.data.Writer
import com.github.skac112.fractalizer._
import com.github.skac112.miro.Ensemble
import com.github.skac112.fractalizer.SimpleNode

/**
  * Node which draws contents of a proc channel doesn't change proc channel (passes it through).
  */
case class DrawPass(override val arity: Int = 0) extends SimpleNode {
  override def apply(ens: Ensemble) = Writer(ens, ens)
}
