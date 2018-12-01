package skac.fractalizer

/**
  * Node with optional styler.
  */
abstract class WrappingNode extends Node {
  def internalNode: Node
  def stylerO: Option[Styler]

  override lazy val n = stylerO match {
    case Some(styler) => StyledNode(internalNode, styler)
    case None => internalNode
  }

  override def internalArity = internalNode.arity
}
