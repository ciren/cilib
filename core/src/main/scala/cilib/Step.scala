package cilib

import scalaz._
import scalaz.syntax.state._

/**
  A `Step` is a type that models a single step / operation within a CI Algorithm.

  The general idea would be that you would compose different `Step`s
  to produce the desired algorithmic behaviour.

  Even though this is an initial pass at modeling the compuation of CI algorithms
  this way, it does provide a recursive, list-like composition allowing a multitude
  of different usages (or it is hoped to be so).

  `Step` is nothing more than a data structure that hides the details of a
  monad transformer stack which represents the algoritm parts.
  */
final case class Step[F[_],A,B] private (run: Opt => Eval[F,A] => RVar[B]) {
  def map[C](f: B => C): Step[F,A,C] =
    Step(o => e => run(o)(e).map(f))

  def flatMap[C](f: B => Step[F,A,C]): Step[F,A,C] =
    Step(o => e => run(o)(e).flatMap(f(_).run(o)(e)))
}

object Step {
  def point[F[_],A,B](b: B): Step[F,A,B] =
    Step(_ => _ => RVar.point(b))

  def pointR[F[_],A,B](a: RVar[B]): Step[F,A,B] =
    Step(_ => _ => a)

  def liftK[F[_],A,B](a: Reader[Opt, B]): Step[F,A,B] =
    Step(o => _ => RVar.point(a.run(o)))

  def withOpt[F[_],A,B](f: Opt => RVar[B]): Step[F,A,B] =
    Step(o => _ => f(o))

  def evalF[F[_]:Foldable,A](pos: Position[F,A]): Step[F,A,Position[F,A]] =
    Step { _ => e =>
      RVar.point(pos match {
        case Point(x) =>
          val (fit, vio) = e.eval(x)
          Solution(x, fit, vio)
        case x @ Solution(_, _, _) =>
          x
      })
    }

  implicit def stepMonad[F[_],A] = new Monad[Step[F,A,?]] {
    def point[B](a: => B) =
      Step.point(a)

    def bind[B,C](fa: Step[F,A,B])(f: B => Step[F,A,C]): Step[F,A,C] =
      fa flatMap f
  }
}

object StepS {

  implicit def stepSMonadState[F[_],A,S] = new MonadState[StepS[F,A,?,?], S] {
    def point[B](a: => B) =
      StateT.stateTMonadState[S, Step[F,A,?]].point(a)

    def bind[B,C](fa: StepS[F,A,S,B])(f: B => StepS[F,A,S,C]): StepS[F,A,S,C] =
      fa flatMap f

    def get =
      StateT.stateTMonadState[S, Step[F,A,?]].get

    def init = get

    def put(s: S) =
      StateT.stateTMonadState[S, Step[F,A,?]].put(s)
  }

  def get[F[_],A,S] =
    stepSMonadState[F,A,S].get

  def put[F[_],A,S](s: S) =
    stepSMonadState[F,A,S].put(s)

  def apply[F[_],A,S,B](f: S => Step[F,A,(S, B)]): StepS[F,A,S,B] =
    StateT[Step[F,A,?],S,B](f)

  def point[F[_],A,S,B](b: B): StepS[F,A,S,B] =
    stepSMonadState[F,A,S].point(b)

  def pointR[F[_],A,S,B](a: RVar[B]): StepS[F,A,S,B] =
    StateT[Step[F,A,?],S,B]((s: S) => Step.pointR(a).map((s, _)))

  def pointS[F[_],A,S,B](a: Step[F,A,B]): StepS[F,A,S,B] =
    StateT[Step[F,A,?],S,B]((s: S) => a.map((s,_)))

  def liftK[F[_],A,S,B](a: Reader[Opt,B]): StepS[F,A,S,B] =
    pointS(Step.liftK(a))

  def liftS[F[_],A,S,B](a: State[S, B]): StepS[F,A,S,B] =
    a.lift[Step[F,A,?]]
}
