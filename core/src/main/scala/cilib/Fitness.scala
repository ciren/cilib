package cilib

import zio.prelude._

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
      case x @ Feasible(v)   => Adjusted(Left(x), f(v))
      case x @ Infeasible(v) => Adjusted(Right(x), f(v))
    }
}

final case class Feasible(v: Double)                                                              extends Fit
final case class Infeasible(v: Double)                                                            extends Fit
final case class Adjusted private[cilib] (original: Either[Feasible, Infeasible], adjust: Double) extends Fit

@annotation.implicitNotFound("No instance of Fitness[${F},${A},${B}] is available in current scope.")
trait Fitness[F[_], A, B] {
  def fitness(a: F[A]): Option[Objective]
}

abstract class Comparison(val opt: Opt) {
  def apply[F[_], A, B](a: F[A], b: F[A])(implicit F: Fitness[F, A, B]): F[A]
}

object Comparison {

  def compare[F[_], A, B](x: F[A], y: F[A])(implicit F: Fitness[F, A, B]): Comparison => F[A] =
    _.apply(x, y)

  def fitter[F[_], A, B](x: F[A], y: F[A])(implicit F: Fitness[F, A, B]): Comparison => Boolean =
    compare(x, y).andThen(_ == x)

  def fittest[F[_], A, B](a: F[A], b: F[A])(implicit F: Fitness[F, A, B]): Step[F[A]] =
    Step.withCompare(comp => if (fitter(a, b).apply(comp)) a else b)

  def fitCompare(opt: Opt, x: Fit, y: Fit, xv: => Int, yv: => Int): Ordering =
    (x, y) match {
      case (Adjusted(_, a), Adjusted(_, b)) => opt.D.compare(a, b)
      case (Adjusted(_, a), Feasible(b))    => opt.D.compare(a, b)
      case (Adjusted(_, _), Infeasible(_))  => Ordering.GreaterThan
      case (Feasible(a), Adjusted(_, b))    => opt.D.compare(a, b)
      case (Feasible(a), Feasible(b))       => opt.D.compare(a, b)
      case (Feasible(_), Infeasible(_))     => Ordering.GreaterThan
      case (Infeasible(_), Adjusted(_, _))  => Ordering.LessThan
      case (Infeasible(_), Feasible(_))     => Ordering.LessThan
      case (Infeasible(a), Infeasible(b))   =>
        if (xv == yv) opt.D.compare(a, b)
        else opt.I.compare(xv, yv)
    }

  def multiFitCompare(opt: Opt, xs: List[Fit], ys: List[Fit], xsv: => Int, ysv: => Int): Ordering = {
    val z  = xs.zip(ys)
    val x2 = z.forall { case (a, b) =>
      val r = fitCompare(opt, a, b, xsv, ysv)
      r == Ordering.GreaterThan || r == Ordering.Equals
    }
    val y2 = z.exists { case (a, b) =>
      fitCompare(opt, a, b, xsv, ysv) == Ordering.GreaterThan
    }

    if (x2 && y2) Ordering.GreaterThan else if (x2) Ordering.Equals else Ordering.LessThan
  }

  def dominance(opt: Opt): Comparison = new Comparison(opt) {
    def apply[F[_], A, B](a: F[A], b: F[A])(implicit F: Fitness[F, A, B]) =
      (F.fitness(a), F.fitness(b)) match {
        case (Some(f1), Some(f2)) =>
          (f1.fitness, f2.fitness) match {
            case (Left(x), Left(y))   =>
              if (fitCompare(opt, x, y, f1.violationCount, f2.violationCount) === Ordering.GreaterThan) a else b
            case (Right(x), Right(y)) =>
              val r = multiFitCompare(opt, x, y, f1.violationCount, f2.violationCount)
              if (r != Ordering.LessThan) a else b
            case _                    => a
          }
        case (None, None)         => a
        case (None, _)            => b
        case (_, None)            => a
      }
  }

  // Dominance is the generalised form of normal quality comparisons, taking constraint violations into account
  def quality(o: Opt): Comparison =
    dominance(o)
}

sealed abstract class Opt {
  val I: Ord[Int] = Ord[Int].reverse

  def D: Ord[Double]
}

final case object Min extends Opt {
  val D: Ord[Double] = Ord[Double].reverse
}

final case object Max extends Opt {
  val D: Ord[Double] = Ord[Double]
}
