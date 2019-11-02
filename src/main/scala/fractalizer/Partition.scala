package com.github.skac112.fractalizer

import com.github.skac112.vgutils._
import scala.math._
import scala.annotation._

object Partition {
  type PartSeq = Seq[Double]
  type PartSpans = Seq[(Double, Double)]

    /**
     * Losowy podzial odcinka jednostkowego z zadanym wspolczynnikiem agregacji.
     * Wartosc 0 oznacza wartosci nieskorelowane, wartosci ujemne oznaczaja
     * sklonnosc do agregacji a dodatnie do separacji
     * Wynikowa sekwencja ma postac 0,p(1),p(2),..,p(midPointNum),1
     */
    def random(midPointNum: Int, clusterCoeff: Double = .0): Partition = {
      lazy val cci = 1.0 / clusterCoeff

      @tailrec
      def sampleExp: Double = {
        val try_sample = -cci * log(1 - scala.math.random)
        if (try_sample <= 1.0) try_sample else sampleExp
      }

      def sample = clusterCoeff match {
        case .0 => scala.math.random
        // zaleznosc pochodzi ze "Świat sieci złożonych", s. 288"
        // w przypadku wylosowania wartosci spoza 0..1 trzeba ponowic losowanie
        case _ if clusterCoeff < 0 => sampleExp
        case _ => 1 - sampleExp
      }

      val samples = (1 to midPointNum + 1) map { _ => sample }
      val sum_inv = 1.0 / samples.sum
      Partition(samples map { sum_inv * _ })
    }

    def uniform(midPointNum: Int): Partition = {
      val inc = 1.0 / (midPointNum + 1)
      val incrs = (1 to midPointNum + 1) map {_ => inc}
      Partition(incrs)
    }

    def trivial = Partition(Seq(1.0))

    /**
     * Zwraca podzial "dookolny" na podstawie podzialu jednostkowego. Dokonuje
     * nie tylko normalizacji do zakresu 0..2*Pi, ale takze uwzglednia zmiane
     * topologii. Oznacza to, ze punkt 0 i 1 sa tozsame, wiec w wyjsciowym
     * rozkladzie jednostkowym pomijana jest koncowe 1. Jest on rowniez
     * "obracany" o losowy kat z zakresu 0..2*Pi (o ile parametr randomRot nie ma
     * wartosci false
     */
//    def circularize(unitPartition: PartSeq, randomRot: Boolean = true): PartSeq = {
//      // losowy kat obrotu (unormowany do 1 a nie 2 * Pi), bo przeskalowanie
//      // odbywa sie pozniej
//      val rot = if (randomRot) scala.math.random else .0
//
//      // unitPartition.dropRight(1).map {p =>
//      unitPartition map {p =>
//        val p2 = p + rot
//        2 * Pi * (p2 - floor(p2))
//      }
//    }

    /**
     * Zwraca podział "kątowy" na podstawie podzialu jednostkowego.
     * Przeskalowuje podany podział jednostkowy do zakresu kątowego
     */
//    def angularize(unitPartition: PartSeq, startAngle: Angle, endAngle: Angle): PartSeq = {
//      val range: Angle = endAngle - startAngle
//      unitPartition.map {p => (startAngle + Angle(p * range)).value}
//    }

    /**
     * Rozklad losowy "dookolny". Zwraca katy podzialu. Zwracana sekwencja jest
     * "kolowo monotoniczna".
     */
//    def circRandom(pointNum: Int, clusterCoeff: Double = .0): PartSeq = pointNum match {
//      case 1 => Seq(2 * Pi * scala.math.random)
//      case _ => circularize(random(pointNum - 1, clusterCoeff))
//    }

    /**
     * Jednorodny rozklad dookolny.
     */
//    def circUniform(pointNum: Int) = pointNum match {
//      case 1 => Seq(.0)
//      case _ => uniform(pointNum - 1).dropRight(1).map(2*Pi*_)
//    }

    def mapToRange(part: PartSeq, from: Double, to: Double) = part map {p => {from * (1 - p) + to * p}}

  /**
    * Creates partition with increases obeying power function:
    * n-th increase = (a*n + b) ^ exponent.
    * @param firstIncrease
    * @param lastIncrease
    * @param numInc
    * @param exponent
    * @return
    */
  def power(firstIncrease: Double, lastIncrease: Double, numInc: Int, exponent: Double) = {
    // n-th increase = (a*n + b) ^ exponent
    // firstIncrease -> n = 0
    // lastIncrease -> n = numInc - 1
    val inv_exp = 1.0 / exponent
    val b = math.pow(firstIncrease, inv_exp)
    val a = (math.pow(lastIncrease, inv_exp) - b) / (numInc - 1)
    val increases = (0 until numInc).map(n => math.pow((a * n + b), exponent))
    Partition(increases)
  }

  def linear(firstIncrease: Double, lastIncrease: Double, numInc: Int) =
    power(firstIncrease, lastIncrease, numInc, 1.0)
}

import com.github.skac112.fractalizer.Partition._

case class Partition(increases: Seq[Double]) {

  lazy val levels: Seq[Double] = increases.scanLeft(0.0)(_ + _)

  lazy val totalIncrease = increases.reduce(_ + _)

  /**
    * Returns sequence of values ranging from min to max corresponding to increases (rescaled).
    * @param min
    * @param max
    * @return
    */
  def rangeValues(min: Double, max: Double): PartSeq = {
    // increase
    val d = (max - min) / totalIncrease
    levels map (min + d * _)
  }

  def rangeSpans(min: Double, max: Double): Seq[(Double, Double)] = {
    val d = (max - min) / totalIncrease
    levels.map(min + d * _).sliding(2).map(pair => (pair(0), pair(1))).toSeq
  }

  /**
    * Returns sequence of angles ranging from start to end corresponding to increases.
    * @param start
    * @param end
    * @return
    */
  def angleValues(start: Angle, end: Angle, positiveDir: Boolean = true): Seq[Angle] = {
    // angle increase/decrease factor
    val d = positiveDir match {
      // positive d
      case true => ((end - start) / totalIncrease).value
      // negative d
      case false => -((start - end) / totalIncrease).value
    }

    levels map { l: Double => Angle(start.value + (d * l)) }
  }

  /**
    * Returns sequence of values in unit range.
    * @return
    */
  def unitValues: PartSeq = rangeValues(0.0, 1.0)

  def circleValues(start: Double = .0): Seq[Angle] = rangeValues(0, 2 * Pi) map { level: Double => Angle(level + start) }
  def +(other: Partition): Partition = ???
  def *(times: Int): Partition = ???
  def insert(other: Partition, startPos: Int): Partition = ???
  def insert(pt: Double): Partition = ???
  def movePt(idx: Double, displacement: Double): Partition = ???
  def moveFlex(idx: Double, displacement: Double, rigidity: Double): Partition = ???
}
