package cilib
package example

import cilib.Defaults.gbest

import scalaz.effect._
import scalaz.effect.IO.putStrLn
import scalaz.std.list._
import spire.implicits._
import spire.math.Interval

import cilib.syntax.algorithm._

object GBestPSO extends SafeApp {

  val sum = Problems.spherical[List,Double]

  // Define a normal GBest PSO and run it for a single iteration
  val cognitive = Guide.pbest[Mem[List,Double],List,Double]
  val social = Guide.gbest[Mem[List,Double],List]

  val gbestPSO: List[Particle[Mem[List,Double],List,Double]] => Particle[Mem[List,Double],List,Double] => Step[List,Double,Result[Particle[Mem[List,Double],List,Double]]] =
    gbest(0.729844, 1.496180, 1.496180, cognitive, social).map(One(_))

  // RVar
  val swarm = Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(Interval(-5.12,5.12)^30, 20)
  val syncGBest = Iteration.sync(gbestPSO)

  // Our IO[Unit] that runs at the end of the world
  override val runc: IO[Unit] =
    putStrLn(Runner.repeat(1000, syncGBest, swarm).run(Min)(sum).run(RNG.fromTime).toString)
}
