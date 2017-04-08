package skac

package object fractalizer {
  implicit def partSeqToPartSeqSupp(partSeq: Seq[Double]): () => Seq[Double] = {() => partSeq}
}
