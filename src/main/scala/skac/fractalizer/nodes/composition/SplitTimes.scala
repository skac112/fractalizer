package skac.fractalizer.nodes.composition

import com.github.skac112.funnodes.Split.QuickSplit
import com.github.skac112.funnodes._

case class SplitTimes(node: skac.fractalizer.Node, times: Int) extends skac.fractalizer.Node {
  override def n = node.n.splitTimes(node.splitFun _, times)
  override val internalArity = node.internalArity * times
}
