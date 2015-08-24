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
 // val thing = quantumBehavedOriginal2004(social, 0.34)

  // RVar
  val swarm = Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(Interval(closed(-5.12),closed(5.12))^30, 20)

  val a = Step.pointR[Double,List[Particle[Mem[Double],Double]]](swarm)

//  val b2 = sync(gbest)

//  type Iter[M[_],A] = Kleisli[M,A,A]

//  def sync[A,B](f: List[B] => B => Step[Double,B]): Iter[Step[Double,?],List[B]] =
//    Kleisli.kleisli[Step[Double,?],List[B],List[B]]((l: List[B]) => l traverseU f(l))

//  def repeat(n: Int, iter: Iter[Step[Double,?], List[cilib.Entity[cilib.Mem[Double],Double]]]) =
//    (l: List[cilib.Entity[cilib.Mem[Double],Double]]) => Range.inclusive(1, n).toStream.map(_ => iter).foldLeftM[Step[Double,?], List[Entity[Mem[Double],Double]]](l) {
//      (a,c) => c.run(a)
//    }

//<<<<<<< HEAD
//  val b3 = repeat/*[Entity[Mem[Double], Double]]*/(100, b2)
  // (1 to 1000).toStream.foldLeft((a, RNG.fromTime))((a,c) => {
  //   val w = a._1 flatMap (b2.run)
  //   val m = w.run((Min, sum))
  //   val (rng2, newPop) = m.run(a._2)
  //   (newPop, rng2)
  // })
//  val w = a flatMap (b3)
//  val m = w.run(Min)(sum)
//=======
  val b2 = Iteration.sync(gbestPSO)
  val w = a flatMap (b2.run)
  val m = w.run(Comparison.quality(Min))(sum)
//>>>>>>> non-empty-interval

//    val y = m run sum
//  val z = m.run(RNG.fromTime)

  //println(z)

  // Run the above algorithm 1000 times, without any parameter changes
  // val r = b2.repeat(1000)
  // val w2 = a flatMap (r)
  // val m2 = w2 run Env(Min, sum)
  // //val y2 = m2 run sum
  // val z2 = m2.run(RNG.fromTime)

  // println(z2)

  // Our IO[Unit] that runs at the end of the world
  override val runc: IO[Unit] =
    putStrLn(m.run(RNG.fromTime).toString)
}
