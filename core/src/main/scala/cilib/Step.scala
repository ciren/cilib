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
  monad transformer stack which represents the algoritmic parts.
  */
final case class Step[A,B] private (run: Comparison => Eval[A] => RVar[B]) {
  def map[C](f: B => C): Step[A,C] =
    Step(o => e => run(o)(e).map(f))

  def flatMap[C](f: B => Step[A,C]): Step[A,C] =
    Step(o => e => run(o)(e).flatMap(f(_).run(o)(e)))
}

object Step {
  import scalaz._

  def point[A,B](b: B): Step[A,B] =
    Step(_ => _ => RVar.point(b))

  def pointR[A,B](a: RVar[B]): Step[A,B] =
    Step(_ => _ => a)

  def liftK[A,B](a: Reader[Comparison,B]): Step[A,B] =
    Step(o => _ => RVar.point(a.run(o)))

  def withCompare[A,B](f: Comparison => RVar[B]): Step[A,B] =
    Step(o => _ => f(o))

  def evalF[A](pos: Position[A]): Step[A,Position[A]] =
    Step { _ => e =>
      RVar.point(pos match {
        case Point(x, b) =>
          val (fit, vio) = e.eval(x)
          Solution(x, b, fit, vio)
        case x @ Solution(_, _, _, _) =>
          x
      })
    }

  implicit def stepMonad[A] = new Monad[Step[A,?]] {
    def point[B](a: => B) =
      Step.point(a)

    def bind[B,C](fa: Step[A,B])(f: B => Step[A,C]): Step[A,C] =
      fa flatMap f
  }
}

// Should the internal StateT not be hidden?
final case class StepS[A,S,B] private (run: StateT[Step[A,?],S,B]) {
  def map[C](f: B => C): StepS[A,S,C] =
    StepS(run map f)

  def flatMap[C](f: B => StepS[A,S,C]): StepS[A,S,C] =
    StepS(run.flatMap(f(_).run))
}

object StepS {
  implicit def stepSMonad[A,S] = new Monad[StepS[A,S,?]] {
    def point[B](a: => B) =
      StepS.point(a)

    def bind[B,C](fa: StepS[A,S,B])(f: B => StepS[A,S,C]): StepS[A,S,C] =
      fa flatMap f
  }

  def point[A,S,B](b: B): StepS[A,S,B] =
    StepS(StateT.stateT[Step[A,?],S,B](b))

  def pointR[A,S,B](a: RVar[B]): StepS[A,S,B] =
    StepS(StateT[Step[A,?],S,B](
      (s: S) => Step.pointR(a).map((s, _))
    ))

/*  def pointS[F[_],A,S,B](a: Step[F,A,B]): StepS[F,A,S,B] =
    StateT.StateMonadTrans[S].liftMU(a)

  def liftK[F[_],A,S,B](a: Reader[Opt,B]): StepS[F,A,S,B] =
    pointK(Step.liftK(a))

  def liftS[F[_],A,S,B](a: State[S, B]): StepS[F,A,S,B] =
    a.lift[Step[F,A,?]]
 */
}
