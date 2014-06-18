package cilib

import scalaz._
import scalaz.IList._

import spire.math._
import spire.algebra._
import Position._

case class Mem[A](b: Position[IList, A], v: Position[IList, A])

object PSO {

  // (S, A) => M[(S, A)] - This is the Kleisli arrow, where M = RVar
  type C[S, A] = Kleisli[RVar, (S, Position[IList, A]), (S, Position[IList, A])]
  type Pos[A] = Position[IList, A]
  type Guide[A] = (IList[Pos[A]], Pos[A]) => Pos[A] // Should expand into a typeclass?

  import Position._

  def velUp[S, A:Numeric](v: Lens[S, Pos[A]], local: Guide[A], global: Guide[A])(collection: IList[Pos[A]]): C[S, A] = // Should the collection not be partially applied to the guides already?
    Kleisli {
      case (s, a) => {
        val A = implicitly[Numeric[A]]

        val localG = local(collection, a)
        val globalG = global(collection, a)

        for {
          cog <- (a - localG)  traverse (x => Dist.stdUniform.map(y => A.times(A.fromDouble(y), x)))
          soc <- (a - globalG) traverse (x => Dist.stdUniform.map(y => A.times(A.fromDouble(y), x)))
        } yield (v.mod(_ + cog + soc, s), a)
      }
    }

  def posUp[S, A:Numeric](v: Lens[S, Pos[A]]): C[S, A] = Kleisli {
    case (s, a) => RVar.point((s, a + v.get(s)))
  }

//  def c[S, A:Numeric] = (velUp[S,A] _) >==> (posUp _)

}


/*
next pso work:
==============
- dynamic psos (quantum, charged, etc) bennie
- vepso / dvepso (robert afer moo & dmoo)
- cooperative & variations
- heterogenous filipe

- niching (less important for now)

commonalities:
- subswarms

functions:
- moo & dmoo functions (benchmarks) robert

*/


/*
 Stopping conditions:
 ====================
 iteration based stopping conditions
 fitness evaluations
 dimension based updates
 # of position updates (on;y defined if change is some epislon based on the position vector)
 # of dimensional updates > epsilon
 */
