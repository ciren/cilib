package cilib

import scalaz._
import scalaz.IList._
import scalaz.syntax.zip._

import spire.math._
import spire.algebra._
import spire.implicits._
import Position._

case class Mem[A](b: Position[IList, A], v: Position[IList, A])

object PSO {

  // (S, A) => M[(S, A)] - This is the Kleisli arrow, where M = RVar
  type C[S, A] = Kleisli[RVar, (S, Position[IList, A]), (S, Position[IList, A])]
  type Pos[A] = Position[IList, A]
  type Guide[A] = (IList[Pos[A]], Pos[A]) => Pos[A] // Should expand into a typeclass?

  import Position._

  // Should the collection not be partially applied to the guides already?
  def velUp[S, A:Fractional](v: Lens[S, Pos[A]], local: Guide[A], global: Guide[A])(collection: IList[Pos[A]]): C[S, A] =
    Kleisli {
      case (s, a) => {
        val localG = local(collection, a)
        val globalG = global(collection, a)

        for {
          cog <- (a - localG)  traverse (x => Dist.stdUniform.map(y => x * y))
          soc <- (a - globalG) traverse (x => Dist.stdUniform.map(y => x * y))
        } yield (v.mod(_ + cog + soc, s), a)
      }
    }

  def posUp[S, A:Fractional](v: Lens[S, Pos[A]]): C[S, A] = Kleisli {
    case (s, a) => RVar.point((s, a + v.get(s)))
  }

  def barebonesVel[S, A:Fractional](v: Lens[S, Pos[A]], p: Lens[S, Pos[A]], global: Guide[A])(collection: IList[Pos[A]]): C[S, A] =
    Kleisli {
      case (s, a) =>
        val A = implicitly[Fractional[A]]
        val pbest = p.get(s)
        val gbest = global(collection, a)

        val sigmas = Zip[Pos].zipWith(pbest, gbest)((x, y) => spire.math.abs(x - y))
        val means = Zip[Pos].zipWith(pbest, gbest)((x, y) => (x + y) / 2.0)
        val vel = (means zip sigmas) traverse (x => Dist.gaussian(A.toDouble(x._1), A.toDouble(x._2))) map (x => x.map(A.fromDouble(_))) // This needs to be nicer.... A needs to disappear

        vel.map(x => (v.mod(_ => x, s), a))
    }

  def replace[S, A:Numeric](v: Lens[S, Pos[A]]): C[S, A] =
    Kleisli {
      case (s, a) => RVar.point((s, v.get(s)))
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
