package cilib

trait Input[F[_]] {
  def toInput[A](a: NonEmptyVector[A]): F[A]
}

sealed abstract class Eval[F[_]] {
  def eval(v: Type[NonEmptyVector]): RVar[Objective]

  def constrain(cs: List[Constraint]): Eval[F]

  def unconstrain: Eval[F]
}

object Eval {

  def unconstrained[F[_], A](f: F[A] => Fit)(implicit F: Input[F]): Eval[F] =
    new Eval[F] {
      def eval(fa: Type[NonEmptyVector]): RVar[Objective] =
        RVar.pure {
          Objective.single(f(F.toInput(fa.asInstanceOf[NonEmptyVector[A]])), List.empty)
        }

      def constrain(cs: List[Constraint]): Eval[F] =
        constrained(f, cs)(F)

      def unconstrain: Eval[F] =
        this
    }

  def constrained[F[_], A](f: F[A] => Fit, cs: List[Constraint])(implicit F: Input[F]): Eval[F] =
    new Eval[F] {
      def eval(fa: Type[NonEmptyVector]): RVar[Objective] = {
        val input = F.toInput(fa.asInstanceOf[NonEmptyVector[A]])
        cs.filter(c => !Constraint.satisfies(c, fa)) match {
          case Nil => RVar.pure(Objective.single(f(input), List.empty))
          case xs  =>
            val result =
              f(input) match {
                case Fit.Feasible(v)       => Fit.Infeasible(v)
                case Fit.Adjusted(_, a)    => Fit.Infeasible(a)
                case i @ Fit.Infeasible(_) => i
              }

            RVar.pure(Objective.single[A](result, xs))
        }
      }

      def constrain(cs: List[Constraint]): Eval[F] =
        constrained(f, cs)(F)

      def unconstrain: Eval[F] =
        unconstrained(f)(F)
    }

  def unconstrainedR[F[_], A](f: F[A] => RVar[Fit])(implicit F: Input[F]): Eval[F] =
    new Eval[F] {
      def eval(fa: Type[NonEmptyVector]): RVar[Objective] =
        f(F.toInput(fa.asInstanceOf[NonEmptyVector[A]])).map(x => Objective.single(x, List.empty))

      def constrain(cs: List[Constraint]): Eval[F] =
        constrainedR(f, cs)(F)

      def unconstrain: Eval[F] =
        this
    }

  def constrainedR[F[_], A](f: F[A] => RVar[Fit], cs: List[Constraint])(implicit F: Input[F]): Eval[F] =
    new Eval[F] {
      def eval(fa: Type[NonEmptyVector]): RVar[Objective] =
        f(F.toInput(fa.asInstanceOf[NonEmptyVector[A]])).map { x =>
          cs.filter(c => !Constraint.satisfies(c, fa)) match {
            case Nil => Objective.single(x, List.empty)
            case xs  =>
              val result =
                x match {
                  case Fit.Feasible(v)       => Fit.Infeasible(v)
                  case Fit.Adjusted(_, a)    => Fit.Infeasible(a)
                  case i @ Fit.Infeasible(_) => i
                }

              Objective.single[A](result, xs)
          }
        }

      def constrain(cs: List[Constraint]): Eval[F] =
        constrainedR(f, cs)(F)

      def unconstrain: Eval[F] =
        unconstrainedR(f)(F)
    }
}
