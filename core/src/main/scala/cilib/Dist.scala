package cilib

import spire.implicits._
import spire.math.Interval

import _root_.eu.timepit.refined.auto._

object Dist {
  import RVar._

  val stdUniform: RVar[Double]     = next[Double]
  val stdNormal: RVar[Double]      = gaussian(0.0, 1.0)
  val stdCauchy: RVar[Double]      = cauchy(0.0, 1.0)
  val stdExponential: RVar[Double] = exponential(1.0)
  val stdGamma: RVar[Double]       = gamma(2, 2.0)
  val stdLaplace: RVar[Double]     = laplace(0.0, 1.0)
  val stdLognormal: RVar[Double]   = lognormal(0.0, 1.0)

  /** Generate a discrete uniform value in [from, to]. Note that the upper bound is *inclusive* */
  def uniformInt(i: Interval[Int]): RVar[Int] =
    next[Int].map { x =>
      val (from, to) = (i.lowerValue, i.upperValue)
      val (ll, hh)   = if (to < from) (to, from) else (from, to)
      val diff       = hh.toLong - ll.toLong
      if (diff == 0) ll
      else (ll.toLong + (math.abs(x.toLong) % (diff + 1))).toInt
    }

  def uniform(i: Interval[Double]): RVar[Double] =
    stdUniform.map { x =>
      i.lowerValue + x * (i.upperValue - i.lowerValue)
    }

  def cauchy(l: Double, s: Double): RVar[Double] =
    stdUniform.map { x =>
      l + s * math.tan(math.Pi * (x - 0.5))
    }

  def gamma(k: Double, theta: Double): RVar[Double] = {
    val n        = k.toInt
    val gammaInt = stdUniform.replicateM(n).map(_.map(x => -math.log(x)).sum)
    val gammaFrac = {
      val delta = k - n

      val a: RVar[(Double, Double)] =
        zio.prelude.fx.ZPure.mapN(stdUniform, stdUniform, stdUniform) { (u1, u2, u3) =>
          val v0 = math.E / (math.E + delta)
          if (u1 <= v0) {
            val zeta = math.pow(u2, 1.0 / delta)
            val eta  = u3 * math.pow(zeta, delta - 1)
            (zeta, eta)
          } else {
            val zeta = 1 - math.log(u2)
            val eta  = u3 * math.exp(-zeta)
            (zeta, eta)
          }
        }

      a.repeatUntil { case (zeta, eta) => eta > math.pow(zeta, delta - 1) * math.exp(-zeta) }.map(_._1)

      // a.flatMap(a0 =>
      //   BindRec[RVar].tailrecM(a0) { (x: (Double, Double)) =>
      //     val (zeta, eta) = x
      //     if (eta > math.pow(zeta, delta - 1) * math.exp(-zeta)) a.map(_.left[Double])
      //     else RVar.pure(zeta.right[(Double, Double)])
      //   }
      //)

      // def inner: RVar[Double] =
      //   for {
      //     u1 <- stdUniform
      //     u2 <- stdUniform
      //     u3 <- stdUniform
      //     (zeta, eta) = {
      //       val v0 = math.E / (math.E + delta)
      //       if (u1 <= v0) {
      //         val zeta = math.pow(u2, 1.0 / delta)
      //         val eta = u3 * math.pow(zeta, delta - 1)
      //         (zeta, eta)
      //       } else {
      //         val zeta = 1 - math.log(u2)
      //         val eta = u3 * math.exp(-zeta)
      //         (zeta, eta)
      //       }
      //     }
      //     r <- if (eta > math.pow(zeta, delta - 1) * math.exp(-zeta)) inner else RVar.pure(zeta)
      //   } yield r
      //
      // inner
    }

    zio.prelude.fx.ZPure.mapN(gammaInt, gammaFrac) { (a, b) =>
      (a + b) * theta
    }
  }

  def exponential(l: Double): RVar[Double] =
    stdUniform.map { math.log(_) / l }

  def laplace(b0: Double, b1: Double): RVar[Double] =
    stdUniform.map { x =>
      val rr = x - 0.5
      b0 - b1 * (math.log(1 - 2 * rr.abs)) * rr.sign
    }

