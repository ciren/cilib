package cilib

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

final case class Step[A,B] private[cilib] (run: (Opt,Eval[A]) => RVar[B]) {

  def map[C](f: B => C): Step[A,C] =
    Step { case (opt, eval) =>
      run(opt,eval) map f
    }

  def flatMap[C](f: B => Step[A,C]): Step[A,C] =
    Step { case (opt, eval) => {
      val a = run(opt, eval)
      val b = a.flatMap(f(_).run(opt,eval))
      b
    }}
}

object Step {
  import scalaz._

  implicit def stepMonad[A] = new Monad[Step[A,?]] {
    def point[B](a: => B): Step[A,B] =
      Step.point(a)

    override def map[B,C](fa: Step[A,B])(f: B => C): Step[A,C] =
      fa map f

    def bind[B,C](fa: Step[A,B])(f: B => Step[A,C]): Step[A,C] =
      fa flatMap f
  }

  // def apply[/*F[_],*/A,B](f: ((Opt,Eval[A])) => RVar[B]) =
  //   Kleisli[RVar,(Opt,Eval[A]),B](f)

  def point[/*F[_],*/A,B](b: B): Step[A,B] =
    Step { case (opt,eval) => RVar.point(b) }

  def pointR[/*F[_],*/A,B](a: RVar[B]): Step[A,B] =
    Step { case (opt,eval) => a }

  def liftK[/*F[_],*/A,B](a: Reader[Opt, B]): Step[A,B] =
    Step { case (opt,eval) => RVar.point(a.run(opt)) }

}
