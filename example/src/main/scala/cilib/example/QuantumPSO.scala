package cilib
package example

object QunatumPSO {
  import scalaz.syntax.functor._
  import scalaz.std.tuple._
  import scalaz.std.list._
  import PSO._

  import monocle._

  case class QuantumState(b: Position[List,Double], v: Position[List,Double], charge: Double)

  object QuantumState {
    implicit object QSMemory extends Memory[QuantumState] with Velocity[QuantumState] with Charge[QuantumState] {
      def _memory: SimpleLens[QuantumState, Position[List,Double]] = SimpleLens[QuantumState,Position[List,Double]](_.b, (a,b) => a.copy(b = b))
      def _velocity = SimpleLens[QuantumState, Position[List,Double]](_.v, (a,b) => a.copy(v = b))
      def _charge = SimpleLens[QuantumState,Double](_.charge, (a,b) => a.copy(charge = b))
    }
  }

  def main(args: Array[String]): Unit = {

    def quantumPSO[S:Memory:Velocity:Charge](
      w: Double,
      c1: Double,
      c2: Double,
      cognitive: Guide[S,Double],
      social: Guide[S,Double]
    )(implicit C: Charge[S]): List[Particle[S,Double]] => Particle[S,Double] => Instruction[Particle[S,Double]] =
      collection => x => {
        for {
          cog     <- cognitive(collection, x)
          soc     <- social(collection, x)
          p       <- if (C._charge.get(x._1) < 0.01) for {
            v  <- stdVelocity(x, soc, cog, w, c1, c2)
            p  <- stdPosition(x, v)
            p2 <- evalParticle(p)
            p3 <- updateVelocity(p2, v)
          } yield p3
          else for {
            p <- quantum(collection, x, soc, 60.0)
            p2 <- replace(x, p)
            p3 <- evalParticle(p2)
          } yield p3
          updated <- updatePBest(p)
        } yield updated
      }

    val interval = Interval(closed(0.0),closed(100.0))^2

    val cognitive: Guide[QuantumState,Double] = Guide.pbest
    val social: Guide[QuantumState,Double] = Guide.gbest
    val pop = Instruction.pointR(Position.createCollection(PSO.createParticle(x => (QuantumState(x,x.map(_ => 0.0), 0.0), x)))(interval, 40))

    // 20% of the swarm are charged particles
    val pop2: Instruction[List[Particle[QuantumState,Double]]] = pop.map(coll => coll.take(8).map(x => (x._1.copy(charge = 0.05), x._2)) ++ coll.drop(8))

    //val sum = Problem.static((a: List[Double]) => Valid(a.map(x => x*x).sum))
    val peakState = Problems.PeakState(/*peaks*/15, interval)
    //val probRNG = RNG.fromTime
    val prob: (Problems.PeakState, (RNG, List[Problems.Peak])) =
        Problems.initPeaks.run(peakState).map(x => (x._1, x._2.run(RNG.fromTime)))

    val r = Iteration.sync(quantumPSO[QuantumState](0.729844, 1.496180, 1.496180, cognitive, social))//.repeat(1000)
/*
    // Need to track both the problem and the current collection
    (0 to 1000).foldLeft((prob, pop2))((a, c) => {
      val ((peakState, (rng, peaks)), pop3) = a
      val w = pop3.flatMap(r.run)
      val x = w.run(Max)
      val (rng2, y) = {
        val a = Problems.movingPeaks(RVar.point(peaks))
        val (r, p) = a.run(rng)
        (r, x.run(p))
      }
      val (rng3, z: (Problem[List,Double],List[Particle[QuantumState,Double]])) = y.run(rng2)

      if (c == 1000) {
        println(z.map(_.map(x => {
          x._1.v + ":" + x._2.pos.mkString(",") + "," + x._2.fit.getOrElse(0.0)
        }).mkString("\n")))
      }

      if (c % 100 == 0) {
        val (ps, r) = Problems.modifyPeaks(peaks).run(peakState)
        val (rng4, a) = r.run(rng3)

        println("Changing environment peaks: " + a)

        ((ps, r.run(rng3)), Instruction.point(z._2))
      }
      else
        ((peakState, (rng3, peaks)), Instruction.point(z._2))
    })*/
  }
}