  def lognormal(mean: Double, dev: Double): RVar[Double] =
    stdNormal.map(x => math.exp(mean + dev * x))

  def dirichlet(alphas: List[Double]): RVar[List[Double]] =
    zio.prelude.ForEach[List].forEach(alphas)(gamma(_, 1))
      .map { ys =>
        val sum = ys.sum
        ys.map(_ / sum)
      }

  def weibull(shape: Double, scale: Double): RVar[Double] =
    stdUniform.map { x =>
      scale * math.pow(-math.log(1 - x), 1 / shape)
    }


  private def DRandNormalTail(min: Double, ineg: Boolean): RVar[Double] = {
    def sample: RVar[(Double, Double)] =
      stdUniform.map(x => math.log(x) / min).zip(stdUniform.map(math.log(_)))

    sample
      .repeatUntil { case (v1, v2) => -2.0 * v2 >= v1 * v1 }
      .map(x => if (ineg) x._1 - min else min - x._1)
  }

  //private val ZIGNOR_C = 128               // Number of blocks
  private val ZIGNOR_R = 3.442619855899      // Start of the right tail
  private val ZIGNOR_V = 9.91256303526217e-3 // (R * phi(R) + Pr(X>=3)) * sqrt(2/pi)
  private val (blocks, ratios) = {
    def unfold[A, B](b: => B)(f: B => Option[(A, B)]): Stream[A] =
      f(b) match {
        case None         => Stream.empty[A]
        case Some((a, r)) => a #:: unfold(r)(f)
      }

    val f = math.exp(-0.5 * ZIGNOR_R * ZIGNOR_R)
    val unfoldStart = (126, ZIGNOR_V / f, ZIGNOR_R)
    val blocks: Stream[Double] = {
      Stream(ZIGNOR_V / f, ZIGNOR_R) ++
        (unfold(unfoldStart) { (a: (Int, Double, Double)) =>
          val f = math.exp(-0.5 * a._3 * a._3)
          val v = math.sqrt(-2.0 * math.log(ZIGNOR_V / a._2 + f))
          if (a._1 == 0) None else Some((v, (a._1 - 1, a._3, v)))
        }) ++ Stream(0.0)
    }

    (blocks, blocks.zip(blocks.drop(1)).map(a => a._1 / a._2))
    //(blocks, ???)//blocks.zip apzip(_.drop(1)).map(a => a._1 / a._2))
  }

  def gaussian(mean: Double, dev: Double): RVar[Double] =
    for {
      u <- stdUniform.map(2.0 * _ - 1)
      i <- next[Int].map(a => ((a & 0xFFFFFFFFL) % 127).toInt)
      r <- if (math.abs(u) < ratios(i)) RVar.pure(u * blocks(i))
          else if (i == 0) DRandNormalTail(ZIGNOR_R, u < 0)
          else {
            val x  = u * blocks(i)
            val f0 = math.exp(-0.5 * (blocks(i) * blocks(i) - x * x))
            val f1 = math.exp(-0.5 * (blocks(i + 1) * blocks(i + 1) - x * x))
            stdUniform.flatMap(a =>
              if (f1 + a * (f0 - f1) < 1.0) RVar.pure(x)
              else gaussian(mean, dev)
            )
          }
    } yield mean + dev * r

  private def invErf(x: Double) = {
    val a      = 0.147
    val halfPi = 2.0 / (math.Pi * a)
    val xcomp  = 1.0 - (x * x)

    val t1 = halfPi + (math.log(xcomp) / 2.0)
    val t2 = math.log(xcomp) / a

    math.signum(x) * math.sqrt(math.sqrt(t1 * t1 - t2) - t1)
  }

  private def invErfc(x: Double) = invErf(1.0 - x) // check this. invErfc(1 - x) == invErf(x)

  def levy(l: Double, s: Double): RVar[Double] =
    stdUniform.map { x =>
      l + s / (0.5 * invErfc(x) * invErfc(x))
    }
}
