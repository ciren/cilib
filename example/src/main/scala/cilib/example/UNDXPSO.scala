package cilib
package example

import cilib.pso._
import cilib.pso.Defaults._

import scalaz.effect._
import scalaz.effect.IO.putStrLn
import scalaz.NonEmptyList
import spire.implicits._
import spire.math.Interval

object UNDXPSO extends SafeApp {

  val sum = Problems.spherical

  val guide = Guide.undx[Mem[Double]](1.0, 0.1)
  val undxPSO = crossoverPSO(guide)

  val swarm = Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(Interval(-5.12,5.12)^30, 20)
  val iter = Iteration.sync(undxPSO)

  val opt = Comparison.dominance(Min)

  override val runc: IO[Unit] =
    putStrLn(Runner.repeat(1000, iter, swarm).run(opt)(sum).run(RNG.fromTime).toString)

}
