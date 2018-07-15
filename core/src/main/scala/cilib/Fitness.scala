package cilib

import scalaz._
import scalaz.Ordering._
import Scalaz._

sealed abstract class Fit {
  def fold[Z](penalty: Adjusted => Z, valid: Feasible => Z, infeasible: Infeasible => Z): Z =
    this match {
      case p @ Adjusted(_, _) => penalty(p)
      case v @ Feasible(_)    => valid(v)
      case x @ Infeasible(_)  => infeasible(x)
    }

  def adjust(f: Double => Double): Fit =
    this match {
      case Adjusted(_, _)    => this
      case x @ Feasible(v)   => Adjusted(\/.left(x), f(v))
      case x @ Infeasible(v) => Adjusted(\/.right(x), f(v))
    }
}

final case class Feasible(v: Double) extends Fit
final case class Infeasible(v: Double /*, violations: Int*/ ) extends Fit
final case class Adjusted private[cilib] (original: Feasible \/ Infeasible, adjust: Double)
    extends Fit
@annotation.implicitNotFound(
  "No instance of Fitness[${F},${A},${B}] is available in current scope.")
trait Fitness[F[_], A, B] {
  def fitness(a: F[A]): Option[Objective[B]]
}

abstract class Comparison(val opt: Opt) {
  def apply[F[_], A, B](a: F[A], b: F[A])(implicit F: Fitness[F, A, B]): F[A]
}

object Comparison {

  def compare[F[_], A](x: F[A], y: F[A])(implicit F: Fitness[F, A, A]): Comparison => F[A] =
    _.apply(x, y)

  def dominance(o: Opt) = new Comparison(o) {
    def apply[F[_], A, B](a: F[A], b: F[A])(implicit F: Fitness[F, A, B]) = {
      def fromOrdering(f1: Objective[B], f2: Objective[B]): F[A] =
        o.order(f1, f2) match {
          case LT => b
          case GT => a
          case EQ => a // ??? No difference?
        }

      (F.fitness(a), F.fitness(b)) match {
        case (Some(f1), Some(f2)) => fromOrdering(f1, f2)
        case (None, None)         => a
        case (None, _)            => b
        case (_, None)            => a
      }
    }
  }

  // Dominance is the generalised form of normal quality comparisons, taking constraint violations into account
  def quality(o: Opt) =
    dominance(o)

  def fitter[F[_], A, B](x: F[A], y: F[A])(implicit F: Fitness[F, A, B]): Comparison => Boolean =
    cmp =>
      (F.fitness(x), F.fitness(y)) match {
        case (Some(a), Some(b)) => cmp.opt.objectiveOrder[B].order(a, b) === GT
        case (None, None)       => false
        case (None, Some(_))    => false
        case (Some(_), None)    => true
    }

  def fittest[F[_], A](a: F[A], b: F[A])(implicit F: Fitness[F, A, A]): Step[A, F[A]] =
    Step.withCompareR(comp =>
      RVar.pure {
        if (fitter(a, b).apply(comp)) a else b
    })

}

sealed trait Opt {
  def objectiveOrder[A]: Order[Objective[A]]

  final def order[A](x: Objective[A], y: Objective[A]): Ordering =
    objectiveOrder[A].order(x, y)
}

final case object Min extends Opt {
  private val D = Order[Double].reverseOrder
  private val I = Order[Int].reverseOrder

  private def fitCompare(x: Fit, y: Fit, xv: => Int, yv: => Int) =
    (x, y) match {
      case (Adjusted(_, a), Adjusted(_, b)) => D.order(a, b)
      case (Adjusted(_, a), Feasible(b))    => D.order(a, b)
      case (Adjusted(_, _), Infeasible(_))  => GT
      case (Feasible(a), Adjusted(_, b))    => D.order(a, b)
      case (Feasible(a), Feasible(b))       => D.order(a, b)
      case (Feasible(_), Infeasible(_))     => GT
      case (Infeasible(_), Adjusted(_, _))  => LT
      case (Infeasible(_), Feasible(_))     => LT
      case (Infeasible(a), Infeasible(b)) =>
        if (xv == yv) D.order(a, b)
        else I.order(xv, yv)
    }

  def objectiveOrder[A] = new Order[Objective[A]] {
    def order(x: Objective[A], y: Objective[A]) =
      (x.fitness, y.fitness) match {
        case (-\/(f1), -\/(f2)) => fitCompare(f1, f2, x.violationCount, y.violationCount)
        case (\/-(xs), \/-(ys)) =>
          val z = xs.zip(ys)
          val x2 = z.forall {
            case (a, b) =>
              val r = fitCompare(a, b, x.violationCount, y.violationCount)
              r == LT || r == EQ
          }
          val y2 = z.exists {
            case (a, b) => fitCompare(a, b, x.violationCount, y.violationCount) == LT
          }

          if (x2 && y2) LT else if (x2) EQ else GT

        case _ => EQ
      }
  }
}

final case object Max extends Opt {
  private val D = Order[Double]
  private val I = Order[Int]

  protected def fitCompare(x: Fit, y: Fit, xv: => Int, yv: => Int) =
    (x, y) match {
      case (Adjusted(_, a), Adjusted(_, b)) => D.order(a, b)
      case (Adjusted(_, a), Feasible(b))    => D.order(a, b)
      case (Adjusted(_, _), Infeasible(_))  => GT
      case (Feasible(a), Adjusted(_, b))    => D.order(a, b)
      case (Feasible(a), Feasible(b))       => D.order(a, b)
      case (Feasible(_), Infeasible(_))     => GT
      case (Infeasible(_), Adjusted(_, _))  => LT
      case (Infeasible(_), Feasible(_))     => LT
      case (Infeasible(a), Infeasible(b)) =>
        if (xv == yv) D.order(a, b)
        else I.order(xv, yv)
    }

  def objectiveOrder[A] = new Order[Objective[A]] {
    def order(x: Objective[A], y: Objective[A]) =
      (x.fitness, y.fitness) match {
        case (-\/(f1), -\/(f2)) => fitCompare(f1, f2, x.violationCount, y.violationCount)
        case (\/-(xs), \/-(ys)) =>
          val z = xs.zip(ys)
          val x2 = z.forall {
            case (a, b) =>
              val r = fitCompare(a, b, x.violationCount, y.violationCount)
              r == GT || r == EQ
          }
          val y2 = z.exists {
            case (a, b) => fitCompare(a, b, x.violationCount, y.violationCount) == GT
          }

          if (x2 && y2) GT else if (x2) EQ else LT

        case _ => EQ
      }
  }

}
