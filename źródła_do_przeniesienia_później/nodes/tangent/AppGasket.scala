package fractalizer.nodes.tangent

import skac.miro._
import skac.fractalizer.Node._
import skac.miro.graphics._
import skac.miro.MathUtils._
// import scalaz._

case class AppGasket(r1c: Double,
 r2c: Double,
 r1Angle: Angle,
 cNum: Int = 104,
 override val stylerO: Option[Styler] = None) extends skac.fractalizer.Node(1) {
  type TanCircle = (PosGraphic, Boolean)
  type QueueEl = (TanCircle, TanCircle, TanCircle, TanCircle)
  type ProcState = (Seq[QueueEl], PosGraphics)
  val emptyState: ProcState = (Seq[QueueEl](), Seq[PosGraphic]())

  override def procNat(input: PosGraphics): PosGraphics = {
    val (g, s0) = input(0)
    val c0 = g toC
    val c0p = (c0, s0)
    // okrag o promieniu r1c * g.r (okrag 1)
    val c1 = Circle(r1c * c0.r, c0.genericAttribs)
    // srodek okregu 1
    val s1 = s0 + ((c0.r - c1.r) rot r1Angle)
    val c1p = (c1, s1)
    val r2 = r2c * c0.r
    // okrag styczny do c0 i c1 - w ogolnym przypadku sa takie dwa.
    val c2p = CirclesTan2(r2, false, true).procNat(Seq(c0p, c1p))(0)
    val cs3p = CirclesTan3(false, true, true).procNat(Seq(c0p, c1p, c2p))
    val c3p = if (cs3p(0)._1.toC.r > cs3p(1)._1.toC.r) cs3p(0) else cs3p(1)
    var state = add44(c0p, c1p, c2p, c3p)
    for (i <- 1 to cNum - 4) {
      state = procQueue(state)
    }
    state._2
  }

  /**
   * Dodaje do kolejki czworek cztery pozycje odpowiadajace kolom stycznym do
   * 3 kombinacji trojek w ramach pierwotnej czworki kol. Wysyla kola z
   * z pierwotnej czworki do kanalu przetwarzania.
   */
  private def add44(c0p: PosGraphic, c1p: PosGraphic, c2p: PosGraphic,
   c3p: PosGraphic): ProcState =
  {
    var state: ProcState = emptyState
    state = addToProcCh(state, c0p)
    state = addToProcCh(state, c1p)
    state = addToProcCh(state, c2p)
    state = addToProcCh(state, c3p)
    val tc0 = (c0p, false)
    val tc1 = (c1p, true)
    val tc2 = (c2p, true)
    val tc3 = (c3p, true)
    val tc4s = (tc0, tc1, tc2, tc3)
    Seq((tc1, tc2, tc3), (tc0, tc1, tc2), (tc0, tc2, tc3), (tc0, tc1, tc3)) map {tc3s =>
      val new_tan = newTanForFour(tc4s, tc3s)
      val new_foursome = (tc3s._1, tc3s._2, tc3s._3, (new_tan, true))
      state = addToQueue(state, new_foursome)
    }
    state
  }

  private def newTanForFour(foursome: QueueEl, threesome: (TanCircle, TanCircle, TanCircle)): PosGraphic = {
    CirclesTan3(threesome._1._2, threesome._2._2, threesome._3._2).procNat(
     Seq(threesome._1._1, threesome._2._1, threesome._3._1)) match {
      case Seq(cp) => cp
      case Seq(cp1, cp2) if cp1._1.toC.r < .0 => cp2
      case Seq(cp1, cp2) if cp2._1.toC.r < .0 => cp1
      case Seq(cp1, cp2) => if (closeFactor4(cp1, foursome) <
       closeFactor4(cp2, foursome)) cp2 else cp1
    }
  }

  /**
   * Counts "closeness" factor of a single (cp) circle to foursome of circles
   * located in queue element (cp4).
   * The value is computed as a product of "closeness" of singlecircle to
   * individual circles from foursome, which in turn is a sum of squares of
   * differences in x, y and r.
   * Value close to 0 suggests that single circle is identical to one of
   * foursome circles (module rounding error from computation).
   */
  private def closeFactor4(cp: PosGraphic, cp4: QueueEl): Double =
   cDiff(cp, cp4._1._1) * cDiff(cp, cp4._2._1) * cDiff(cp, cp4._3._1) * cDiff(cp, cp4._4._1)

  /**
   * Counts "difference" between circles which is a sum of squares of
   * differences in x, y and r.
   */
  private def cDiff(cp1: PosGraphic, cp2: PosGraphic): Double = {
    val c1 = cp1._1.toC
    val c2 = cp2._1.toC
    (cp1._2 - cp2._2).modulus2 + (c1.r - c2.r)*(c1.r - c2.r)
  }

  private def addToQueue(state: ProcState, c4: QueueEl) = (state._1 :+ c4, state._2)

  private def addToProcCh(state: ProcState, c: PosGraphic) = (state._1, state._2 :+ c)

  /**
   * Realizuje pojedynczy krok przetwarzania kolejki czworek kol - bierze
   * element z poczatku kolejki, dodaje czwarte kolo do kanalu przetwarzania
   * oraz wyznacza 3 nowe czworki, ktore dodaje do kolejki. Czworki wyznaczane
   * sa w ten sposob: dla kazdych dwoch sposrod trzech pierwszych kol dodaje
   * sie czwarte kolo, uzyskujac w ten sposob 3 trojki. Dla kazdej z tych trojek
   * oblicza sie kolo styczne nie nalezace do pierwotnej czworki i uzyskuje sie
   * w ten sposob czworke. Kazda z 3 nowych czworek dodawana jest na koniec
   * kolejki.
   */
  private def procQueue(state: ProcState): ProcState =
  {
    val tc4s = state._1.head
    var new_state = (state._1.tail, state._2)
    val (tc1, tc2, tc3, tc4) = (tc4s._1, tc4s._2, tc4s._3, tc4s._4)
    Seq((tc1, tc2, tc4), (tc1, tc3, tc4), (tc2, tc3, tc4)) map {tc3s =>
      val new_tan = newTanForFour(tc4s, tc3s)
      val new_foursome = (tc3s._1, tc3s._2, tc3s._3, (new_tan, true))
      new_state = addToQueue(new_state, new_foursome)
    }
    addToProcCh(new_state, tc4._1)
  }
}
