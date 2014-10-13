package cilib

import scalaz._
//import Scalaz._

import org.scalacheck._
import org.scalacheck.Prop._

object GuideTest extends Properties("Guides") {

/*  implicit def ParticleGen(implicit P: Arbitrary[List[Double]]): Arbitrary[Particle[Mem[Double],Double]] = Arbitrary {
    for {
      l <- arbitrary[List[Double]]
    } yield (Mem(l, ))
  }

  property("gbest guide") =
    forAll { (a: List[Particle[Mem,Double]]) => (a.size > 10) ==> {
      val str: String = Guide.nbest(a, a.head).run(Min)

      str == a.sorted.head
    }}
*/
}
