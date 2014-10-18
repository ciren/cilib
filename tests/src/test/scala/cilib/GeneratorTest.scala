package cilib

import scalaz._, Scalaz._
import org.scalacheck._
import org.scalacheck.Prop._

object GeneratorTest extends Properties("Distribution") {

  def sizedGen(r: RVar[Double]) =
    Gen.sized { _ => r.replicateM(250).run(RNG.fromTime)._2.toVector }

  val gaussianRandom =
    sizedGen(Dist.stdNormal)

  val uniformRandom =
    sizedGen(Dist.stdUniform)

  // Perform a hypothesis test using the Anderson-Darling test for normality
  property("stdNormal") = {
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
      (a: Vector[Double]) => {
        val n = a.size
        val a2 = -n - S(a, cdf_gauss)

        a2 < 3.857
      }
    }
  }

  property("stdUniform") = forAll(uniformRandom) {
    (a: Vector[Double]) => {
      val n = a.size
      val b = 10

      // The expected bins for the uniform distribution imply that the probability for each number is 1/n
      val expected = (1 to b).map(_ => n/b).toList
      val observed = a.groupBy(x => (x * b).toInt).toList.map(x => x._2.length)

      def calc(o: Int, e: Int): Double = {
        val dev = o - e
        (dev * dev) / e.toDouble
      }

      val F = Align[List]
      val sum = F.pad(observed, expected).foldLeft(0.0)((a, c) => a + (c match {
        case (Some(o), Some(e)) => calc(o, e)
        case (None, Some(e)) => calc(0, e)
        case _ => sys.error("impossible")
      }))

      //println("sum: " + sum)
      sum < 27.83 && a.forall(x => x >= 0.0 && x < 1.0)
    }
  }

  //property("Hypothesis test: uniformInt") = forAll()
}
