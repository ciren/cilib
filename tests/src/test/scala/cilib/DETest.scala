package cilib

import org.scalacheck._
import org.scalacheck.Prop._
import scalaz._
import Scalaz._

object DETest extends Properties("DE") {

/*  property("binomial point selection") = forAll(Gen.choose(1, 100)) {
    (n: Int) => {
      val result = DE.binomial(n).run(0.5).run(RNG.init()).run._2
      result.length === n && result.exists(_ == true)
    }
  }*/

}
