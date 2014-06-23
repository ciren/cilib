package cilib

import scalaz._, Scalaz._
import org.scalacheck._
import org.scalacheck.Prop._

object GeneratorTest extends Properties("Distribution") {

  def sizedGen(r: RVar[Double]) =
    Gen.sized { n => r.replicateM(n).run(RNG.init()).run._2.toVector }

  val gaussianRandom =
    sizedGen(Dist.stdNormal)

  val uniformRandom =
    sizedGen(Dist.stdUniform)

  // Perform a hypothesis test using the Anderson-Darling test for normality
  property("Gaussian hypothesis test") = {
    @annotation.tailrec def until[A](p: A => Boolean)(f: A => A)(z: A): A = if (p(z)) z else until(p)(f)(f(z))

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

    // NB: java.util.math.log is really ln
    def S(x: Seq[Double], F: Double => Double) = {
      val n = x.size
      val m = x.foldLeft(0.0)(_ + _) / x.size
      val stdDev = math.sqrt((1.0 / (n - 1)) * x.foldLeft(0.0)((a, b) => a + (b - m) * (b - m)))
      val Y = x.map(a => (a - m) / stdDev).map(F).sorted

      (1.0 / n) * (1 to n).foldLeft(0.0) {
        (s, i) => s + ((2 * i - 1) * math.log(Y(i - 1)) + (2 * (n - i) + 1) * math.log(1.0 - Y(i - 1)))
      }
    }

    forAll(gaussianRandom) {
      (a: Vector[Double]) => (a.size >= 100 && a.size <= 200) ==> {
        val n = a.size
        val a2 = -n - S(a, cdf_gauss)

        a2 < 3.857
      }
    }
  }

  property("Uniform hypothesis test") = forAll(uniformRandom) {
    (a: Vector[Double]) => (a.size >= 100 && a.size <= 200) ==> {
      val n = a.size

      // The expected bins for the uniform distribution imply that the probability for each number is 1/n
      val expected = List(n/2, n/2)
      val (o1, o2) = a.partition(_ < 0.5)
      val observed = List(o1, o2).map(_.length)

      (expected zip observed).map {
        case (e, o) => {
          val dev = o - e
          (dev * dev) / e
        }
      }.sum < 10.83
    }
  }
}
