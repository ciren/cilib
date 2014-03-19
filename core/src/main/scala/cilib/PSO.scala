package cilib

import scalaz._

object Velocity {

  case class Params(
    val w: Double = 0.72,
    val c1: Double = 1.496,
    val c2: Double = 1.496,
    val vel: Solution = Vector(1.0, 2.0, 3.0),
    val pbest: Solution = Vector(1.0, 2.0, 3.0)
  )

  import scalaz._
  import scalaz.Id._
  import scalaz.StateT._
  import scalaz.syntax.apply._
  import spire.implicits._

  type Guide[A, B] = NonEmptyList[Entity] => Entity => StateT[RVar, A, B]

  def inertia[A](w: Lens[A, Double]): Solution => VState[A, Solution] =
    x => VState.get[A].map(w.get(_) *: x)

  def cognitive[A](c1: Lens[A, Double]): Solution => VState[A, Solution] =
    x => for {
      s <- VState.get[A]
    } yield c1.get(s) *: x

  def social[A](c2: Lens[A, Double]): /*NonEmptyList[Solution] =>*/ Solution => VState[A, Solution] =
    /*xs =>*/ x => for {
      s <- VState.get[A]
      r <- VState.pointR(Dist.stdUniform.replicateM(x.length))
    } yield (c2.get(s) *: r.toVector) * x

  val wL:  Lens[Params, Double] = Lens.lensu((a, b) => a.copy(w = b), _.w)
  val c1L: Lens[Params, Double] = Lens.lensu((a, b) => a.copy(c1 = b), _.c1)
  val c2L: Lens[Params, Double] = Lens.lensu((a, b) => a.copy(c2 = b), _.c2)

  // TODO: Make VState its own definition to avoid the roundabout definitions
  def stdVel(velL: Lens[Params, Solution]): VState[Params, Solution] =
    for {
      c <- VState.get[Params]
      vel = velL.get(c)
      w <- inertia(wL)(vel)
      cog <- cognitive(c1L)(vel)
      soc <- social(c2L)(vel)
    } yield w + cog + soc

  def withStep[A](step: VState[A, Solution]): Solution => VState[A, Solution] =
    x => step.map(_ + x)

  final class VState[A, B](val v: StateT[RVar, A, B]) {
    def map[C](f: B => C): VState[A, C] =
      new VState(v map f)

    def flatMap[C](f: B => VState[A, C]): VState[A, C] =
      new VState(v flatMap (f(_).v))
  }

  object VState {
    def apply[A, B](a: StateT[RVar, A, B]) =
      new VState(a)

    def point[A, B](a: A) =
      new VState(StateT((s: A) => RVar.point((s, a))))

    def pointR[A, B](a: RVar[B]) =
      new VState(StateT((s: A) => a.map(x => (s, x))))

    def get[A] = new VState(StateT.stateTMonadState[A, RVar].get)
  }


  // def stdVelocity(inertia: Kleisli[VState, Solution, Solution], cog: Kleisli[VState, Solution, Solution], soc: Kleisli[VState, Solution, Solution]) =
  //   (inertia |@| cog |@| soc) { _ + _ + _ }

  // def position: Kleisli[VState, Solution, Solution] =
  //   Kleisli[VState, Solution, Solution](sol => State[Params, Solution] { params => (params, sol) })

  // def update =
  //   (position |@| stdVelocity(inertia, inertia, inertia)) { _ + _ }

}
