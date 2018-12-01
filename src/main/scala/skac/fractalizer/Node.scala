package skac.fractalizer

import skac.miro._
import skac.miro.Graphic._
import skac.fractalizer.nodes.composition._

import scala.annotation.tailrec
import scala.math._
import scala.collection._
//import scalaz.Writer
import cats._
import cats.data._
import cats.implicits._
import cats.data.Writer._
import cats.syntax.writer._
import cats.syntax.applicative._
import cats.instances.all._
import com.github.skac112.funnodes._
import com.github.skac112.funnodes.FunNode._

object Node {
  case class QuickWrappedNode(override val n: Node) extends Node {
    override def internalArity = n.arity
    override def arity = 0
  }

  def wrap(intNode: Node) = QuickWrappedNode(intNode)

//  type DrawingMaker[A] = cats.data.Writer[Ensemble, A]
////  implicitly[Monad[DrawingMaker]]
//
//  implicit val drawingMakerInstance: Monad[DrawingMaker] = new Monad[DrawingMaker] {
//    def pure[A](a: A): DrawingMaker[A] = Writer(Nil, a)
//    def flatMap[A, B](fa: DrawingMaker[A])(f: A => DrawingMaker[B]): DrawingMaker[B] = {
//      val new_writer = f(fa.value)
//      Writer(fa.written ++ new_writer.written, new_writer.value)
//    }
//    override def map[A, B](fa: DrawingMaker[A])(f: A => B): DrawingMaker[B] = Writer(fa.written, f(fa.value))
//    @tailrec
//    def tailRecM[A, B](a: A)(f: A => DrawingMaker[Either[A, B]]): DrawingMaker[B] = {
//      val new_w = f(a)
//      val either_val = new_w.value
//      either_val match {
//        case Right(v) => Writer(new_w.written, v)
//        case Left(v) => tailRecM(v)(f)
//      }
//    }
//  }

//  implicit val drawingMakerInstance: Monad[DrawingMaker] = new Monad[DrawingMaker] {
//    def pure[A](a: A): DrawingMaker[A] = Writer(Nil, a)
//    def flatMap[A, B](fa: DrawingMaker[A])(f: A => DrawingMaker[B]): DrawingMaker[B] = ???
//    def map[A, B](fa: DrawingMaker[A])(f: A => B): DrawingMaker[B] = ???
//    def tailRecM[A, B](a: A)(f: A => DrawingMaker[Either[A, B]]): DrawingMaker[B] = ???
//  }

  //  /**
//   * Creates a node which is a s sequential connection of nodes - output of the
//   * first becomes an input of the second and so on.
//   */
//  def chain(nodes: Node*) = new Node(nodes(0).naturalArity) {
//    override def apply(data: NodeData) = {
//      var d = data
//      nodes foreach {n => (d = n(d))}
//      d
//    }
//  }
//
//  /**
//   * Rozdziela elementy graficzne pomiedzy wezly skladowe (jak przy rozdawaniu
//   * w kartach)
//   */
//  def splitInput(nodes: Node*): Node = new Node(nodes map {_.naturalArity} sum) {
//    override def apply(data: NodeData) = {
//      val proc_data = data._1
//      val draw_data = data._2
//      val pd_len = proc_data.size
//      val turns = ceil(pd_len.toDouble / naturalArity.toDouble).toInt
//
//      // przetwarzanie przez wezly
//      val out = for {
//        t <- 0 until turns
//        turn_len = if ((t + 1)*naturalArity > pd_len) pd_len - t*naturalArity else naturalArity
//        // indices of starts of graphic chunks for successive nodes (relative to turn start index in proc_data collection)
//        n_starts = nodes.view.dropRight(1).scanLeft(0) {(a: Int, n: Node) => {a + n.naturalArity}} takeWhile {_ < turn_len}
//        (n_i_pairs, last_start) = if (n_starts.size > 1) {
//          (n_starts.sliding(2).toSeq, n_starts.last)
//        }
//        else {
//          (Seq[Int](), 0)
//        }
//        turn_start = t * naturalArity
//
//        // n_i_pairs_2 = n_i_pairs.asInstanceOf[Iterator[Seq[Int]]].toSeq :+ Seq(last_start, turn_len) map {pair => {Seq(pair(0) + turn_start, pair(1) + turn_start)}}
//        n_i_pairs_2 = (n_i_pairs :+ Seq(last_start, turn_len)) map {pair =>
//          val t_pair = pair.asInstanceOf[Seq[Int]]
//          Seq(t_pair(0) + turn_start, t_pair(1) + turn_start)
//        }
//        zipped = nodes zip n_i_pairs_2
//        outputs <- zipped map {kv =>
//          val n = kv._1
//          n((proc_data.slice(kv._2(0), kv._2(1)), draw_data))
//        }
//      } yield outputs
//
//      val proc_ch_out: PosGraphics = out match {
//        case empty @ Seq() => Seq()
//        case one @ Seq(x) => x._1
//        case _ => out map (_._1) reduceLeft {_ ++ _}
//      }
//
//      // zlaczenie wynikow
//      (proc_ch_out, mergeParallelDraw(data._2, out))
//    }
//
//    /**
//     * Parallel-input connection of nodes. Input is submitted to all nodes and
//     * output  is collected.
//     */
//    // def parallelInput(nodes: Node*): Node = new Node(nodes map {_.naturalArity} max) {
//    //   override def apply(data: NodeData) = nodes flatMap {_.apply(nodes)}
//    // }
//
//    /**
//     * Dokonuje polaczenia wyjsc kanalow rysowania wezlow polaczonych rownolegle
//     * (poprzez || lub |||)
//     */
//    private def mergeParallelDraw(draw_in: PosGraphics, out: Seq[NodeData]): PosGraphics = {
//      val in_count = draw_in.size
//      (out map (_._2)).foldLeft(draw_in) {(acc: PosGraphics, curr: PosGraphics) => {acc ++ curr.drop(in_count)}}
//    }
//  }
//
//  /**
//   * Połączenie z rozdziałem elementow po kopiach jednego węzła. Obiekt węzła nie jest kopiowany.
//   */
//  def splitInput(node: Node, times: Int): Node = splitInput((Seq.fill(times)(node)):_*)

