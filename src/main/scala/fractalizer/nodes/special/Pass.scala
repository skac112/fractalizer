package com.github.skac112.fractalizer.nodes.special

import cats.data.Writer
import com.github.skac112.fractalizer._
import com.github.skac112.fractalizer.SimpleNode
import com.github.skac112.miro._

/**
  * Node which simply passes both channels (no-op node). It can be helpful in making some
  * combined nodes.
  */
class Pass extends SimpleNode {
  override def apply(ens: Ensemble) = Writer(Nil, ens)
}
