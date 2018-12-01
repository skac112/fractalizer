package skac.fractalizer

import skac.miro._
import com.github.skac112.funnodes._
import skac.fractalizer.Node.mergeFun
import skac.fractalizer.nodes.composition.Add
import Node._
import cats.data.Writer

object SimpleNode {
  case class QuickSimpleNode(nodeFun: NodeFun, override val arity: Int = 1) extends SimpleNode {
    override def apply(ens: Ensemble) = {
      val results = nodeFun(ens)
      Writer(results._1, results._2)
    }
  }

  def apply(nodeFun: NodeFun, arity: Int = 1) = QuickSimpleNode(nodeFun, arity)
}

/**
  * Node for which its internal node (n) is the node itself. Because of overriding apply (abstractly) such a node
  * doesn't use 'split while' mechanism in processing. It is intended to be used as an internal node of
  * other node.
  */
abstract class SimpleNode extends Node {
  override val n = this
  override def internalArity = arity
  override def arity = 1

  override def splitFun(ens: Ensemble) = {
    val split_ens = ens.splitAt(internalArity)
    (split_ens._1, split_ens._2, mergeFun _)
  }

  override def whileCond(ens: Ensemble) = false
  override def apply(ens: Ensemble): DrawingMaker[Ensemble]
}
