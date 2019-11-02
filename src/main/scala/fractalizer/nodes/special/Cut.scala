package com.github.skac112.fractalizer.nodes.special

import cats.data.Writer
import com.github.skac112.miro.Ensemble
import com.github.skac112.fractalizer.SimpleNode

/**
  * Node which simply clears both channels. It can be helpful in making some
  * combined nodes.
  */
case class Cut(override val arity: Int = 0) extends SimpleNode {
  override def apply(ens: Ensemble) = Writer(Nil, Nil)
}
