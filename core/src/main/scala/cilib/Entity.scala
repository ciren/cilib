package cilib

import scalaz._

final case class Entity[S, A](state: S, pos: Position[A])

object Entity {

  implicit def entityEqual[S, A: scalaz.Equal]: scalaz.Equal[Entity[S, A]] =
    new scalaz.Equal[Entity[S, A]] {
      import Position._
      def equal(x: Entity[S, A], y: Entity[S, A]): Boolean =
        scalaz.Equal[Position[A]].equal(x.pos, y.pos)
    }

  implicit def entityFitness[S, A]: Fitness[Entity[S, ?], A] =
    new Fitness[Entity[S, ?], A] {
      def fitness(a: Entity[S, A]) =
        a.pos.objective
    }
}


object BCHM {

  // clamp and wrap are the same function - the logic operator is just flipped
  def clamp[A: scalaz.Equal](x: Position[A])(implicit N: spire.math.Numeric[A]): NonEmptyList[A] =
    x.pos.zip(x.boundary).map {
      case (p, b) =>
        if (N.toDouble(p) < b.lowerValue) N.fromDouble(b.lowerValue)
        else if (N.toDouble(p) > b.upperValue) N.fromDouble(b.upperValue)
        else p
    }

  def wrap[A: scalaz.Equal](p: Position[A])(implicit N: spire.math.Numeric[A]): NonEmptyList[A] =
    p.pos.zip(p.boundary).map {
      case (p, b) => {
        if (N.toDouble(p) < b.lowerValue) N.fromDouble(b.upperValue)
        else if (N.toDouble(p) > b.upperValue) N.fromDouble(b.lowerValue)
        else p
      }
    }

  def intoInterval[A: scalaz.Equal](default: NonEmptyList[A])(x: Position[A])(implicit N: spire.math.Numeric[A]) =
    x.pos.zip(x.boundary).zip(default).map {
      case ((p, b), m) => if (b.contains(N.toDouble(p))) p else m
    }

  def initToMidpoint[A: scalaz.Equal](x: Position[A])(implicit N: spire.math.Numeric[A]) =
    intoInterval[A](x.boundary.map(b => N.fromAlgebraic((b.upperValue + b.lowerValue) / 2)))(x)

  def reverseVelocity[A: scalaz.Equal](target: NonEmptyList[A])(p: Position[A])(implicit N: spire.math.Numeric[A]) =
    intoInterval(target.map(v => N.fromAlgebraic(N.toDouble(v) * -1.0)))(p)

  def absorb[A: scalaz.Equal:spire.math.Numeric](target: NonEmptyList[A])(x: Position[A]) = {
    val a = intoInterval(target)(x)
    clamp(Lenses._vector[A].set(a)(x))
  }

  def reflect[A: scalaz.Equal: spire.math.Numeric](x: Position[A], velocity: Position[A]) = {
    val a = reverseVelocity(velocity.pos)(x)
    clamp(Lenses._vector[A].set(a)(x))
  }
}
