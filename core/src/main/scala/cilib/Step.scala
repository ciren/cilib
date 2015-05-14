package cilib

import scalaz._
import scalaz.syntax.state._

/**
  A `Step` is a type that models a single step within a CI Algorithm's operation.

  The general idea would be that you would compose different `Step`s
  to produce the desired algorithmic behaviour.

  Even though this is an initial pass at modeling the compuation of CI algorithms
  this way, it does provide a recursive, list-like composition allowing a multitude
  of different usages (or it is hoped to be so).

  `Step` is nothing more than a data structure that hides the details of a
  monad transformer stack which represents the algoritm parts.
  */
object Step {

  def apply[F[_],A,B](f: ((Opt,Eval[F,A])) => RVar[B]) =
    Kleisli[RVar,(Opt,Eval[F,A]),B](f)

  def point[F[_],A,B](b: B): Step[F,A,B] =
    Kleisli[RVar,(Opt,Eval[F,A]),B]((e: (Opt,Eval[F,A])) => RVar.point(b))

  def pointR[F[_],A,B](a: RVar[B]): Step[F,A,B] =
    Kleisli[RVar,(Opt,Eval[F,A]),B]((e: (Opt,Eval[F,A])) => a)

  def liftK[F[_],A,B](a: Reader[Opt, B]): Step[F,A,B] =
   Kleisli[RVar,(Opt,Eval[F,A]),B]((o: (Opt,Eval[F,A])) => RVar.point(a.run(o._1)))

}

object StepS {

  def apply[F[_],A,S,B](f: S => Step[F,A,(S, B)]) =
    StateT[Step[F,A,?],S,B](f)

  def point[F[_],A,S,B](b: B): StepS[F,A,S,B] =
    b.stateT[Step[F,A,?], S]

  def pointR[F[_],A,S,B](a: RVar[B]): StepS[F,A,S,B] =
    apply(s => Step.pointR(a).map((s, _)))

  def pointK[F[_],A,S,B](a: Step[F,A,B]): StepS[F,A,S,B] =
    StateT.StateMonadTrans[S].liftMU(a)

  def liftK[F[_],A,S,B](a: Reader[Opt,B]): StepS[F,A,S,B] =
    pointK(Step.liftK(a))

  def liftS[F[_],A,S,B](a: State[S, B]): StepS[F,A,S,B] =
    a.lift[Step[F,A,?]]

}
