package cilib
package example

import spire.implicits._
import spire.math._
import zio.prelude._

// Define the benchmarks. These functions are hardcoded, but it would be better to consider
// using https://github.com/ciren/benchmarks which is a far more extensive and
// complete set of benchmark functions and suites.

object ExampleHelper {

  val absoluteValue: NonEmptyVector[Double] => Double = (xs: NonEmptyVector[Double]) => xs.map(math.abs).sum

  val ackley: NonEmptyVector[Double] => Double = (xs: NonEmptyVector[Double]) => {
    val n      = xs.size
    val sumcos = xs.map(xi => cos(2 * pi * xi)).sum
    val sumsqr = xs.map(_ ** 2).sum

    -20 * exp(-0.2 * sqrt(sumsqr / n)) - exp(sumcos / n) + 20 + e
  }

  val quadric: NonEmptyVector[Double] => Double = (xs: NonEmptyVector[Double]) => {
    val list = xs.toChunk.toList

    (1 to xs.size).toList.map { i =>
      list.take(i).sum ** 2
    }.sum
  }

  val spherical: NonEmptyVector[Double] => Double = (xs: NonEmptyVector[Double]) => xs.map(x => x * x).sum

}
