package cilib

import zio.test._

object GeneratorTest extends DefaultRunnableSpec {

  def sizedGen(r: RVar[Double]): Gen[Any, Vector[Double]] =
    Gen.const(r.replicateM(250).run(RNG.fromTime)._2.toVector)

  val gaussianRandom: Gen[Any, Vector[Double]] =
    sizedGen(Dist.stdNormal)

  @annotation.tailrec
  def until[A](p: A => Boolean)(f: A => A)(z: A): A = if (p(z)) z else until(p)(f)(f(z))

  def phi_gauss(x: Double): Double = math.exp(-x * x / 2) / math.sqrt(2 * math.Pi)

  def cdf_gauss: Double => Double = (z: Double) => {
    if (z < -8.0) 0.0
    else if (z > 8.0) 1.0
    else {
      def test = (sum: Double, term: Double, _: Int) => sum + term == sum
      def sum  = (a: Double, b: Double, i: Int) => (a + b, b * z * z / i, i + 2)
      0.5 + until(test.tupled)(sum.tupled)((0.0, z, 3))._1 * phi_gauss(z)
    }
  }

  // NB: java.util.math.log is really ln
  def S(x: Seq[Double], F: Double => Double): Double = {
    val n      = x.size
    val m      = x.foldLeft(0.0)(_ + _) / x.size
    val stdDev = math.sqrt((1.0 / (n - 1)) * x.foldLeft(0.0)((a, b) => a + (b - m) * (b - m)))
    val Y      = x.map(a => (a - m) / stdDev).map(F).sorted

    (1.0 / n) * Range.inclusive(1, n).foldLeft(0.0) { (s, i) =>
      s + ((2 * i - 1) * math.log(Y(i - 1)) + (2 * (n - i) + 1) * math.log(1.0 - Y(i - 1)))
    }
  }

  override def spec: ZSpec[Environment, Failure] = suite("Distribution")(
    // Perform a hypothesis test using the Anderson-Darling test for normality
    testM("stdNormal") {
      check(gaussianRandom) {
        case a =>
          val n  = a.size
          val a2 = -n - S(a, cdf_gauss)

          assert(a2)(Assertion.isLessThan(2.492)) // 5% significance  -- 3.857
      }
    }
  )

}
