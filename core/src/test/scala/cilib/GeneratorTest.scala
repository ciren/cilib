package cilib

import org.scalacheck._
import org.scalacheck.Prop._

object GeneratorTest extends Properties("Distribution") {
  @annotation.tailrec
  def until[A](p: A => Boolean)(f: A => A)(z: A): A = if (p(z)) z else until(p)(f)(f(z))

  def phi_gauss(x: Double) = math.exp(-x * x / 2) / math.sqrt(2 * math.Pi)

  def cdf_gauss = (z: Double) => {
    if (z < -8.0) 0.0
    else if (z > 8.0) 1.0
    else {
      def test = (sum: Double, term: Double, _: Int) => sum + term == sum
      def sum = (a: Double, b: Double, i: Int) => (a + b, b * z * z / i, i + 2)
      0.5 + until(test.tupled)(sum.tupled)((0.0, z, 3))._1 * phi_gauss(z)
    }
  }

  // NB: java.util.math.log returns ln !!!!
  def S(x: Seq[Double], F: Double => Double) = {
    val n = x.size
    val m = x.foldLeft(0.0)(_ + _) / x.size
    val stdDev = math.sqrt((1.0 / (n - 1)) * x.foldLeft(0.0)((a, b) => a + (b - m) * (b - m)))
    val Y = x.map(a => (a - m) / stdDev).map(F).sorted

    (1.0 / n) * (1 to n).foldLeft(0.0) {
      (s, i) => s + ((2 * i - 1) * math.log(Y(i - 1)) + (2 * (n - i) + 1) * math.log(1.0 - Y(i - 1)))
    }
  }

  // Generator for gaussian numbers
  implicit val arbRandom: Arbitrary[Vector[Double]] = Arbitrary[Vector[Double]] {
    import scalaz._, Scalaz._
    val gaussian = Dist.stdNormal

    Gen.sized { n => (gaussian replicateM n).run(RNG.init()).run._2.toVector }
  }

  property("Gaussian hypothesis test") = forAll {
    (a: Vector[Double]) =>
      (a.size >= 100 && a.size <= 200) ==> {
        val n = a.size
        val a2 = -n - S(a, cdf_gauss)

        //if (a2 < 3.857) true else false
        //println("a2: " + a2)
        a2 < 6.044 //5.9694 // This value was obtained from: http://stats.stackexchange.com/questions/11310/critical-values-for-anderson-darling-test
      }
  }
}
