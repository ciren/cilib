package cilib

import scalaz._
import Scalaz._

object Runner {

  // Fixme: need to unify these two functions into a single generic one -> the pattern is there
  def repeat[F[_], A, B](n: Int,
                         alg: Kleisli[Step[A, ?], F[B], F[B]],
                         collection: RVar[F[B]]): Step[A, F[B]] =
    Step
      .pointR(collection)
      .flatMap(coll =>
        (1 to n).toStream.foldLeftM[Step[A, ?], F[B]](coll) { (a, _) =>
          alg.run(a)
      })

  def repeatS[F[_], A, S, B](n: Int,
                             alg: Kleisli[StepS[A, S, ?], F[B], F[B]],
                             collection: RVar[F[B]]): StepS[A, S, F[B]] =
    StepS
      .pointR(collection)
      .flatMap(coll =>
        (1 to n).toStream.foldLeftM[StepS[A, S, ?], F[B]](coll) { (a, _) =>
          alg.run(a)
      })
}
