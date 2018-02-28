package cilib
package exec

import scalaz._
import Scalaz._

import scalaz.stream._
import scalaz.concurrent.Task

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.collection._

trait Output
final case class Algorithm[A](name: String Refined NonEmpty, value: A)
final case class Problem[A](name: String Refined NonEmpty,
                            env: Env,
                            eval: RVar[NonEmptyList[A] => Objective[A]])
final case class Progress[A] private (algorithm: String,
                                      problem: String,
                                      seed: Long,
                                      iteration: Int,
                                      env: Env,
                                      value: A)

object Runner {

  def repeat[M[_]: Monad, F[_], A](n: Int, alg: Kleisli[M, F[A], F[A]], collection: RVar[F[A]])(
      implicit M: MonadStep[M]): M[F[A]] =
    M.pointR(collection)
      .flatMap(coll =>
        (1 to n).toStream.foldLeftM[M, F[A]](coll) { (a, _) =>
          alg.run(a)
      })

  def staticProblem[S, A](
      name: String Refined NonEmpty,
      eval: RVar[NonEmptyList[A] => Objective[A]],
      rng: RNG
  ): Process[Task, Problem[A]] = {
    val (_, e) = eval.run(rng)
    Process.constant(Problem(name, Unchanged, RVar.point(e)))
  }

  def problem[S, A](name: String Refined NonEmpty,
                    state: RVar[S],
                    next: S => RVar[(S, Eval[NonEmptyList, A])])(
      env: Stream[Env],
      rng: RNG
  ): Process[Task, Problem[A]] = {
    def go(s: S, c: Eval[NonEmptyList, A], e: Stream[Env], r: RNG): Process[Task, Problem[A]] =
      e match {
        case Stream.Empty => Process.empty
        case h #:: t =>
          h match {
            case Unchanged =>
              Process.emit(Problem(name, h, c.eval)) ++ go(s, c, t, r)

            case Change =>
              val (rng2, (s1, c1)) = next(s).run(r)
              Process.emit(Problem(name, h, c1.eval)) ++ go(s1, c1, t, rng2)
          }
      }

    val (rng2, (s2, e)) = state.flatMap(next).run(rng)
    go(s2, e, env, rng2)
  }

  /**
    *  Interpreter for algorithm execution
    */
  def foldStep[F[_], A, B](initalConfig: Environment[A],
                           rng: RNG,
                           collection: RVar[F[B]],
                           alg: Algorithm[Kleisli[Step[A, ?], F[B], F[B]]], // Simplify?
                           env: Process[Task, Problem[A]],
                           onChange: F[B] => RVar[F[B]]): Process[Task, Progress[F[B]]] = {

    def go(iteration: Int, r: RNG, current: F[B], config: Environment[A])
      : Tee[Problem[A], Algorithm[Kleisli[Step[A, ?], F[B], F[B]]], Progress[F[B]]] =
      Process.awaitL[Problem[A]].awaitOption.flatMap {
        case None => Process.halt
        case Some(Problem(problem, e, eval)) =>
          Process.awaitR[Algorithm[Kleisli[Step[A, ?], F[B], F[B]]]].awaitOption.flatMap {
            case None => Process.halt
            case Some(algorithm) =>
              val newConfig: Environment[A] =
                e match {
                  case Unchanged => config
                  case Change    => config.copy(eval = eval)
                }

              val (r2, next) =
                e match {
                  case Unchanged => algorithm.value.run(current).run(newConfig).run(r)
                  case Change =>
                    val (r3, updated) = onChange(current).run(r)
                    algorithm.value.run(updated).run(newConfig).run(r3)
                }

              next match {
                case -\/(error) => Process.fail(error)
                case \/-(value) =>
                  val progress =
                    Progress(algorithm.name, problem, r2.seed, iteration, e, value)
                  Process.emit(progress) ++ go(iteration + 1, r2, value, newConfig)
              }
          }
      }

    val (rng2, current) = collection.run(rng) // the collection of entities

    env.tee(Process.constant(alg))(go(1, rng2, current, initalConfig))
  }

  import com.sksamuel.avro4s._

  def measure[F[_], A, B <: Output](f: F[A] => B)(
      implicit B: SchemaFor[B]): Process1[Progress[F[A]], Measurement[B]] =
    process1.lift {
      case Progress(algorithm, problem, seed, iteration, env, value) =>
        Measurement(algorithm, problem, iteration, env, seed, f(value))
    }
}
