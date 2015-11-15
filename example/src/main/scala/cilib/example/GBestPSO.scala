package cilib
package example

import cilib.Defaults._

import scalaz.NonEmptyList
import scalaz.effect._
import scalaz.effect.IO.putStrLn
import scalaz.std.list._
import spire.implicits._

import scalaz._
import Scalaz._

object GBestPSO extends SafeApp {

  // Create a problem by specifiying the function and it's constrainment
  val sum = Problems.spherical[Double]

  // Define a normal GBest PSO and run it for a single iteration
  val cognitive = Guide.pbest[Mem[Double],Double]
  val social = Guide.gbest[Mem[Double]]
  val gbestPSO = gbest(0.729844, 1.496180, 1.496180, cognitive, social)

  // RVar
  val swarm = Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(Interval(closed(-5.12),closed(5.12))^30, 20)
  val iter = Iteration.sync(gbestPSO)

  val opt = Comparison.dominance(Min)

  // Our IO[Unit] that runs the algorithm, at the end of the world
  override val runc: IO[Unit] =
    putStrLn(Runner.repeat(1000, iter, swarm).run(opt)(sum).run(RNG.fromTime).toString)

}
