import scalaz._

import spire.algebra.Field
import spire.algebra.VectorSpace
import spire.implicits._

package object cilib {

  // Aliases
  type Solution = Vector[Double]

  sealed trait Positive
  sealed trait Negative

  def positive(d: Double): Option[Double @@ Positive] =
    if (d > 0.0) Tag.subst(Some(d))
    else None

  def negative(d: Double): Option[Double @@ Negative] =
    if (d < 0.0) Tag.subst(Some(d))
    else None

}
