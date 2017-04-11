package skac.fractalizer

import skac.miro._
import skac.miro.Graphic._
import scala.math._
import scala.collection._

object Node {
  type PosGraphics = Seq[PosGraphic]
  type NodeData = (PosGraphics, PosGraphics)

  def chain(nodes: Node*) = new Node(nodes(0).naturalArity) {
    override def apply(data: NodeData) = {
      var d = data
      nodes foreach {n => (d = n(d))}
      d
    }
  }

  /**
   * Rozdziela elementy graficzne pomiedzy wezly skladowe (jak przy rozdawaniu
   * w kartach)
   */
  def splitInput(nodes: Node*): Node = new Node(nodes map {_.naturalArity} sum) {
    override def apply(data: NodeData) = {
      val proc_data = data._1
      val draw_data = data._2
      val pd_len = proc_data.size
      val turns = ceil(pd_len.toDouble / naturalArity.toDouble).toInt

      // przetwarzanie przez wezly
      val out = for {
        t <- 0 until turns
        turn_len = if ((t + 1)*naturalArity > pd_len) pd_len - t*naturalArity else naturalArity
        // indices of starts of graphic chunks for successive nodes (relative to turn start index in proc_data collection)
        n_starts = nodes.view.dropRight(1).scanLeft(0) {(a: Int, n: Node) => {a + n.naturalArity}} takeWhile {_ < turn_len}
        (n_i_pairs, last_start) = if (n_starts.size > 1) {
          (n_starts.sliding(2), n_starts.last)
        }
        else {
          (Seq[Int](), 0)
        }
        turn_start = t * naturalArity
        n_i_pairs_2 = n_i_pairs.asInstanceOf[Iterator[Seq[Int]]].toSeq :+ Seq(last_start, turn_len) map {pair => {Seq(pair(0) + turn_start, pair(1) + turn_start)}}
        zipped = nodes zip n_i_pairs_2
        outputs <- zipped map {kv =>
          val n = kv._1
          n((proc_data.slice(kv._2(0), kv._2(1)), draw_data))
        }
      } yield outputs

      // zlaczenie wynikow
      (out map (_._1) reduceLeft {_ ++ _}, mergeParallelDraw(data._2, out))
    }

    /**
     * Dokonuje polaczenia wyjsc kanalow rysowania wezlow polaczonych rownolegle
     * (poprzez || lub |||)
     */
    def mergeParallelDraw(draw_in: PosGraphics, out: Seq[NodeData]): PosGraphics = {
      val in_count = draw_in.size
      (out map (_._2)).foldLeft(draw_in) {(acc: PosGraphics, curr: PosGraphics) => {acc ++ curr.drop(in_count)}}
    }
  }

  /**
   * Połączenie z rozdziałem elementow po kopiach jednego węzła. Obiekt węzła nie jest kopiowany.
   */
  def splitInput(node: Node, times: Int): Node = splitInput((Seq.fill(times)(node)):_*)

  abstract class Styler extends Function2[PosGraphics, PosGraphics, Seq[GenericAttribs]]
}

import Node._

abstract class Node(val naturalArity: Int = 1) extends Function1[NodeData, NodeData] {
  def apply(data: NodeData): NodeData = (proc(data._1), draw(data))
  // def proc(graphics: Graphics): Graphics = graphics flatMap {g => procSingle(g)}

  private[fractalizer] def proc(graphics: PosGraphics): PosGraphics = {
    // dzieli wejsciowe dane na grupy po n elementow PosGraphic, gdzie n to
    // naturalna krotnosc (naturalArity)
    graphics.grouped(naturalArity).toSeq.flatMap(procPart _)
  }

  def stylerO: Option[Styler] = None

  /**
   * Proceduje dane wejsciowe "kawalkami" po n elementow (n == naturalArity),
   * ew. uzywajac stylera, jesli jest.
   * Dane moga miec mniej elementow ze wzgledu na to, ze nie zawsze liczba
   * elementow w danych wejsciowych jest krotnoscia naturalArity.
   */
  def procPart(graphics: PosGraphics): PosGraphics = {
    val nat_out = procNat(graphics)
    stylerO match {
      case Some(styler) => {
        val styles = styler(graphics, nat_out)
        (nat_out zip styles) map {kv =>
          val styled_g = kv._1._1.setGenericAttribs(kv._2)
          (styled_g, kv._1._2)
        }
      }
      case _ => nat_out
    }
  }

  def procNat(graphics: PosGraphics): PosGraphics = Nil
  // def procSingle(graphic: Graphic): Seq[Graphic] = Nil

  /**
   * Bazowa implementacja nie rysuje niczego - nie dodaje grafik do kanalu
   * rysowania.
   */
  def draw(data: NodeData): PosGraphics = data._2

  /**
   * Szeregowe polaczenie wezlow
   */
  def +(other: Node) = chain(this, other)

  /**
   * Szereg n wezlow tej klasy
   */
  def *(count: Int) = chain(Seq.fill(count)(this):_*)



  //
  // def apply(Elems: Seq[E]): Seq[E] = Elems flatMap {e => applySingle(e)}
  // def applySingle(El: E) = Seq[E]()
  // def +(Other: Replicator[E]) = chain[E](this, Other)
  // def *(RepeatCount: Int) = chain[E](Seq.fill(RepeatCount)(this):_*)
  // def |(Other: Replicator[E]) = parallelInput(this, Other)
  // def |*(Times: Int) = parallelInput(this, Times)
  def ||(other: Node) = splitInput(this, other)
  def ||*(times: Int) = splitInput(this, times)
  // def rot(Angle: Double) = Replicator.rot(this, Angle)
  // def singleFilter(Filter: Replicator[E]) = Replicator.singleFilter(this, Filter)
  // def ->(Filter: Replicator[E]) = Replicator.singleFilter(this, Filter)
  //
  // def draw(Elem: GeomElem) {
  //   Replicator.Canvas match {
  //     case c: Group => c.getChildren.add(Elem.render)
  //     case p: Pane => p.getChildren.add(Elem.render)
  //   }
  // }
}
