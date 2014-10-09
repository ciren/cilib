package cilib

import scalaz._
//import Scalaz._

import org.scalacheck._
import org.scalacheck.Prop._

object GuideTest extends Properties("Guides") {

  /*property("gbest guide") = {
    forAll { (a: List[Int]) => (a.size > 10) ==> {
      val str: String = Guide.nbest(a, a.head).run(Min)

      str == a.sorted.head
    }
    }
  }*/
}
