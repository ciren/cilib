package cilib

import scalaz.NonEmptyList

trait Input[F[_]] {
  def toInput[A](a: NonEmptyList[A]): F[A]
}

sealed abstract class Eval[F[_], A] {
  import Eval._

  val F: Input[F]

  def run: F[A] => Double

  lazy val eval: RVar[NonEmptyList[A] => Objective[A]] =
    RVar.pure { (fa: NonEmptyList[A]) =>
      this match {
        case Unconstrained(f, _) => Objective.single(Feasible(f(F.toInput(fa))), List.empty)
        case Constrained(f, cs, _) =>
          cs.filter(c => !Constraint.satisfies(c, fa)) match {
            case Nil => Objective.single(Feasible(f(F.toInput(fa))), List.empty)
            case xs  => Objective.single(Infeasible(f(F.toInput(fa))), xs)
          }
      }
    }

  def constrain(cs: List[Constraint[A]]): Eval[F, A] =
    Constrained(run, cs, F)

  def unconstrain: Eval[F, A] =
    Unconstrained(run, F)
}

object Eval {
  private final case class Unconstrained[F[_], A](run: F[A] => Double, F: Input[F])
      extends Eval[F, A]
  private final case class Constrained[F[_], A](run: F[A] => Double,
                                                cs: List[Constraint[A]],
                                                F: Input[F])
      extends Eval[F, A]

  def unconstrained[F[_], A](f: F[A] => Double)(implicit F: Input[F]): Eval[F, A] =
    Unconstrained(f, F)

  def constrained[F[_], A](f: F[A] => Double, cs: List[Constraint[A]])(
      implicit F: Input[F]): Eval[F, A] =
    Constrained(f, cs, F)
}

trait EvalInstances {
  import scalaz.{ICons, NonEmptyList}

  implicit val nelInput: Input[NonEmptyList] = new Input[NonEmptyList] {
    def toInput[A](a: NonEmptyList[A]): NonEmptyList[A] = a
  }

  implicit val pairInput: Input[Lambda[x => (x, x)]] =
    new Input[Lambda[x => (x, x)]] {
      def toInput[A](a: NonEmptyList[A]): (A, A) =
        a.list match {
          case ICons(a, ICons(b, _)) => (a, b)
          case _                     => sys.error("error producing a pair")
        }
    }
}
