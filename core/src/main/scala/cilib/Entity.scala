package cilib

import scalaz._
import Scalaz._

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
  def clamp[S, A: scalaz.Equal](x: Entity[S, A])(implicit N: spire.math.Numeric[A]) =
    Lenses._position.modify((p: Position[A]) => {
      val c: NonEmptyList[A] = p.pos.zip(p.boundary).map {
        case (p, b) =>
          if (N.toDouble(p) < b.lowerValue) N.fromDouble(b.lowerValue)
          else if (N.toDouble(p) > b.upperValue) N.fromDouble(b.upperValue)
          else p
      }
      Lenses._vector[A].set(c)(p)
    })(x)

  def initToPB[S, A: scalaz.Equal](x: Entity[S, A])(implicit N: spire.math.Numeric[A],
                                                    M: HasMemory[S, A]) =
    Lenses._position.modify((p: Position[A]) => {
      val c: NonEmptyList[A] = p.pos.zip(p.boundary).zip(M._memory.get(x.state).pos).map {
        case ((p, b), m) => if (b.contains(N.toDouble(p))) p else m
      }
      Lenses._vector[A].set(c)(p)
    })(x)

  def initToGB[S, A: scalaz.Equal](x: Entity[S, A], gBest: Position[A])(
      implicit N: spire.math.Numeric[A]) =
    Lenses._position.modify((p: Position[A]) => {
      val c: NonEmptyList[A] = p.pos.zip(p.boundary).zip(gBest.pos).map {
        case ((p, b), g) => if (b.contains(N.toDouble(p))) p else g
      }
      Lenses._vector[A].set(c)(p)
    })(x)

  def zeroedVelocity[S, A: scalaz.Equal](x: Entity[S, A])(implicit N: spire.math.Numeric[A],
                                                          V: HasVelocity[S, A]) =
    Entity(
      V._velocity.modify((p: Position[A]) => {
        val c: NonEmptyList[A] = p.pos.zip(p.boundary).zip(V._velocity.get(x.state).pos).map {
          case ((p, b), v) => if (b.contains(N.toDouble(p))) p else v
        }
        Lenses._vector[A].set(c)(p)
      })(x.state),
      x.pos
    )

  def reverseVelocity[S, A: scalaz.Equal](x: Entity[S, A])(implicit N: spire.math.Numeric[A],
                                                           V: HasVelocity[S, A]) =
    Entity(
      V._velocity.modify((p: Position[A]) => {
        val c: NonEmptyList[A] = p.pos.zip(p.boundary).zip(V._velocity.get(x.state).pos).map {
          case ((p, b), v) =>
            if (b.contains(N.toDouble(p))) v else N.fromAlgebraic(N.toDouble(v) * -1.0)
        }
        Lenses._vector[A].set(c)(p)
      })(x.state),
      x.pos
    )

  def initToMidPoint[S, A: scalaz.Equal](x: Entity[S, A])(implicit N: spire.math.Numeric[A]) =
    Lenses._position.modify((p: Position[A]) => {
      val c: NonEmptyList[A] = p.pos.zip(p.boundary).map {
        case (p, b) =>
          if (b.contains(N.toDouble(p))) p else N.fromAlgebraic((b.upperValue + b.lowerValue) / 2)
      }
      Lenses._vector[A].set(c)(p)
    })(x)

  def wrap[S, A: scalaz.Equal](x: Entity[S, A])(implicit N: spire.math.Numeric[A]) =
    Lenses._position.modify((p: Position[A]) => {
      val c: NonEmptyList[A] = p.pos.zip(p.boundary).map {
        case (p, b) => {
          if (N.toDouble(p) < b.lowerValue) N.fromDouble(b.upperValue)
          else if (N.toDouble(p) > b.upperValue) N.fromDouble(b.lowerValue)
          else p
        }
      }
      Lenses._vector[A].set(c)(p)
    })(x)

  def absorb[S, A: scalaz.Equal](x: Entity[S, A])(implicit N: spire.math.Numeric[A],
                                                  V: HasVelocity[S, A]) = clamp(zeroedVelocity(x))

  def reflect[S, A: scalaz.Equal](x: Entity[S, A])(implicit N: spire.math.Numeric[A],
                                                   V: HasVelocity[S, A]) = clamp(reverseVelocity(x))
}
