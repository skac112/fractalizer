package com.github.skac112

import com.github.skac112.miro._
import com.github.skac112.miro.Graphic._
import com.github.skac112.vgutils._
import cats.data.Writer
import cats.{Monad, Monoid}
import scala.annotation._

package object fractalizer {
  type DrawingMaker[A] = cats.data.Writer[Ensemble, A]
  type NodeFun = Ensemble => (Ensemble, Ensemble)
  type EnsFun = Ensemble => Ensemble
  type SingleEnsFun = (Graphic, Point) => Ensemble
  type Styler = (Ensemble, Ensemble) => Seq[GenericAttribs]

  implicit val drawingMakerMonadInstance: Monad[DrawingMaker] = new Monad[DrawingMaker] {
    def pure[A](a: A): DrawingMaker[A] = Writer(Nil, a)
    def flatMap[A, B](fa: DrawingMaker[A])(f: A => DrawingMaker[B]): DrawingMaker[B] = {
      val new_writer = f(fa.value)
      Writer(fa.written ++ new_writer.written, new_writer.value)
    }
    override def map[A, B](fa: DrawingMaker[A])(f: A => B): DrawingMaker[B] = Writer(fa.written, f(fa.value))
    @tailrec
    def tailRecM[A, B](a: A)(f: A => DrawingMaker[Either[A, B]]): DrawingMaker[B] = {
      val new_w = f(a)
      val either_val = new_w.value
      either_val match {
        case Right(v) => Writer(new_w.written, v)
        case Left(v) => tailRecM(v)(f)
      }
    }
  }

  implicit val ensembleMonoidInstance: Monoid[Ensemble] = new Monoid[Ensemble] {
    override def combine(ens1: Ensemble, ens2: Ensemble) = ens1 ++ ens2
    override def empty = Seq.empty[PosGraphic[Graphic]]
  }

  /**
   * Konwersja podzialu na funkcje dostarczajaca podzial (dostarczyciela podzialu).
   */
  implicit def partSeqToPartSeqSupp(partSeq: Seq[Double]): () => Seq[Double] = {() => partSeq}

//  implicit def partSeqToPart(partSeq: Seq[Double] = Partition())
}
