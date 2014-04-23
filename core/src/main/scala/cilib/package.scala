import scalaz._

import spire.algebra.Field
import spire.algebra.VectorSpace
import spire.implicits._

package object cilib {

  def positive(d: Double): Option[Double @@ Tags.Positive] =
    if (d > 0.0) Tag.subst(Some(d))
    else None

  def negative(d: Double): Option[Double @@ Tags.Negative] =
    if (d < 0.0) Tag.subst(Some(d))
    else None

//  implicit object SolutionMultiplicativeSemigroup extends spire.algebra.MultiplicativeSemigroup[Solution[Vector[Double]]] {
    //def times(x: Solution[Vector[Double]], y: Solution[Vector[Double]]): Solution[Vector[Double]] =
//      Solution((x.x, y.x).zipped map { _ * _ })
//  }
}
