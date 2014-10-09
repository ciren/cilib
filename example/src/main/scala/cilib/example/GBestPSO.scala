package cilib
package example

import cilib.Predef._

import scalaz._
import Scalaz._

object Running {

  def main(args: Array[String]): Unit = {
    val sum = Problem.static((a: List[Double]) => Valid(a.sum))

    // Define a normal GBest PSO and run it for a single iteration
    val cognitive: Guide[Mem[Double],Double] = (c, x) => Instruction.point(x._2)
    val social: Guide[Mem[Double],Double] = (c, x) => Instruction.point(x._2)

    val gbestPSO = gbest[Mem[Double]](0.8, 1.4, 1.4, cognitive, social)
    val a = Instruction.pointR(Position.createCollection(PSO.createParticle(x => (Mem(x,x), x)))(Interval(closed(-5.12),closed(5.12))^20, 20))

    val b2 = Scheme.sync(gbestPSO)
    val w = a flatMap (l => b2.run(l))

    val m = w.run(Min)
    val y = m run sum
    val z = y.run(RNG.fromTime)

    println(z)

    // Run the above algorithm 1000 times, without any parameter changes
    val r = b2.repeat(1000)
    val w2 = a flatMap (r run _)
    val m2 = w2 run Min
    val y2 = m2 run sum
    val z2 = y2.run(RNG.fromTime)

    println(z2)

    // Now run the a PSO algorithm but the cognitive and social coefficients are adjusted from 2.0 to 1.4 over 1000 iterations
    import scalaz._
    import Scalaz._
    def linearDecrease(n: Int, d: Int) = n / d.toDouble


    // val functions = (1 to 1000).map(x =>
    //   (l: List[PSO.Particle[Mem[Double],Double]]) =>
    //   PSO.syncUpdate(l, PSO.gbest(0.8, linearDecrease(x, 1000), linearDecrease(x, 1000), cognitive, social))
    // )

    //functions.foldLeft()

    // GCPSO
    // val tmp = Guide.identity[Mem[Double],Double]
    // val gcp = gcpso(0.8, 1.4, 1.4, tmp)

    // val ss = Scheme.syncS(gcp)
  }
}