  protected def splitFun(naturalArity: Int = 1): SplitFun[Ensemble, Ensemble] = (ens: Ensemble) => {
    val split_ens = ens.splitAt(naturalArity)
    (split_ens._1, split_ens._2, mergeFun _)
  }

//  protected def splitCond(naturalArity: Int) = (ens: Ensemble) => {(naturalArity != 0) && (ens.size > naturalArity)}

  def mergeFun(ens1: Ensemble, ens2: Ensemble): Ensemble = ens1 ++ ens2

  def fromFun(nodeFun: NodeFun, internalArity: Int = 1): Node = wrap(SimpleNode(nodeFun, internalArity))

  /**
    * Creates a "pure draw" node - which passes it input unchanged in proc channel and only draws something.
    * @param drawFun
    */
  def draw(drawFun: EnsFun): Node = {
    val fun = (ens: Ensemble) => (drawFun(ens), ens)
    wrap(SimpleNode(fun, 0))
  }

  /**
    * Creates a "pure proc" node - which processes its input with a given function in proc channel but
    * doesn't draw anything.
    * @param procFun
    * @return
    */
  def proc(procFun: EnsFun, internalArity: Int = 1): Node = wrap(SimpleNode(makeProcNodeFun(procFun), internalArity))


  def makeNodeFun(drawFun: EnsFun, procFun: EnsFun): NodeFun = (ens: Ensemble) => (drawFun(ens), procFun(ens))

  /**
    * Creates a "pure proc" node function.
    * @param procFun
    * @return
    */
  def makeProcNodeFun(procFun: EnsFun): NodeFun = (ens: Ensemble) => (Nil, procFun(ens))

  /**
    * Creates a proc function processing single input (one-element Ensemble).
    * @param fun
    * @return
    */
  def makeSingleInProcFun(fun: SingleEnsFun) = (ens: Ensemble) => {fun(ens(0)._1, ens(0)._2)}


  /**
    * Creates a "pure proc" node function processing single input (one-element) Ensemble.
    * @param fun
    * @return
    */
  def makeSingleInNodeFun(fun: SingleEnsFun) = makeProcNodeFun(makeSingleInProcFun(fun))
}

import Node._

abstract class Node extends SplitWhile[DrawingMaker, Ensemble, Ensemble] {
  def internalArity: Int
  def arity: Int = 0

//  override def apply(ens: Ensemble) = stylerO match {
//    case Some(styler) => {
//      for {
//        out <- super.apply(ens)
//        attrss = styler(out)
//      } yield (out zip attrss).map(_ match {
//        case ((g, pt), attrs) => (g.setGenericAttribs(attrs), pt)
//      })
//    }
//    case _ => super.apply(ens)
//  }

