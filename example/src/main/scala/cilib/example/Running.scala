package cilib
package example

import scalaz._
import Scalaz._

object Running {

  def main(args: Array[String]): Unit = {
    val sum = Problem.static((a: List[Double]) => Valid(a.sum))

    val a = for {
      p <- PSO.createPosition(20).map(PSO.createParticle(x => (Mem(x,x),x)))
      v <- PSO.updateVelocity(p, p._2, p._2, 0.8, 1.4, 1.4)
      r <- PSO.updatePosition(p, v)
    } yield PSO.evalParticle(r) map PSO.updatePBest[Mem[Double]]

    val b = PSO.run(a)

    val randomized = b.run(RNG.fromTime).run
    val y = randomized.map(_ run sum)
    val z = y.map(_ map(_ run Min))

    println(z)
  }
}
