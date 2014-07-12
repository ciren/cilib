package cilib

import scalaz._
import Scalaz._

import org.scalacheck._
import org.scalacheck.Prop._

object ShufflingTest extends Properties("Shuffling") {

  val rng = RNG.fromTime

  property("maintains members") =
    forAll(Gen.chooseNum(1, 1000)) { (n: Int) =>
      val ints = RVar.ints(n)

      val shuffled = ints.flatMap(x => RVar.shuffle(x)).run(rng).run._2.sorted
      val sorted = ints.run(rng).run._2.sorted

      shuffled === sorted && shuffled.size === n
    }
}
