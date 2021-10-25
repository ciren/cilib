package cilib

import cilib.Position._
import cilib.algebra._
import cilib.syntax.dotprod._
import spire.implicits._
import spire.math.sqrt
import zio.prelude._

object Crossover {

  def nmpc(parents: NonEmptyVector[Position[Double]]): RVar[NonEmptyVector[Position[Double]]] = {
    def norm(x: Double, sum: Double) = 5.0 * (x / sum) - 1

    Dist.stdUniform.replicateM(4).map { coef =>
      val s: Double                      = coef.sum
      val scaled: NonEmptyVector[Double] = NonEmptyVector.fromIterableOption(coef.map(x => norm(x, s))).get

      parents.zip(scaled).map(t => t._2 *: t._1)
    }
  }

  def pcx(sigma1: Double, sigma2: Double)(
    parents: NonEmptyVector[Position[Double]]
  ): RVar[NonEmptyVector[Position[Double]]] = {

    val mean = Algebra.meanVector(parents)
    val k    = parents.size

    val initEta     = NonEmptyList(parents.last - mean)
    val (dd, e_eta) = parents.init.foldLeft((0.0, initEta)) { (a, b) =>
      val d = b - mean

      if (d.isZero) a
      else {
        val e = Algebra.orthogonalize(d, a._2.toList)

        if (e.isZero) a
        else (a._1 + e.norm, NonEmptyList.fromIterable(e.normalize, a._2))
      }
    }

    val distance = if (k > 2) dd / (k - 1) else 0.0

    for {
      s1 <- Dist.gaussian(0.0, sigma1)
      s2 <- Dist.gaussian(0.0, sigma2)
    } yield {
      val offspring = parents.last + (s1 *: e_eta.head)
      NonEmptyVector(e_eta.tail.foldLeft(offspring) { (c, e) =>
        c + (s2 *: (distance *: e))
      })
    }
  }

  def undx(sigma1: Double, sigma2: Double)(
    parents: NonEmptyVector[Position[Double]]
  ): RVar[NonEmptyVector[Position[Double]]] = {
    val n      = parents.head.pos.toChunk.length
    val bounds = parents.head.boundary

    // calculate mean of parents except main parents
    val g = Algebra.meanVector(
      NonEmptyVector.fromIterableOption(parents.init).getOrElse(sys.error("UNDX requires at least 3 parents"))
    )

    // basis vectors defined by parents
    val initZeta = List[Position[Double]]()
    val zeta     = parents.init.foldLeft(initZeta) { (z, p) =>
      val d = p - g

      if (d.isZero) z
      else {
        val dbar = d.norm
        val e    = Algebra.orthogonalize(d, z)

        if (e.isZero) z
        else z :+ (dbar *: e.normalize)
      }
    }

    val dd = (parents.last - g).norm

    // create the remaining basis vectors
    val initEta = NonEmptyVector(parents.last - g)
    val reta = Position.createPositions(bounds, positiveInt(n - zeta.length))
    val eta  = reta.map(r => Algebra.orthonormalize(initEta ++ r.toChunk))

    // construct the offspring
    for {
      s1    <- Dist.gaussian(0.0, sigma1)
      s2    <- Dist.gaussian(0.0, sigma2 / sqrt(n.toDouble))
      e_eta <- eta
    } yield {
      val vars      = zeta.foldLeft(g)((vr, z) => vr + (s1 *: z))
      val offspring = e_eta.foldLeft(vars)((vr, e) => vr + ((dd * s2) *: e))

      NonEmptyVector(offspring)
    }
  }

}
