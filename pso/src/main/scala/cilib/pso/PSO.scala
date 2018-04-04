package cilib
package pso

import monocle._, Monocle._
import Position._
import scalaz._
import Scalaz._

import spire.algebra._
import spire.implicits._

object PSO {
  import Lenses._
  // Constrain this better - Not numeric. Operation for vector addition
  def stdPosition[S, A](
      c: Particle[S, A],
      v: Position[A]
  )(implicit A: Module[Position[A], A]): Step[A, Particle[S, A]] =
    Step.pure(_position.modify((_: Position[A]) + v)(c))

  // Dist \/ Double (scalar value)
  // This needs to be fleshed out to cater for the parameter constants // remember to extract Dists
  def stdVelocity[S](
      entity: Particle[S, Double],
      social: Position[Double],
      cognitive: Position[Double],
      w: Double,
      c1: Double,
      c2: Double
  )(implicit V: HasVelocity[S, Double]): Step[Double, Position[Double]] =
    Step.liftR(for {
      cog <- (cognitive - entity.pos).traverse(x => Dist.stdUniform.map(_ * x))
      soc <- (social - entity.pos).traverse(x => Dist.stdUniform.map(_ * x))
    } yield (w *: V._velocity.get(entity.state)) + (c1 *: cog) + (c2 *: soc))

  // Step to evaluate the particle, without any modifications
  def evalParticle[S](entity: Particle[S, Double]) =
    Step.eval[S, Double](x => x)(entity)

  def updatePBest[S](p: Particle[S, Double])(
      implicit M: HasMemory[S, Double]): Step[Double, Particle[S, Double]] = {
    val pbestL = M._memory
    Step
      .withCompare(Comparison.compare(p.pos, p.state.applyLens(pbestL).get))
      .map(x => Entity(p.state.applyLens(pbestL).set(x), p.pos))
  }

  def updatePBestBounds[S](p: Particle[S, Double])(
      implicit M: HasMemory[S, Double]): Step[Double, Particle[S, Double]] = {
    val b = Foldable1[NonEmptyList].foldLeft(p.pos.pos.zip(p.pos.boundary), true)((a, c) =>
      a && (c._2.contains(c._1)))

    if (b) updatePBest(p) else Step.pure(p)
  }

  def updateVelocity[S](p: Particle[S, Double], v: Position[Double])(
      implicit V: HasVelocity[S, Double]): Step[Double, Particle[S, Double]] =
    Step.liftR(RVar.pure(Entity(p.state.applyLens(V._velocity).set(v), p.pos)))

  def singleComponentVelocity[S](
      entity: Particle[S, Double],
      component: Position[Double],
      w: Double,
      c: Double
  )(implicit V: HasVelocity[S, Double]): Step[Double, Position[Double]] =
    Step.liftR(for {
      comp <- (component - entity.pos).traverse(x => Dist.stdUniform.map(_ * x))
    } yield (w *: V._velocity.get(entity.state)) + (c *: comp))

  final case class GCParams(p: Double, successes: Int, failures: Int, e_s: Double, e_f: Double)

  def defaultGCParams =
    GCParams(p = 1.0, successes = 0, failures = 0, e_s = 15, e_f = 5)

  def gcVelocity[S](
      entity: Particle[S, Double],
      nbest: Position[Double],
      w: Double,
      s: GCParams
  )(implicit V: HasVelocity[S, Double]): Step[Double, Position[Double]] =
    Step.liftR(
      nbest
        .traverse(_ => Dist.stdUniform.map(x => s.p * (1 - 2 * x)))
        .map(a => -1.0 *: entity.pos + nbest + w *: V._velocity.get(entity.state) + a))

  def barebones[S](p: Particle[S, Double], global: Position[Double])(
      implicit M: HasMemory[S, Double]) =
    Step.liftR {
      val pbest = M._memory.get(p.state)
      val zipped = pbest.zip(global)
      val sigmas = zipped.map { case (x, y) => math.abs(x - y) }
      val means = zipped.map { case (x, y)  => (x + y) / 2.0 }

      (means.zip(sigmas)).traverse(x => Dist.gaussian(x._1, x._2))
    }