  override def splitFun(ens: Ensemble) = {
    val split_ens = ens.splitAt(internalArity)
    (split_ens._1, split_ens._2, mergeFun _)
  }

  override def whileCond(ens: Ensemble) = {(internalArity != 0) && (ens.size > internalArity)}

  /**
    * Draw function of internal funNode.
    * @param ens
    * @return
    */
  def drawFun(ens: Ensemble): Ensemble = apply(ens).run._1

  /**
    * Proc function of internal funNode.
    * @param ens
    * @return
    */
  def procFun(ens: Ensemble): Ensemble = apply(ens).run._2

  /**
    * Sequential connection of nodes - this node is applied first (is internal).
    */
  def +(other: Node) = Add(this, other)

  def |(other: Node) = skac.fractalizer.nodes.composition.Split(this, other)

  def |*(times: Int) = skac.fractalizer.nodes.composition.SplitTimes(this, times)

  /**
    * Creates a node which has proc part determined by this node and draw part determined by given
    * EnsFun.
    * @param drawFun
    * @return
    */
  def withDraw(drawFun: EnsFun): Node = fromFun(makeNodeFun(drawFun, this.procFun),
    internalArity)

  def withDraw(drawNode: Node): Node = withDraw(drawNode.drawFun _)

  def withProc(procFun: EnsFun, internalArity: Int = 1): Node =
    fromFun(makeNodeFun(this.drawFun, procFun), internalArity)

  def withProc(procNode: Node, internalArity: Int): Node = withProc(procNode.procFun _, internalArity)

//  def stylerO: Option[Styler] = None

    //  final def apply(data: NodeData): NodeData = {
    //    val elems = setFun(data)
    //    (elems._1, data._2 ++ elems._2)
    //    (proc(data._1), data._2 ++ draw(data))
    //  }
    //
    //  /**
    //    * Function which is set by user and defines how node works. This function is used to create main function
    //    * (defined by apply method).
    //    */
    //  abstract def setFun(data: NodeData): NodeData
    //
    //  private[fractalizer] def proc(graphics: PosGraphics): PosGraphics = {
    //    // Splits input Graphocs on groups of size equal to natural arity (with possible exception of the last group).
    //    graphics.grouped(naturalArity).toSeq.flatMap(procPart _)
    //  }
    //
    //  def stylerO: Option[Styler] = None
    //
    //  /**
    //   * Proceduje dane wejsciowe "kawalkami" po n elementow (n == naturalArity),
    //   * ew. uzywajac stylera, jesli jest.
    //   * Dane moga miec mniej elementow ze wzgledu na to, ze nie zawsze liczba
    //   * elementow w danych wejsciowych jest krotnoscia naturalArity.
    //   */
    //  def procPart(graphics: PosGraphics): PosGraphics = {
    //    val nat_out = procNat(graphics)
    //    stylerO match {
    //      case Some(styler) => {
    //        val styles = styler(graphics, nat_out)
    //        (nat_out zip styles) map {kv =>
    //          val styled_g = kv._1._1.setGenericAttribs(kv._2)
    //          (styled_g, kv._1._2)
    //        }
    //      }
    //      case _ => nat_out
    //    }
    //  }
    //
    //  def procNat(graphics: PosGraphics): PosGraphics = Nil
    //  // def procSingle(graphic: Graphic): Seq[Graphic] = Nil
    //
    //  /**
    //   * Bazowa implementacja nie rysuje niczego - nie dodaje grafik do kanalu
    //   * rysowania.
    //   */
    //  def draw(data: NodeData): PosGraphics = Nil
    //
    //
    //  /**
    //   * Szereg n wezlow tej klasy
    //   */
    //  def *(count: Int) = chain(Seq.fill(count)(this):_*)



    //
    // def apply(Elems: Seq[E]): Seq[E] = Elems flatMap {e => applySingle(e)}
    // def applySingle(El: E) = Seq[E]()
    // def +(Other: Replicator[E]) = chain[E](this, Other)
    // def *(RepeatCount: Int) = chain[E](Seq.fill(RepeatCount)(this):_*)
    // def |(Other: Replicator[E]) = parallelInput(this, Other)
    // def |*(Times: Int) = parallelInput(this, Times)

    //  def ||(other: Node) = splitInput(this, other)
    //  def ||*(times: Int) = splitInput(this, times)

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
    //  }
}
