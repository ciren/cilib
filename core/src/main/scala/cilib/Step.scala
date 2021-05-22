package cilib

/**
  A `Step` is a type that models a single step / operation within a CI Algorithm.

  The general idea would be that you would compose different `Step`s
  to produce the desired algorithmic behaviour.

  Even though this is an initial pass at modeling the compuation of CI algorithms
  this way, it does provide a recursive, list-like composition allowing a multitude
  of different usages (or it is hoped to be so).

  `Step` is nothing more than a data structure that hides the details of a
  monad transformer stack which represents the algoritmic parts.
 */
// sealed abstract class Step[A, B] {
//   import Step._

//   def run(env: Environment[A]): RVar[Exception \/ B] =
//     this match {
//       case Halt(r, e) =>
//         e match {
//           case None    => RVar.pure(\/.left[Exception, B](new Exception(r)))
//           case Some(x) => RVar.pure(\/.left[Exception, B](new Exception(r, x)))
//         }

//       case Cont(f) =>
//         f(env)
//     }

//   def map[C](f: B => C): Step[A, C] =
//     this match {
//       case Halt(r, e) => Halt(r, e)
//       case Cont(_)    => Cont(e => run(e).map(_.map(f)))
//     }

//   def flatMap[C](f: B => Step[A, C]): Step[A, C] =
//     this match {
//       case Halt(r, e) => Halt(r, e)
//       case Cont(_) =>
//         Cont(env =>
//           run(env).flatMap(_ match {
//             case -\/(error) => RVar.pure(error.left[C])
//             case \/-(value) => f(value).run(env)
//           })
//         )
//     }
// }

object Step {

  // private final case class Cont[A, B](f: Environment[A] => RVar[Exception \/ B]) extends Step[A, B]
  // private final case class Halt[A, B](reason: String, error: Option[Exception])  extends Step[A, B]

  def fail[A](reason: String, error: Exception): Step[A] =
    zio.prelude.fx.ZPure.fail(new Exception(reason, error))

  // def failString[A, B](reason: String): Step[A, B] =
  //   Halt(reason, None)

  def pure[A](a: A): Step[A] =
    zio.prelude.fx.ZPure.succeed(a)

  def liftR[A](a: RVar[A]): Step[A] =
    zio.prelude.fx.ZPure.modify((s: RNG) => a.run(s))

  def withCompare[A](a: Comparison => A): Step[A] =
    for {
      env <- zio.prelude.fx.ZPure.environment
    } yield a.apply(env.cmp)

  def withCompareR[A](f: Comparison => RVar[A]): Step[A] =
    for {
      env <- zio.prelude.fx.ZPure.environment[RNG, Environment]
      a <- liftR(f(env.cmp))
    } yield a

  def eval[S, A](f: Position[A] => Position[A])(entity: Entity[S, A]): Step[Entity[S, A]] =
    evalP(f(entity.pos)).map(p => Lenses._position.set(entity, p))

  def evalP[A](pos: Position[A]): Step[Position[A]] =
    for {
      env <- zio.prelude.fx.ZPure.environment[RNG, Environment]
      a <- liftR(Position.eval[A](env.eval, pos))
    } yield a
}

// final case class StepS[A, S, B](run: StateT[S, Step[A, *], B]) {
//   def map[C](f: B => C): StepS[A, S, C] =
//     StepS(run.map(f))
//
//   def flatMap[C](f: B => StepS[A, S, C]): StepS[A, S, C] =
//     StepS(run.flatMap(f(_).run))
//
//   def zoom[S2](l: monocle.Lens[S2, S]): StepS[A, S2, B] =
//     StepS(run.zoom(StepS.lensIso.reverseGet(l)))
// }

object StepS {

  // def lensIso[A, B]: monocle.Iso[scalaz.Lens[A, B], monocle.Lens[A, B]] =
  //   monocle.Iso[scalaz.Lens[A, B], monocle.Lens[A, B]]((s: scalaz.Lens[A, B]) =>
  //     monocle.Lens[A, B](s.get)(b => a => s.set(a, b))
  //   )((m: monocle.Lens[A, B]) => scalaz.Lens.lensu[A, B]((a, b) => m.set(b)(a), m.get(_)))

  // implicit def stepSMonad[A, S]: Monad[StepS[A, S, *]] =
  //   new Monad[StepS[A, S, *]] {
  //     def point[B](a: => B): StepS[A, S, B] =
  //       StepS(StateT[S, Step[A, *], B]((s: S) => Step.pure((s, a))))

  //     def bind[B, C](fa: StepS[A, S, B])(f: B => StepS[A, S, C]): StepS[A, S, C] =
  //       fa.flatMap(f)
  //   }

  // implicit def stepSMonadState[A, S]: MonadState[StepS[A, S, *], S] =
  //   new MonadState[StepS[A, S, *], S] {
  //     private val M = StateT.stateTMonadState[S, Step[A, *]]

  //     def point[B](a: => B) = StepS(M.pure(a))

  //     def bind[B, C](fa: StepS[A, S, B])(f: B => StepS[A, S, C]): StepS[A, S, C] =
  //       fa.flatMap(f)

  //     def get: StepS[A, S, S] =
  //       StepS(M.get)

  //     override def init = StepS(M.get)

  //     def put(s: S) =
  //       StepS(M.put(s))
  //   }
  def getState[S]: zio.prelude.fx.ZPure[Nothing, (RNG, S), (RNG, S), Environment, Exception, S] =
    zio.prelude.fx.ZPure.get.map(_._2)

  def modifyState[S](f: S => S): StepS[S, Unit] =
    zio.prelude.fx.ZPure.modify((s: (RNG, S)) => ((s._1, f(s._2)), ()))

  def liftR[S, A](a: RVar[A]): StepS[S, A] =
    zio.prelude.fx.ZPure.modify((s: (RNG, S)) => {
      val (rng2, b) = a.run(s._1)
      ((rng2, s._2), b)
    })

  def liftStep[S, A](a: Step[A]): StepS[S, A] =
    for {
      env <- zio.prelude.fx.ZPure.environment[(RNG, S), Environment]
      x <- zio.prelude.fx.ZPure.modify((s: (RNG, S)) => {
        val (_, either) = a.provide(env).runAll(s._1)

        either match {
          case Left(_) => sys.error("Unable to lift Step into StepS")
          case Right((rng2, result)) => ((rng2, s._2), result)
        }

      })
    } yield x

  // def liftK[S, A](a: Comparison => A)(implicit ev: Exception <:< Nothing): StepS[S, A] =
  //   liftStep(Step.withCompare(a))

  // def liftS[A, S, B](a: State[S, B]): StepS[A, S, B] =
  //   StepS(a.lift[Step[A, *]])
}
