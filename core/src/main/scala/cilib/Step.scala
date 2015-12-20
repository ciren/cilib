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
  import spire.math.Numeric

  def point[A,B](b: B): Step[A,B] =
    Step(_ => _ => RVar.point(b))

  def pointR[A,B](a: RVar[B]): Step[A,B] =
    Step(_ => _ => a)

  def liftK[A,B](a: Reader[Comparison,B]): Step[A,B] =
    Step(o => _ => RVar.point(a.run(o)))

  def withCompare[A,B](f: Comparison => RVar[B]): Step[A,B] =
    Step(o => _ => f(o))

  def evalF[A:Numeric](pos: Position[A]): Step[A,Position[A]] =
    Step { _ => e =>
      RVar.point(pos match {
        case Point(x, b) =>
          //val (fit, vio) = e.eval(x)
          val objective = e.eval(x)
          Solution(x, b, objective)//fit, vio)
        case x @ Solution(_, _, _) =>
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

object StepS {

//  implicit def stepSMonadState[A,S] = new MonadState[StepS[A,S,?], S] {
  implicit def stepSMonadState[A,S] = new MonadState[StepS[A,?,?], S] {
    def point[B](a: => B) =
      StateT.stateTMonadState[S, Step[A,?]].point(a)

    def bind[B,C](fa: StepS[A,S,B])(f: B => StepS[A,S,C]): StepS[A,S,C] =
      fa flatMap f

    def get =
      StateT.stateTMonadState[S, Step[A,?]].get

    def init = get

    def put(s: S) =
      StateT.stateTMonadState[S, Step[A,?]].put(s)
  }

  def get[A,S] =
    stepSMonadState[A,S].get

  def put[A,S](s: S) =
    stepSMonadState[A,S].put(s)

  def apply[A,S,B](f: S => Step[A,(S, B)]): StepS[A,S,B] =
    StateT[Step[A,?],S,B](f)

  def point[A,S,B](b: B): StepS[A,S,B] =
    stepSMonadState[A,S].point(b)

  def pointR[A,S,B](a: RVar[B]): StepS[A,S,B] =
    StateT[Step[A,?],S,B]((s: S) => Step.pointR(a).map((s, _)))

  def pointS[A,S,B](a: Step[A,B]): StepS[A,S,B] =
    StateT[Step[A,?],S,B]((s: S) => a.map((s,_)))

  def liftK[A,S,B](a: Reader[Comparison,B]): StepS[A,S,B] =
    pointS(Step.liftK(a))

  def liftS[A,S,B](a: State[S, B]): StepS[A,S,B] =
    a.lift[Step[A,?]]
}
