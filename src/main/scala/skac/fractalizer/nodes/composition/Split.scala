package skac.fractalizer.nodes.composition
import com.github.skac112.funnodes.Split._
import skac.miro._
import skac.fractalizer._

case class Split(n1: skac.fractalizer.Node, n2: skac.fractalizer.Node) extends skac.fractalizer.Node {
  override def n = QuickSplit(n1.n, n2.n, n1.splitFun _)
  override val internalArity = n1.internalArity + n2.internalArity
}
