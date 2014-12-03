package cilib
package example

import cilib.Predef._

import scalaz._
import Scalaz._

import spire.implicits._

object LBestPSO {

  def main(args: Array[String]): Unit = {
    val sum = Problem.static((a: List[Double]) => Functions.spherical(a).map(Valid(_)))

    // LBest is a network topology where every Paricle 'x' has (n/2) neighbours
    // on each side. For example, a neighbourhood size of 3 means that there is
    // a single neighbour on both sides of the current particle.

    // Define a LBest PSO and run it for a single iteration
    val cognitive: Guide[Mem[Double],Double] = Guide.pbest
    val social: Guide[Mem[Double],Double] = Guide.lbest[Mem[Double]](3)

    val gbestPSO = gbest[Mem[Double]](0.729844, 1.496180, 1.496180, cognitive, social)
    val a = Instruction.pointR(Position.createCollection(PSO.createParticle(x => (Mem(x,x.map(_ => 0.0)), x)))(Interval(closed(-5.12),closed(5.12))^30, 20))

    val b2 = Iter.sync(gbestPSO)
    val w = a flatMap (b2.run)
    val m = w run Min
    val y = m run sum
    val z = y run RNG.fromTime

    println(z)

    // Run the above algorithm 1000 times, without any parameter changes
    val r = b2 repeat 1000
    val w2 = a flatMap (r)
    val m2 = w2 run Min
    val y2 = m2 run sum
    val z2 = y2 run RNG.fromTime

    println(z2)
  }
}
