package cilib
package exec

import zio._
import zio.prelude._
import zio.stream._


final case class Algorithm[A](name: Name, value: A)
final case class Problem(name: Name, env: Env, eval: Eval[NonEmptyVector])
final case class Progress[A] private (
  algorithm: Name,
  problem: Name,
  seed: Long,
  iteration: Int,
  env: Env,
  value: A
)

object Runner {

  object IterationCount extends Newtype[Int]
  type IterationCount = IterationCount.Type

  // Should this be a compile-time macro????
  def toName(n: String): Name =
    Name.make(n) match {
      case ZValidation.Failure(_, error) => sys.error(error.toString)
      case ZValidation.Success(_, value) => value
    }

  /**
   * Define a stream of algorithm where the algorithm remains unchanged.
   *
   * @param name Identifier for the algorithm
   * @param a Kleilsi representation of the algorithm instance
   * @return
   */
  def staticAlgorithm[M[+_]: IdentityFlatten: Covariant, F[_], A](
    name: String,
    a: Kleisli[M, F[A], F[A]]
  ) =
    Stream.repeat(Algorithm(toName(name), a))

  /**
   * @param name
   * @param config
   * @param f
   * @param updater
   * @return
   */
  def algorithm[M[+_]: IdentityFlatten: Covariant, F[+_]: NonEmptyForEach, A, B](
    name: String,
    config: A,
    f: A => Kleisli[M, F[B], F[B]],
    updater: (A, IterationCount) => A
  ): UStream[Algorithm[Kleisli[M, F[B], F[B]]]] = {

    def go(current: A, iteration: Int): UStream[Algorithm[Kleisli[M, F[B], F[B]]]] = {
      val next = f(current)

      Stream.succeed(Algorithm(toName(name), next)) ++
        go(updater(current, IterationCount(iteration)), iteration + 1)
    }

    go(config, 1)
  }

  /**
   * @param name
   * @param eval
   * @return
   */
  def staticProblem[S, A](
    name: String,
    eval: Eval[NonEmptyVector]
  ): UStream[Problem] =
    Stream.repeat(Problem(toName(name), Unchanged, eval))

  /**
   * @param name
   * @param state
   * @param next
   * @param env
   * @param rng
   * @return
   */
  def problem[S, A](name: String, state: RVar[S], next: S => RVar[(S, Eval[NonEmptyVector])])(
    env: UStream[Env],
    rng: RNG
  ): UStream[Problem] = {
    val n = toName(name)
    val (rng2, (s2, e)) = state.flatMap(next).run(rng)

    def go2: ((S, Eval[NonEmptyVector], RNG), Env) => ((S, Eval[NonEmptyVector], RNG), Problem) =
      (s: (S, Eval[NonEmptyVector], RNG), e: Env) => {
        val (state, eval, r) = s

        e match {
          case Unchanged =>
            (s, Problem(n, e, eval))

          case Change =>
            val (rng2, (s1, c1)) = next(state).run(r)
            ((s1, c1, rng2), Problem(n, e, c1))
        }
      }

    env.mapAccum((s2, e, rng2))(go2)
  }

  /**
   *  Interpreter for algorithm execution
   */
  def foldStep[F[_], A, B](
    initialConfig: cilib.Comparison,
    rng: RNG,
    collection: RVar[F[B]],
    alg: UStream[Algorithm[Kleisli[Step[*], F[B], F[B]]]],
    env: UStream[Problem],
    onChange: (F[B], Eval[NonEmptyVector]) => RVar[F[B]]
  ): Stream[Exception, Progress[F[B]]] = {

    // Convert to a StepS with Unit as the state parameter
    val a: UStream[Algorithm[Kleisli[StepS[Unit, *], F[B], F[B]]]] =
      alg.map(x => x.copy(value = Kleisli((a: F[B]) => StepS.liftStep(x.value.run(a)))))

    foldStepS(initialConfig, (), rng, collection, a, env, onChange)
      .map(x => x.copy(value = x.value._2))
  }

  def foldStepS[F[_], S, A, B](
    initialConfig: cilib.Comparison,
    initialState: S,
    rng: RNG,
    collection: RVar[F[B]],
    alg: UStream[Algorithm[Kleisli[StepS[S, *], F[B], F[B]]]],
    env: UStream[Problem],
    onChange: (F[B], Eval[NonEmptyVector]) => RVar[F[B]]
  ): Stream[Exception, Progress[(S, F[B])]] = {

    val (rng2, current) = collection.run(rng) // the collection of entities

    final case class FoldState(iteration: Int, r: RNG, current: F[B], config: cilib.Comparison, state: S)

    env
      .zipWith(alg)(Tuple2.apply)
      .mapAccumM(FoldState(1, rng2, current, initialConfig, initialState)) {
        case (FoldState(iteration, r, current, config, state), (Problem(problem, e, eval), algorithm)) =>
          val (_, result) =
            e match {
              case Unchanged => algorithm.value.run(current).provide((config, eval)).runAll((r, state))
              case Change    =>
                val (r3, updated) = onChange(current, eval).run(r)
                algorithm.value.run(updated).provide((config, eval)).runAll((r3, state))
            }

          result match {
            case Left(error)                    => sys.error(error.toString())
            case Right(((r2, newState), value)) =>
              val progress =
                Progress(algorithm.name, problem, r2.seed, iteration, e, (newState, value))

              ZIO.succeed((FoldState(iteration + 1, r2, value, config, newState), progress))
          }
      }
  }

  def measure[A, B](f: A => B): Progress[A] => Measurement[B] = {
    case Progress(algorithm, problem, seed, iteration, env, value) =>
      Measurement(algorithm, problem, iteration, env, seed, f(value))
  }

  def measureWithState[A, S, B](f: (S, A) => B): Progress[(S, A)] => Measurement[B] = {
    case Progress(algorithm, problem, seed, iteration, env, (state, value)) =>
      Measurement(algorithm, problem, iteration, env, seed, f(state, value))
  }

  def repeat[M[+_]: IdentityFlatten: Covariant, F[+_], A](n: Int, alg: F[A] => M[F[A]], collection: RVar[F[A]])(implicit
    M: MonadStep[M]
  ): M[F[A]] =
    M.liftR(collection)
      .flatMap(coll =>
        (1 to n).toList.foldLeftM(coll) { (a, _) =>
          alg.apply(a)
        }
      )

}
