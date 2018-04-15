package cilib

import scalaz._
//import Scalaz._

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

  def boundaryConstraint[A: scalaz.Equal](f: Position[A] => NonEmptyList[A])(x: Position[A]): Position[A] = {
    Lenses._vector[A].set(f(x))(x)
  }

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


  def initTo[A: scalaz.Equal](target: NonEmptyList[A])(x: Position[A])(implicit N: spire.math.Numeric[A]) =
    x.pos.zip(x.boundary).zip(target).map {
      case ((p, b), m) => if (b.contains(N.toDouble(p))) p else m
    }

  def initToMidpoint[A: scalaz.Equal](x: Position[A])(implicit N: spire.math.Numeric[A]) =
    initTo[A](x.boundary.map(b => N.fromAlgebraic((b.upperValue + b.lowerValue) / 2)))(x)


  def reverseVelocity[A: scalaz.Equal](p: Position[A])(target: NonEmptyList[A])(implicit N: spire.math.Numeric[A]) =
    initTo(target.map(v => N.fromAlgebraic(N.toDouble(v) * -1.0)))(p)

  def zeroedVelocity[A: scalaz.Equal](target: NonEmptyList[A])(p: Position[A])(implicit N: spire.math.Numeric[A]) =
    initTo(target)(p)
    // p.pos.zip(p.boundary).zip(target).map {
    //   case ((p, b), v) => if (b.contains(N.toDouble(p))) p else v
    // }

  def absorb[A: scalaz.Equal](target: NonEmptyList[A])(x: Position[A])(implicit N: spire.math.Numeric[A]) =
    clamp(zeroedVelocity(target)(x))

  def reflect[S, A: scalaz.Equal](x: Entity[S, A])(implicit N: spire.math.Numeric[A],
                                                   V: HasVelocity[S, A]) = clamp(reverseVelocity(x))
}
