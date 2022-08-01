package cilib

trait Input[F[_]] {
  def toInput[A](a: NonEmptyVector[A]): F[A]
}

sealed abstract class Eval[F[_]] {
  def eval[A](v: NonEmptyVector[A]): RVar[Objective] =
    this match {
      case a @ Eval.Unconstrained(_, _)   => a.eval2(v)
      case b @ Eval.Constrained(_, _, _)  => b.eval2(v)
      case c @ Eval.UnconstrainedR(_, _)  => c.eval2(v)
      case d @ Eval.ConstrainedR(_, _, _) => d.eval2(v)
    }

  def constrain(cs: List[Constraint]): Eval[F]

  def unconstrain: Eval[F]
}

object Eval {

  def unconstrained[F[_], A](f: F[A] => Fit)(implicit F: Input[F]): Eval[F] =
    Unconstrained(f, F)

  def constrained[F[_], A](f: F[A] => Fit, cs: List[Constraint])(implicit F: Input[F]): Eval[F] =
    Constrained(f, cs, F)

  def unconstrainedR[F[_], A](f: F[A] => RVar[Fit])(implicit F: Input[F]): Eval[F] =
    UnconstrainedR(f, F)

  def constrainedR[F[_], A](f: F[A] => RVar[Fit], cs: List[Constraint])(implicit F: Input[F]): Eval[F] =
    ConstrainedR(f, cs, F)

  private final case class Unconstrained[F[_], A](f: F[A] => Fit, F: Input[F]) extends Eval[F] {
    def eval2(fa: NonEmptyVector[A]): RVar[Objective] =
      RVar.pure {
        Objective.single(f(F.toInput(fa)), List.empty)
      }

    def constrain(cs: List[Constraint]): Eval[F] =
      Eval.Constrained(f, cs, F)

    def unconstrain: Eval[F] =
      Eval.Unconstrained(f, F)
  }

  private final case class Constrained[F[_], A](run: F[A] => Fit, cs: List[Constraint], F: Input[F]) extends Eval[F] {
    def eval2(fa: NonEmptyVector[A]): RVar[Objective] =
      cs.filter(c => !Constraint.satisfies(c, fa)) match {
        case Nil => RVar.pure(Objective.single(run(F.toInput(fa)), List.empty))
        case xs  =>
          val result =
            run(F.toInput(fa)) match {
              case Feasible(v)       => Infeasible(v)
              case Adjusted(_, a)    => Infeasible(a)
              case i @ Infeasible(_) => i
            }

          RVar.pure(Objective.single[A](result, xs))
      }

    def constrain(cs: List[Constraint]): Eval[F] =
      Eval.Constrained(run, cs, F)

    def unconstrain: Eval[F] =
      Eval.Unconstrained(run, F)
  }

  private final case class UnconstrainedR[F[_], A](f: F[A] => RVar[Fit], F: Input[F]) extends Eval[F] {
    def eval2(fa: NonEmptyVector[A]): RVar[Objective] =
      f(F.toInput(fa)).map(x => Objective.single(x, List.empty))

    def constrain(cs: List[Constraint]): Eval[F] =
      Eval.ConstrainedR(f, cs, F)

    def unconstrain: Eval[F] =
      Eval.UnconstrainedR(f, F)
  }

  private final case class ConstrainedR[F[_], A](f: F[A] => RVar[Fit], cs: List[Constraint], F: Input[F])
      extends Eval[F] {
    def eval2(fa: NonEmptyVector[A]): RVar[Objective] =
      f(F.toInput(fa)).map { x =>
        cs.filter(c => !Constraint.satisfies(c, fa)) match {
          case Nil => Objective.single(x, List.empty)
          case xs  =>
            val result =
              x match {
                case Feasible(v)       => Infeasible(v)
                case Adjusted(_, a)    => Infeasible(a)
                case i @ Infeasible(_) => i
              }

            Objective.single[A](result, xs)
        }
      }

    def constrain(cs: List[Constraint]): Eval[F] =
      Eval.ConstrainedR(f, cs, F)

    def unconstrain: Eval[F] =
      Eval.UnconstrainedR(f, F)
  }
}
