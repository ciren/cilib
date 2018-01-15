package cilib

import cilib.algebra._
import cilib.syntax.dotprod._
import cilib.Position._

import scalaz._
import Scalaz._

import spire.implicits._
import spire.math.sqrt

object Crossover {

  val nmpc: Crossover[Double] =
    parents => {
      def norm(x: Double, sum: Double) = 5.0 * (x / sum) - 1

      for {
        coef <- Dist.stdUniform.replicateM(4)
        s = coef.sum
        scaled = coef
          .map(norm(_, s))
          .toNel
          .getOrElse(sys.error("Impossible - this is a safe usage as coef is always length 4"))
        offspring = parents.zip(scaled).map(t => t._2 *: t._1).foldLeft1(_ + _)
      } yield NonEmptyList(offspring)
    }

  def pcx(sigma1: Double, sigma2: Double): Crossover[Double] =
    parents => {
      val mean = Algebra.meanVector(parents)
      val k = parents.size

      val initEta = NonEmptyList(parents.last - mean)
      val (dd, e_eta) = parents.init.foldLeft((0.0, initEta)) { (a, b) =>
        val d = b - mean

        if (d.isZero) a
        else {
          val e = Algebra.orthogonalize(d, a._2.toList)

          if (e.isZero) a
          else (a._1 + e.magnitude, e.normalize <:: a._2)
        }
      }

      val distance = if (k > 2) dd / (k - 1) else 0.0

      for {
        s1 <- Dist.gaussian(0.0, sigma1)
        s2 <- Dist.gaussian(0.0, sigma2)
      } yield {
        val offspring = parents.last + (s1 *: e_eta.head)
        NonEmptyList(e_eta.tail.foldLeft(offspring) { (c, e) =>
          c + (s2 *: (distance *: e))
        })
      }
    }

  def undx(sigma1: Double, sigma2: Double): Crossover[Double] =
    parents => {
      val n = parents.head.pos.length
      val bounds = parents.head.boundary

      // calculate mean of parents except main parents
      val g = Algebra.meanVector(
        parents.init.toNel.getOrElse(sys.error("UNDX requires at least 3 parents")))

      // basis vectors defined by parents
      val initZeta = List[Position[Double]]()
      val zeta = parents.init.foldLeft(initZeta) { (z, p) =>
        val d = p - g

        if (d.isZero) z
        else {
          val dbar = d.magnitude
          val e = Algebra.orthogonalize(d, z)

          if (e.isZero) z
          else z :+ (dbar *: e.normalize)
        }
      }

      val dd = (parents.last - g).magnitude

      // create the remaining basis vectors
      val initEta = NonEmptyList(parents.last - g)
      positiveInt(n - zeta.length) { value =>
        val reta = Position.createPositions(bounds, value) //n - zeta.length)
        val eta = reta.map(r => Algebra.orthonormalize(initEta :::> r.toIList))

        // construct the offspring
        for {
          s1 <- Dist.gaussian(0.0, sigma1)
          s2 <- Dist.gaussian(0.0, sigma2 / sqrt(n.toDouble))
          e_eta <- eta
        } yield {
          val vars = zeta.foldLeft(g)((vr, z) => vr + (s1 *: z))
          val offspring = e_eta.foldLeft(vars)((vr, e) => vr + ((dd * s2) *: e))

          NonEmptyList(offspring)
        }
      }
    }
}
