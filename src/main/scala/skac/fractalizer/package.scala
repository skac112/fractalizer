package skac

package object fractalizer {
  /**
   * Konwersja podzialu na funkcje dostarczajaca podzial (dostarczyciela podzialu).
   */
  implicit def partSeqToPartSeqSupp(partSeq: Seq[Double]): () => Seq[Double] = {() => partSeq}
}
