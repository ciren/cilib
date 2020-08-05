package cilib
package exec

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.collection._
import scalaz._, Scalaz._
import zio.stream._
import zio.{ Tag => _, _ }

final case class Algorithm[A](name: String Refined NonEmpty, value: A)
final case class Problem[A](name: String Refined NonEmpty, env: Env, eval: Eval[NonEmptyList, A])
final case class Progress[A] private (
  algorithm: String,
  problem: String,
  seed: Long,
  iteration: Int,
  env: Env,
  value: A
)

object Runner {

  trait IterationCount
  type PureStream[A] = Stream[Nothing, A]

  def repeat[M[_]: Monad, F[_], A](n: Int, alg: Kleisli[M, F[A], F[A]], collection: RVar[F[A]])(
    implicit M: MonadStep[M]
  ): M[F[A]] =
    M.liftR(collection)
      .flatMap(coll =>
        (1 to n).toList.foldLeftM[M, F[A]](coll) { (a, _) =>
          alg.run(a)
        }
      )

  def staticAlgorithm[M[_]: Monad, F[_], A](name: String Refined NonEmpty, a: Kleisli[M, F[A], F[A]]) =
    Stream.repeat(Algorithm(name, a))

  def algorithm[M[_]: Monad, F[_]: Foldable1, A, B](
    name: String Refined NonEmpty,
    config: A,
    f: A => Kleisli[M, F[B], F[B]],
    updater: (A, Int @@ IterationCount) => A
  ): PureStream[Algorithm[Kleisli[M, F[B], F[B]]]] = {

    def go(current: A, iteration: Int): PureStream[Algorithm[Kleisli[M, F[B], F[B]]]] = {
      val next = f(current)

      Stream.succeed(Algorithm(name, next)) ++
        go(updater(current, Tag[Int, IterationCount](iteration)), iteration + 1)
    }

    go(config, 1)
  }

  def staticProblem[S, A](
    name: String Refined NonEmpty,
    eval: Eval[NonEmptyList, A]
  ): PureStream[Problem[A]] =
    Stream.repeat(Problem(name, Unchanged, eval))

  def problem[S, A](name: String Refined NonEmpty, state: RVar[S], next: S => RVar[(S, Eval[NonEmptyList, A])])(
    env: EphemeralStream[Env],
    rng: RNG
  ): PureStream[Problem[A]] = {
    def go(s: S, c: Eval[NonEmptyList, A], e: EphemeralStream[Env], r: RNG): PureStream[Problem[A]] =
      EphemeralStream.##::.unapply(e) match {
        case Some((h, t)) =>
          h match {
            case Unchanged =>
              Stream.succeed(Problem(name, h, c)) ++ go(s, c, t, r)

            case Change =>
              val (rng2, (s1, c1)) = next(s).run(r)
              Stream.succeed(Problem(name, h, c1)) ++ go(s1, c1, t, rng2)
          }

        case None =>
          Stream.empty
      }

    val (rng2, (s2, e)) = state.flatMap(next).run(rng)
    go(s2, e, env, rng2)
  }

  /**
   *  Interpreter for algorithm execution
   */
  def foldStep[F[_], A, B](
    initialConfig: Environment[A],
    rng: RNG,
    collection: RVar[F[B]],
    alg: PureStream[Algorithm[Kleisli[Step[A, ?], F[B], F[B]]]],
    env: PureStream[Problem[A]],
    onChange: (F[B], Eval[NonEmptyList, A]) => RVar[F[B]]
  ): Stream[Exception, Progress[F[B]]] = {

    // Convert to a StepS with Unit as the state parameter
    val a: PureStream[Algorithm[Kleisli[StepS[A, Unit, ?], F[B], F[B]]]] =
      alg.map(x => x.copy(value = Kleisli((a: F[B]) => StepS.pointS(x.value.run(a)))))

    foldStepS(initialConfig, (), rng, collection, a, env, onChange)
      .map(x => x.copy(value = x.value._2))
  }

  def foldStepS[F[_], S, A, B](
    initialConfig: Environment[A],
    initialState: S,
    rng: RNG,
    collection: RVar[F[B]],
    alg: PureStream[Algorithm[Kleisli[StepS[A, S, ?], F[B], F[B]]]],
    env: PureStream[Problem[A]],
    onChange: (F[B], Eval[NonEmptyList, A]) => RVar[F[B]]
  ): Stream[Exception, Progress[(S, F[B])]] = {

    /*    def go(
      iteration: Int,
      r: RNG,
      current: F[B],
      config: Environment[A],
      state: S
    ): PureStream[Progress[(S, F[B])]] = //Tee[Problem[A], Algorithm[Kleisli[StepS[A, S, ?], F[B], F[B]]], Progress[(S, F[B])]] =
      Process.awaitL[Problem[A]].awaitOption.flatMap {
        case None => Process.halt
        case Some(Problem(problem, e, eval)) =>
          Process.awaitR[Algorithm[Kleisli[StepS[A, S, ?], F[B], F[B]]]].awaitOption.flatMap {
            case None => Process.halt
            case Some(algorithm) =>

          }
      }
     */

    val (rng2, current) = collection.run(rng) // the collection of entities

    final case class FoldState(iteration: Int, r: RNG, current: F[B], config: Environment[A], state: S)

    //env.tee(alg)(go(1, rng2, current, initialConfig, initialState))
    env
      .zipWith(alg)(Tuple2.apply)
      .mapAccumM(FoldState(1, rng2, current, initialConfig, initialState)) {
        case (FoldState(iteration, r, current, config, state), (Problem(problem, e, eval), algorithm)) =>
          val newConfig =
            e match {
              case Unchanged => config
              case Change    => Environment._eval[A].set(eval)(config)
            }

          val (r2, next) =
            e match {
              case Unchanged => algorithm.value.run(current).run(state).run(newConfig).run(r)
              case Change =>
                val (r3, updated) = onChange(current, newConfig.eval).run(r)
                algorithm.value.run(updated).run(state).run(newConfig).run(r3)
            }

          next match {
            case -\/(error) => ZIO.fail(error)
            case \/-((newState, value)) =>
              val progress =
                Progress(algorithm.name, problem, r2.seed, iteration, e, (newState, value))

              ZIO.succeed((FoldState(iteration + 1, r2, value, newConfig, newState), progress))
          }
      }
  }

  def measure[A, S, B](f: A => B): Progress[A] => Measurement[B] = {
    case Progress(algorithm, problem, seed, iteration, env, value) =>
      Measurement(algorithm, problem, iteration, env, seed, f(value))
  }

  def measureWithState[A, S, B](f: (S, A) => B): Progress[(S, A)] => Measurement[B] = {
    case Progress(algorithm, problem, seed, iteration, env, (state, value)) =>
      Measurement(algorithm, problem, iteration, env, seed, f(state, value))
  }
}