  //  } else { // the particle is charged
  //     //based on the Pythagorean theorem,
  //     //the following code breaks the square of the radius distance into smaller
  //     //parts that are then "distributed" among the dimensions of the problem.
  //     //the position of the particle is determined in each dimension by a random number
  //     //between 0 and the part of the radius assigned to that dimension
  //     //This ensures that the quantum particles are placed randomly within the
  //     //multidimensional sphere determined by the quantum radius.

  //     this.nucleus = (Vector) AbstractAlgorithm.get().getBestSolution().getPosition();

  //     double distance = Math.pow(this.radius.getParameter(), 2); //square of the radius
  //     int dimensions = particle.getDimension();
  //     double[] pieces = new double[dimensions]; // break up of the distance
  //     pieces[dimensions - 1] = distance;
  //     for (int i = 0; i < dimensions - 1; i++) {
  //         pieces[i] = this.randomiser.getRandomNumber(0, distance);
  //     }//for
  //     Arrays.sort(pieces);
  //     int sign = 1;
  //     if (this.randomiser.getRandomNumber() <= 0.5) {
  //         sign = -1;
  //     }//if
  //     //deals with first dimension
  //     Vector.Builder builder = Vector.newBuilder();
  //     builder.add(this.nucleus.doubleValueOf(0) + sign * this.randomiser.getRandomNumber(0, Math.sqrt(pieces[0])));
  //     //deals with the other dimensions
  //     for (int i = 1; i < dimensions; i++) {
  //         sign = 1;
  //         if (this.randomiser.getRandomNumber() <= 0.5) {
  //             sign = -1;
  //         }//if
  //         double rad = Math.sqrt(pieces[i] - pieces[i - 1]);
  //         double dis = this.randomiser.getRandomNumber(0, rad);
  //         double newpos = this.nucleus.doubleValueOf(i) + sign * dis;
  //         builder.add(newpos);
  //     }//for
  //     return builder.build();
  // }//else

  // This is relative to the origin
  def quantum(
      x: Position[Double], // passed in only to get the length of the vector
      r: RVar[Double], // magnitude of the radius for the hypersphere
      dist: (Double, Double) => RVar[Double] // Distribution used
  ): Step[Double, Position[Double]] =
    Step.liftR {
      for {
        r_i <- x.traverse(_ => Dist.stdUniform) //(0.0, 1.0))
        //_ = println("r_i: " + r_i)
        originSum = math.sqrt(r_i.pos.foldLeft(0.0)((a, c) => a + c * c))
        //_ = println("originSum: " + originSum)
        scale <- r.flatMap(a => {
          /*println("r: " + a); */
          val g = dist(0.0, a); /*println("g: " + g);*/
          g
        }) // Use the provided distribution to scale the cloud radius
      } yield {
        //println("scale: " + scale)
        val z = (scale / originSum) *: r_i
        //println("ratio: " + (scale / originSum))
        //println(z.pos)
        z
      }
    }

  def acceleration[S](collection: NonEmptyList[Particle[S, Double]],
                      x: Particle[S, Double],
                      distance: (Position[Double], Position[Double]) => Double,
                      rp: Double,
                      rc: Double)(implicit C: HasCharge[S]): Step[Double, Position[Double]] = {
    def charge(x: Particle[S, Double]) =
      C._charge.get(x.state)

    Step.pure(collection.list
      .filter(z => charge(z) > 0.0)
      .foldLeft(x.pos.zeroed) { (p1, p2) =>
        {
          val d = distance(x.pos, p2.pos)
          if (d > rp || (x eq p2)) p1
          else
            (charge(x) * charge(p2) / (d * (if (d < rc) (rc * rc) else (d * d)))) *: (x.pos - p2.pos) + p1
        }
      })
  }

  // Naming?
  def replace[S](entity: Particle[S, Double],
                 p: Position[Double]): Step[Double, Particle[S, Double]] =
    Step.pure(entity.applyLens(_position).set(p))

  def better[S, A](a: Particle[S, A], b: Particle[S, A]): Step[A, Boolean] =
    Comparison.fittest(a, b).map(_ eq a)

  def createParticle[S](f: Position[Double] => Particle[S, Double])(
      pos: Position[Double]): Particle[S, Double] =
    f(pos)
}

/*
next pso work:
==============
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
 # of position updates (only defined if change is some epislon based on the position vector)
 # of dimensional updates > epsilon
 */
// trait PopAlg[A] {
//   def run: List[A] => A => Step[Double,A]
// }
