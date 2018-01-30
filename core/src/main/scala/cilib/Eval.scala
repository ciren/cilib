package cilib

import scalaz.{NonEmptyList,\/}
import scalaz.Scalaz._

trait Input[F[_]] {
  def toInput[A](a: NonEmptyList[A]): String \/ F[A]
}

sealed abstract class Eval[F[_], A] {
  import Eval._

  val F: Input[F]

  def run: F[A] => String \/ Double

  def eval: RVar[NonEmptyList[A] => String \/ Objective[A]] =
    RVar.point { (fa: NonEmptyList[A]) => F.toInput(fa).flatMap { v =>
      this match {
        case Unconstrained(f, _) => f(v).map(fv => Single(Feasible(fv), List.empty))
        case Constrained(f, cs, _) =>
          cs.filter(c => !Constraint.satisfies(c, fa)) match {
            case Nil => f(v).map(fv => Single(Feasible(fv), List.empty))
            case xs  => f(v).map(fv => Single(Infeasible(fv, xs.length), xs))
          }
        }
      }
    }

  def constrain(cs: List[Constraint[A]]): Eval[F, A] =
    Constrained(run, cs, F)

  def unconstrain: Eval[F, A] =
    Unconstrained(run, F)
}

object Eval {
  private final case class Unconstrained[F[_], A](run: F[A] => String \/ Double, F: Input[F])
      extends Eval[F, A]
  private final case class Constrained[F[_], A](run: F[A] => String \/ Double,
                                                cs: List[Constraint[A]],
                                                F: Input[F])
      extends Eval[F, A]

  def unconstrained[F[_]: Input, A](f: F[A] => Double)(implicit F: Input[F]): Eval[F, A] =
    Unconstrained(f.map(_.right), F)

  def constrained[F[_]: Input, A](f: F[A] => Double, cs: List[Constraint[A]])(
      implicit F: Input[F]): Eval[F, A] =
    Constrained(f.map(_.right), cs, F)

  object mightFail {
    def unconstrained[F[_]: Input, A](f: F[A] => String \/ Double)(implicit F: Input[F]): Eval[F, A] =
      Unconstrained(f, F)
    def constrained[F[_]: Input, A](f: F[A] => String \/ Double, cs: List[Constraint[A]])(
        implicit F: Input[F]): Eval[F, A] =
      Constrained(f, cs, F)
  }
}

trait EvalInstances {
  import scalaz.{ICons, NonEmptyList}

  implicit val nelInput: Input[NonEmptyList] = new Input[NonEmptyList] {
    def toInput[A](a: NonEmptyList[A]): String \/ NonEmptyList[A] = a.right
  }

  implicit val pairInput: Input[Lambda[x => (x, x)]] =
    new Input[Lambda[x => (x, x)]] {
      def toInput[A](a: NonEmptyList[A]): String \/ (A, A) =
        a.list match {
          case ICons(a, ICons(b, _)) => (a, b).right
          case _                     => "Error producing a pair".left
        }
    }
}
