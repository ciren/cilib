package cilib

import scalaz._

/**
  A Instruction is a type that models a single step in an Algorithm's operation.

  The general idea would be that you would compose different Instruction instances
  to produce the desired behaviour. Furthermore, the Instruction is parameterized
  on some input type `I` and some output type 'O'.

  Even though this is an initial pass at modeling the compuation of CI algorithms
  this way, it does provide a recursive, list-like composition allowing a multitude
  of different usages (or it is hoped to be so).

  An instruction either returns a calculated value, suspends some value or it
  does both and continues the computation. The Instruction is nothing more than a free
  monad, but suited to our uses.
  */
// This should be extracted to use transformers - Does this mean we need RVarT?
trait InstructionFunctions {

}

//case class Instruction[F[_],D,A](run: RVar[State[Problem[F,D],Reader[Opt,A]]]) {
//case class Instruction[F[_],D,A](run: StateT[RVar, Problem[F,D], Reader[Opt,A]]) {
final class Instruction[A](val run: ReaderT[X, Opt, A]) {

  def map[B](f: A => B): Instruction[B] =
    new Instruction(run map f)

  def flatMap[B](f: A => Instruction[B]): Instruction[B] =
    new Instruction(run flatMap (f(_).run))
}

object Instruction {
  import scalaz._, Scalaz._

  def apply[A](s: Kleisli[X,Opt,A]) =
    new Instruction(s)

  def point[A](a: A): Instruction[A] =
    new Instruction(Kleisli[X, Opt, A]((o: Opt) => StateT((p: Problem[List,Double]) => RVar.point((p, a)))))

  def pointR[A](a: RVar[A]): Instruction[A] =
    new Instruction(Kleisli[X,Opt,A]((o: Opt) => StateT((p: Problem[List,Double]) => a.map(x => (p,x)))))

  def pointS[A](a: StateT[RVar, Problem[List,Double],A]): Instruction[A] =
    new Instruction(Kleisli[X,Opt,A]((o: Opt) => a))

  def liftK[A](a: Reader[Opt, A]): Instruction[A] =
    new Instruction(Kleisli[X, Opt, A]((o: Opt) => StateT((p: Problem[List,Double]) => RVar.point((p, a.run(o))))))

  implicit val instructionMonad: Monad[Instruction] = new Monad[Instruction] {
    def point[A](a: => A) =
      Instruction.point(a)

    def bind[A, B](fa: Instruction[A])(f: A => Instruction[B]): Instruction[B] =
      fa flatMap f
  }
}
