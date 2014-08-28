package cilib

import org.scalacheck._
import org.scalacheck.Prop._

object RVarTest extends Properties("RVar") {

  property("ints") =
    forAll { (n: Int) => (n > 5 && n < 1000) ==> {
      val sample = RVar.ints(n).run(RNG.fromTime)._2.toVector.map(_.toDouble)

      Hypothesis.chiSquared(2, 4, sample)
    }}

}
