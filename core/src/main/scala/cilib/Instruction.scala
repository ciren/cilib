package cilib

import scalaz._

/**
  A Instruction is a type that models a single step in a CI Algorithm's operation.

  The general idea would be that you would compose different Instruction instances
  to produce the desired algorithmic behaviour.

  Even though this is an initial pass at modeling the compuation of CI algorithms
  this way, it does provide a recursive, list-like composition allowing a multitude
  of different usages (or it is hoped to be so).

  `Instruction` is nothing more than a data structure that hides the details of a
  monad transformer stack which represents the algoritm instruction.
  */
final class Instruction[F[_],A,B](val run: ReaderT[RVar, (Opt,Eval[F,A]), B]) {

  def map[C](f: B => C): Instruction[F,A,C] =
    new Instruction(run map f)

  def flatMap[C](f: B => Instruction[F,A,C]): Instruction[F,A,C] =
    new Instruction(run flatMap (f(_).run))
}

object Instruction {
  import scalaz._

  def apply[F[_],A,B](s: Kleisli[RVar,(Opt,Eval[F,A]),B]) =
    new Instruction(s)

  def point[F[_],A,B](b: B): Instruction[F,A,B] =
    new Instruction(Kleisli[RVar,(Opt,Eval[F,A]),B]((e: (Opt,Eval[F,A])) => RVar.point(b)))

  def pointR[F[_],A,B](a: RVar[B]): Instruction[F,A,B] =
    new Instruction(Kleisli[RVar,(Opt,Eval[F,A]),B]((e: (Opt,Eval[F,A])) => a))

  def liftK[F[_],A,B](a: Reader[Opt, B]): Instruction[F,A,B] =
    new Instruction(Kleisli[RVar,(Opt,Eval[F,A]),B]((o: (Opt,Eval[F,A])) => RVar.point(a.run(o._1))))

  implicit def instructionMonad[F[_],A]: Monad[({type l[a] = Instruction[F,A,a]})#l] = new Monad[({type l[a] = Instruction[F,A,a]})#l] {
    def point[C](a: => C) =
      Instruction.point(a)

    def bind[B,C](fa: Instruction[F,A,B])(f: B => Instruction[F,A,C]): Instruction[F,A,C] =
      fa flatMap f
  }
}
