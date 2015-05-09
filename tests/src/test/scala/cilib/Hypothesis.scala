package cilib

import scalaz._
import Scalaz._

object Hypothesis {

  def chiSquared(bins: Int, critical: Double, sample: Vector[Double]) = {
    val n = sample.size
    val b = 10

    // The expected bins for the uniform distribution imply that the probability for each number is 1/n
    val expected = Range.inclusive(1, b).map(_ => n/b).toList
    val observed = sample.groupBy(x => (x * b).toInt).toList.map(x => x._2.length)

    def calc(o: Int, e: Int): Double = {
      val dev = o - e

      (dev * dev) / e.toDouble
    }

    val sum = Align[List].pad(observed, expected).foldLeft(0.0)((a, c) => a + (c match {
      case (Some(o), Some(e)) => calc(o, e)
      case (None, Some(e)) => calc(0, e)
      case _ => sys.error("impossible")
    }))

    sum < 27.83 && sample.forall(x => x >= 0.0 && x < 1.0)
  }
}
