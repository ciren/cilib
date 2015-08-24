package cilib

import scalaz._
import Scalaz._

import org.scalacheck._
import org.scalacheck.Prop._
import org.scalacheck.Gen._
import org.scalacheck.Arbitrary._

object QualityTest extends Properties("Quality") {

  implicit val intQuality = new Quality[Int] {
    def quality(a: Int) = Maybe.just((Valid(a.toDouble), 0))
  }

  property("Minimization quality compare") =
    forAll { (x: Int, y: Int) =>
      Comparison.quality(Min)(x, y) == (x min y)
    }

  property("Maximization quality compare") =
    forAll { (x: Int, y: Int) =>
      Comparison.quality(Max)(x, y) == (x max y)
    }

}
