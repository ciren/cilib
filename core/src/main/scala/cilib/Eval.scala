package cilib

trait Input[F[_]] {
  def toInput[A](a: NonEmptyVector[A]): F[A]
}

sealed abstract class Eval[F[_]] {
  def eval: RVar[NonEmptyVector[Any] => Objective] =
    this match {
      case a @ Eval.Unconstrained(_, _)  => a.eval2
      case b @ Eval.Constrained(_, _, _) => b.eval2
    }

  def constrain(cs: List[Constraint]): Eval[F]

  def unconstrain: Eval[F]

}

object Eval {
  private final case class Unconstrained[F[_], A](f: F[A] => Fit, F: Input[F]) extends Eval[F] {
    def eval2: RVar[NonEmptyVector[A] => Objective] =
      RVar.pure { (fa: NonEmptyVector[A]) =>
        Objective.single(f(F.toInput(fa)), List.empty)
      }

    def constrain(cs: List[Constraint]): Eval[F] =
      Eval.Constrained(f, cs, F)

    def unconstrain: Eval[F] =
      Eval.Unconstrained(f, F)
  }

  private final case class Constrained[F[_], A](run: F[A] => Fit, cs: List[Constraint], F: Input[F]) extends Eval[F] {
    def eval2: RVar[NonEmptyVector[A] => Objective] =
      RVar.pure { (fa: NonEmptyVector[A]) =>
        cs.filter(c => !Constraint.satisfies(c, fa)) match {
          case Nil => Objective.single(run(F.toInput(fa)), List.empty)
          case xs =>
            val result =
              run(F.toInput(fa)) match {
                case Feasible(v)       => Infeasible(v)
                case Adjusted(_, a)    => Infeasible(a)
                case i @ Infeasible(_) => i
              }

            Objective.single[A](result, xs)
        }
      }

    def constrain(cs: List[Constraint]): Eval[F] =
      Eval.Constrained(run, cs, F)

    def unconstrain: Eval[F] =
      Eval.Unconstrained(run, F)
  }

  def unconstrained[F[_], A](f: F[A] => Fit)(implicit F: Input[F]): Eval[F] =
    Unconstrained(f, F)

  def constrained[F[_], A](f: F[A] => Fit, cs: List[Constraint])(implicit F: Input[F]): Eval[F] =
    Constrained(f, cs, F)
}

trait EvalInstances {

  implicit val nonEmptyListInput: Input[zio.prelude.NonEmptyList] =
    new Input[zio.prelude.NonEmptyList] {
      def toInput[A](a: NonEmptyVector[A]): zio.prelude.NonEmptyList[A] =
        // Safe as there _will always be_ at least 1 element
        zio.prelude.NonEmptyList.fromIterableOption(a.toChunk.toIterable).get
    }

  implicit val nonEmptyVectorInput: Input[NonEmptyVector] = new Input[NonEmptyVector] {
    def toInput[A](a: NonEmptyVector[A]): NonEmptyVector[A] = a
  }

  implicit val pairInput: Input[Lambda[x => (x, x)]] =
    new Input[Lambda[x => (x, x)]] {
      def toInput[A](a: NonEmptyVector[A]): (A, A) = {
        val grouped = a.toChunk.toList.grouped(2)
        if (grouped.hasNext) {
          grouped.next().toList match {
            case a :: b :: _ => (a, b)
            case _           => sys.error("error producing a pair")
          }
        } else sys.error("Too few elements provided. Need at least 2.")
      }
    }
}
