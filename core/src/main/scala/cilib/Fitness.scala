package cilib

import scalaz._
import scalaz.Ordering._
import scalaz.std.anyVal._
import scalaz.syntax.equal._

sealed trait Fit {
  def fold[Z](penalty: Adjusted => Z, valid: Feasible => Z, infeasible: Infeasible => Z): Z =
    this match {
      case p @ Adjusted(_,_)   => penalty(p)
      case v @ Feasible(_)     => valid(v)
      case x @ Infeasible(_,_) => infeasible(x)
    }
}

final case class Feasible(v: Double) extends Fit
final case class Infeasible(v: Double, violations: Int) extends Fit {
  def adjust(f: Double => Double) =
    Adjusted(this, f(v))
}
final case class Adjusted private[cilib] (original: Infeasible, adjust: Double) extends Fit


@annotation.implicitNotFound("No instance of Fitness[${F},${A}] is available in current scope.")
trait Fitness[F[_],A] {
  def fitness(a: F[A]): Option[Objective[A]]
}

abstract class Comparison(val opt: Opt) {
  def apply[F[_],A](a: F[A], b: F[A])(implicit F: Fitness[F,A]): F[A]
}

object Comparison {

  def compare[F[_],A](x: F[A], y: F[A])(implicit F: Fitness[F,A]): Comparison => F[A] =
    o => o.apply(x, y)

  def dominance(o: Opt) = new Comparison(o) {
    def apply[F[_],A](a: F[A], b: F[A])(implicit F: Fitness[F,A]) = {
      def fromOrdering(f1: Objective[A], f2: Objective[A]): F[A] =
        o.order(f1, f2) match {
          case LT => b
          case GT => a
          case EQ => a // ??? No difference?
        }

      val result = for {
        f1 <- F.fitness(a)
        f2 <- F.fitness(b)
//        c1 <- f1.violations
//        c2 <- f2.violations
      } yield {
        val c1l = f1.violations.length
        val c2l = f2.violations.length

        // Both feasible (i.e: no constraint violations) compare Fit
        if (c1l == 0 && c2l == 0) { /*println("no constraints violated") ;*/ fromOrdering(f1, f2) }
        else if (c1l == 0) a
        else if (c2l == 0) b
        else if (c1l < c2l) a else if (c2l < c1l) b
        else fromOrdering(f1, f2)
      }

      result.getOrElse(a) // ???
    }
  }

  // Dominance is the generalised form of normal quality comparisons, taking constraint violations into account
  def quality(o: Opt) =
    dominance(o)

  def fittest[F[_],A](x: F[A], y: F[A])(implicit F: Fitness[F,A]): Comparison => Boolean =
    a => scalaz.std.option.optionOrder(a.opt.objectiveOrder[A]).order(F.fitness(x), F.fitness(y)) === GT

}

sealed trait Opt {
  def objectiveOrder[A]: Order[Objective[A]]

  def order[A](x: Objective[A], y: Objective[A]): Ordering =
    objectiveOrder[A].order(x, y)
}

final case object Min extends Opt {
  private val D = Order[Double].reverseOrder

  private def fitCompare(x: Fit, y: Fit) =
     (x, y) match {
       case (Adjusted(Infeasible(_,_), a), Adjusted(Infeasible(_,_), b)) => D.order(a, b)
       case (Adjusted(Infeasible(_,_), a), Feasible(b))   => D.order(a, b)
       case (Adjusted(Infeasible(_,_), _), Infeasible(_,_)) => GT
       case (Feasible(a), Adjusted(Infeasible(_, _), b)) => D.order(a, b)
       case (Feasible(_), Infeasible(_,_)) => GT
       case (Feasible(a), Feasible(b))   => { /*println("in feasible") ;*/ D.order(a, b) }
       case (Infeasible(_,_), Adjusted(_,_)) => LT
       case (Infeasible(_,_), Feasible(_))   => LT
       case (Infeasible(_,as), Infeasible(_,bs)) =>
         if (as < bs) LT else if (as > bs) GT else EQ
     }

  def objectiveOrder[A] = new Order[Objective[A]] {
    def order(x: Objective[A], y: Objective[A]) =
      (x, y) match {
        case (Single(f1,_), Single(f2,_)) => fitCompare(f1, f2)
        case (Multi(xs), Multi(ys)) =>
          val z = xs.zip(ys)
          val x = z.forall { case (a,b) =>
            val r = fitCompare(a.f, b.f)
            r == LT || r == EQ
          }
          val y = z.exists { case (a,b) => fitCompare(a.f, b.f) == LT }

          if (x && y) LT else if (x) EQ else GT

        case _ => sys.error("Cannot compare multiple objective against a single objective")
      }
  }
}

final case object Max extends Opt {
  private val D = Order[Double]
  protected def fitCompare(x: Fit, y: Fit) =
    (x, y) match {
      case (Adjusted(_, a), Adjusted(_, b)) => D.order(a, b)
      case (Adjusted(_, a), Feasible(b))   => D.order(a, b)
      case (Adjusted(_,_), Infeasible(_,_)) => GT
      case (Feasible(a), Adjusted(_, b)) => D.order(a, b)
      case (Feasible(a), Feasible(b))   => D.order(a, b)
      case (Feasible(_), Infeasible(_,_)) => GT
      case (Infeasible(_,_), Adjusted(_,_)) => LT
      case (Infeasible(_,_), Feasible(_))   => LT
      case (Infeasible(_, as), Infeasible(_, bs)) =>
        if (as < bs) GT else if (as > bs) LT else EQ
    }

  def objectiveOrder[A] = new Order[Objective[A]] {
    def order(x: Objective[A], y: Objective[A]) =
      (x, y) match {
        case (Single(f1, _), Single(f2, _)) => fitCompare(f1, f2)
        case (Multi(xs), Multi(ys)) =>
          val z = xs.zip(ys)
          val x = z.forall { case (a,b) =>
            val r = fitCompare(a.f, b.f)
            r == GT || r == EQ
          }
          val y = z.exists { case (a,b) => fitCompare(a.f, b.f) == GT }

          if (x && y) GT else if (x) EQ else LT

        case _ => sys.error("Cannot compare multiple objective against a single objective")
      }
  }

}
