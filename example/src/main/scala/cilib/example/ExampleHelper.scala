package cilib
package example

import scalaz._, Scalaz._
import spire.implicits._
import spire.math._

// Define the benchmarks. These functions are hardcoded, but it would be better to consider
// using https://github.com/ciren/benchmarks which is a far more extensive and
// complete set of benchmark functions and suites.

object ExampleHelper {

  val absoluteValue = (xs: NonEmptyList[Double]) => xs.foldMap(math.abs)

  val ackley = (xs: NonEmptyList[Double]) => {
    val n      = xs.size
    val sumcos = xs.foldMap(xi => cos(2 * pi * xi))
    val sumsqr = xs.foldMap(_ ** 2)

    -20 * exp(-0.2 * sqrt(sumsqr / n)) - exp(sumcos / n) + 20 + e
  }

  val quadric = (xs: NonEmptyList[Double]) => {
    val list = xs.toList

    (1 to xs.size).toList.foldMap { i =>
      list.take(i).foldMap(xi => xi) ** 2
    }
  }

  val spherical = (xs: NonEmptyList[Double]) => xs.foldMap(x => x * x)

}
