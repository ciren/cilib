package cilib

import scalaz.NonEmptyList
import spire.implicits._

trait Input[F[_]] {
  def toInput[A](a: NonEmptyList[A]): F[A]
}

sealed abstract class Eval[F[_],A] {
//  private implicit val A = implicitly[spire.algebra.Eq[Double]]

  import Eval._

  def run: F[A] => Double

  def eval(implicit F: Input[F]): RVar[NonEmptyList[A] => Objective[A]] =
    RVar.point { (fa: NonEmptyList[A]) =>
      this match {
        case Unconstrained(f)   => Single(Feasible(f(F.toInput(fa))), List.empty)
        case Constrained(f, cs) =>
          cs.filter(c => !Constraint.satisfies(c, fa)) match {
            case Nil => Single(Feasible(f(F.toInput(fa))), List.empty)
            case xs  => Single(Infeasible(f(F.toInput(fa)), xs.length), xs)
          }
      }
    }

  def constrain(cs: List[Constraint[A]]): Eval[F,A] =
    Constrained(run, cs)

  def unconstrain: Eval[F,A] =
    Unconstrained(run)
}

object Eval {
  private final case class Unconstrained[F[_],A](run: F[A] => Double) extends Eval[F,A]
  private final case class Constrained[F[_],A](run: F[A] => Double, cs: List[Constraint[A]]) extends Eval[F,A]

  def unconstrained[F[_],A](f: F[A] => Double): Eval[F,A] = Unconstrained(f)
  def constrained[F[_],A](f: F[A] => Double, cs: List[Constraint[A]]): Eval[F,A] = Constrained(f, cs)

  // def unconstrained[F[_], A](f: F[A] => Double)(implicit F: Input[F]): RVar[NonEmptyList[A] => Objective[A]] =
  //   RVar.point {
  //     (fa: NonEmptyList[A]) => Single(Feasible(f(F.toInput(fa))), List.empty)
  //   }

  // def constrain[F[_], A](f: F[A] => Double, cs: List[Constraint[A,Double]])(implicit F: Input[F], A: spire.algebra.Eq[Double]): RVar[NonEmptyList[A] => Objective[A]] = // indicate that this is already constrained?
  //   RVar.point {
  //     (fa: NonEmptyList[A]) =>
  //       cs.filter(c => !Constraint.satisfies(c, fa)) match {
  //         case Nil => Single(Feasible(f(F.toInput(fa))), List.empty)
  //         case xs  => Single(Infeasible(f(F.toInput(fa)), xs.length), xs)
  //       }
  //   }


  // def unconstrained[F[_]:Input, A](f: F[A] => Double) =
  //   unconstrainedNamed(f, "unnamed")

  // def unconstrainedNamed[F[_],A](f: F[A] => RVar[Double], name: String)(implicit F: Input[F]): Eval[A] =
  //   new Eval[A] {
  //     def eval(a: NonEmptyList[A]): RVar[Objective[A]] =
  //       f(F.toInput(a)).map(x => Feasible(x), List.empty)
  //     // RVar.point {
  //     //   Single(Feasible(f(F.toInput(a))), List.empty)
  //     // }

  //     def unconstrain = this

  //     override def toString = "Unconstrained(" + name + ")"
  //   }

  // def constrained[F[_]:Input, A](f: F[A] => Double, cs: List[Constraint[A,Double]]): Eval[A] =
  //   constrainedNamed(f, cs, "unnamed")

  // def constrainedNamed[F[_],A](f: F[A] => Double, cs: List[Constraint[A,Double]], name: String)(implicit F: Input[F]): Eval[A] =
  //   new Eval[A] {
  //     import spire.algebra.Eq
  //     import spire.implicits._

  //     def eval(a: NonEmptyList[A]): RVar[Objective[A]] = RVar.point {
  //       cs.filter(c => !Constraint.satisfies(c, a)) match {
  //         case Nil => Single(Feasible(f(F.toInput(a))), List.empty)
  //         case xs  => Single(Infeasible(f(F.toInput(a)), xs.length), xs)
  //       }
  //     }

  //     def constrainBy(css: List[Constraint[A,Double]]): Eval[A] =
  //       constrainedNamed(f, css, name)

  //     def unconstrain =
  //       unconstrainedNamed(f, name)

  //     override def toString = "Constrained(" + name + ")"
  //   }

}

trait EvalInstances {
  import scalaz.{ICons, NonEmptyList}

  implicit val nelInput = new Input[NonEmptyList] {
    def toInput[A](a: NonEmptyList[A]): NonEmptyList[A] = a
  }

  implicit def pairInput = new Input[Lambda[x => (x, x)]] {
    def toInput[A](a: NonEmptyList[A]): (A,A) =
      a.list match {
        case ICons(a, ICons(b, _)) => (a, b)
        case _ => sys.error("error producing a pair")
      }
  }
}
