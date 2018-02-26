package cilib
package exec

import scalaz._
import Scalaz._

import scalaz.stream._
import scalaz.concurrent.Task

object Runner {

  def repeat[M[_]: Monad, F[_], A](n: Int, alg: Kleisli[M, F[A], F[A]], collection: RVar[F[A]])(
      implicit M: MonadStep[M]): M[F[A]] =
    M.pointR(collection)
      .flatMap(coll =>
        (1 to n).toStream.foldLeftM[M, F[A]](coll) { (a, _) =>
          alg.run(a)
      })

  def staticProblem[S, A](
      //eval: RVar[Eval[NonEmptyList, Double]],
      eval: RVar[NonEmptyList[A] => Objective[A]],
      rng: RNG
  ): Process[Task, (Env, NonEmptyList[A] => Objective[A])] = {
    val (_, e) = eval.run(rng)
    Process.constant((Unchanged, e))
  }

  def problem[S, A](state: RVar[S], next: S => RVar[(S, Eval[NonEmptyList, A])])(
      env: Stream[Env],
      rng: RNG
  ): Process[Task, (Env, Eval[NonEmptyList, A])] = {
    def go(s: S,
           c: Eval[NonEmptyList, A],
           e: Stream[Env],
           r: RNG): Process[Task, (Env, Eval[NonEmptyList, A])] =
      e match {
        case Stream.Empty => Process.empty
        case h #:: t =>
          h match {
            case Unchanged =>
              Process.emit((h, c)) ++ go(s, c, t, r)

            case Change =>
              val (rng2, (s1, c1)) = next(s).run(r)
              Process.emit((h, c1)) ++ go(s1, c1, t, rng2)
          }
      }

    val (rng2, (s2, e)) = state.flatMap(next).run(rng)
    go(s2, e, env, rng2)
  }

  def foldStep[F[_], A, B](initalConfig: Environment[A],
                           rng: RNG,
                           collection: RVar[F[B]],
                           alg: Kleisli[Step[A, ?], F[B], F[B]],
                           env: Process[Task, (Env, NonEmptyList[A] => Objective[A])],
                           onChange: F[B] => RVar[F[B]]): Process[Task, (Env, F[B])] = {

    def go(r: RNG, current: F[B], config: Environment[A])
      : Tee[(Env, NonEmptyList[A] => Objective[A]), Kleisli[Step[A, ?], F[B], F[B]], (Env, F[B])] =
      Process.awaitL[(Env, NonEmptyList[A] => Objective[A])].awaitOption.flatMap {
        case None => Process.halt
        case Some((e, eval)) =>
          Process.awaitR[Kleisli[Step[A, ?], F[B], F[B]]].awaitOption.flatMap {
            case None => Process.halt
            case Some(algorithm) =>
              val newConfig: Environment[A] =
                e match {
                  case Unchanged => config
                  case Change    => config.copy(eval = RVar.point(eval))
                }

              val (r2, next) = algorithm.run(current).run(newConfig).run(r)

              next match {
                case -\/(error) => Process.fail(error)
                case \/-(value) => Process.emit((e, value)) ++ go(r2, value, newConfig)
              }
          }
      }

    val (rng2, current) = collection.run(rng) // the collection of entities

    env.tee(Process.constant(alg))(go(rng2, current, initalConfig))
  }

}
