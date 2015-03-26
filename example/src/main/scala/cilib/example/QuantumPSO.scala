package cilib
package example

object QunatumPSO {
  import scalaz.std.list._
  import PSO._

  import monocle._
  import spire.algebra._
  import spire.implicits._

  import scalaz.Traverse

  case class QuantumState(b: Position[List,Double], v: Position[List,Double], charge: Double)

  object QuantumState {
    implicit object QSMemory
        extends Memory[QuantumState,List,Double]
        with Velocity[QuantumState,List,Double]
        with Charge[QuantumState] {
      def _memory = Lens[QuantumState,Position[List,Double]](_.b)(b => a => a.copy(b = b))
      def _velocity = Lens[QuantumState, Position[List,Double]](_.v)(b => a => a.copy(v = b))
      def _charge = Lens[QuantumState,Double](_.charge)(b => a => a.copy(charge = b))
    }
  }

  def main(args: Array[String]): Unit = {

    // def gbestNearestBoundryCloud[S,F[_]](
    //   collection: List[Particle[S,F,Double]],
    //   x: Particle[S,F,Double],
    //   nbest: Position[F,Double]
    // ): Step[F,Double,Position[F,Double]] = {
    //   def euclideanDist(a: List[Double], b: List[Double]) = {

    //   }

//       for {
//         point <- x._2.pos
//         inter <- x._2.interval
//       } yield {
//         List((point.), ())
//       }

//       x._2.interval.flatMap(x =>
// //      val r = nbest
// //      quantum(collection, x, nbest, r)
//     }


    def quantumPSO[S,F[_]:Traverse](
      w: Double,
      c1: Double,
      c2: Double,
      cognitive: Guide[S,F,Double],
      social: Guide[S,F,Double]
    )(
      implicit C: Charge[S], V: Velocity[S,F,Double], M: Memory[S,F,Double], mod: Module[F[Double],Double]
    ): List[Particle[S,F,Double]] => Particle[S,F,Double] => Step[F,Double,Particle[S,F,Double]] =
      collection => x => {
        for {
          cog    <- cognitive(collection, x)
          soc    <- social(collection, x)
          v      <- stdVelocity(x, soc, cog, w, c1, c2)
          p      <- if (C._charge.get(x.state) < 0.01) stdPosition(x, v)
                    else quantum(collection, x, soc, 20.0).flatMap(replace(x, _))
          p2      <- evalParticle(p)
          p3      <- updateVelocity(p2, v)
          updated <- updatePBest(p3)
        } yield updated
      }

    val interval = Interval(closed(0.0),closed(100.0))^2//30
    val r = Iteration.sync(quantumPSO[QuantumState,List](0.729844, 1.496180, 1.496180, Guide.pbest, Guide.gbest))

    val swarm = Position.createCollection(PSO.createParticle(x => Entity(QuantumState(x,x.map(_ => 0.0), 0.0), x)))(interval, 40)
    val pop = Step.pointR[List,Double,List[Particle[QuantumState,List,Double]]](swarm)

    // 20% of the swarm are charged particles
    val pop2 = pop.map(coll => coll.take(8).map(x => x.copy(state = x.state.copy(charge = 0.05))) ++ coll.drop(8))

    val constraints = List(
      GreaterThan(ConstraintFunction((l: List[Double]) => math.pow((l(0) - 50.0) / 45.0, 2) + math.pow((l(1) - 50.0) / 20.0, 2)), 1.0) // ellipse in center of search space
    )

    import scalaz.std.list._

    val initialProb =// : RVar[(Problems.PeakState, Eval[List,Double])] =
      Problems.initPeaks[List](15, interval, 30.0, 70.0, 10.0, 20.0).map(x => (x._1, x._2.constrainBy(constraints)))

    // This is not a runner of sorts?
    // Need to track both the problem and the current collection
    def experiment(rng: RNG) = (Range(0, 1000).toStream.foldLeft((rng, initialProb, pop2, List.empty[Problems.Peak]))((a, c) => {
      val (rng, prob, pop, _) = a
      val (rng2, (ps, eval)) = prob.run(rng)

      println("peaks: " + ps.peaks)

      val w = pop flatMap r.run
      val x = w.run((Max,eval))
      val (rng3, nextPop) = x.run(rng2)

      val next = nextPop.map(penalize(Max))
      //println(next)

      //import scalaz.StateT
      val nextProb = prob
//        if (c % 101 == 0) Problems.modifyPeaks[List]/*.map(_.constrainBy(constraints))*/.run(ps)
//        else prob

      (rng3, nextProb, Step.point(next), ps.peaks)
    }))

    // Run the experiment 30 times
    Range(0, 1).foreach(x => println({
      val (rng, nextP, pop, peaks) = experiment(RNG.init(x.toLong))

      //pop.run((Max,null)).run(null)._2//.map(_.p)

      peaks.map(x => x.pos.mkString(/*"[",*/ "\t"/*, "]"*/ /*+ "\t" + x.width + "," + x.height*/)).mkString("\n") + "\n\n" +
      pop.run((Max, null)).run(null)._2.map(_.pos).map(x => x.pos.mkString("\t") /*+ "\t" + x.fit*/).mkString("\n")
    }))
  }

  import scalaz.Foldable
  import scalaz.syntax.apply._
  import scalaz.syntax.foldable._

  import Lenses._

  def penalize[S,F[_]:Foldable](opt: Opt) = (e: Particle[S,F,Double]) => {
    val magnitude = e.pos.violations.map(x => Constraint.violationMagnitude(1.0, 5.0, x, e.pos.pos.toList))

    (magnitude |@| e.pos.fit) { (mag, fit) => {
      println("mag: " + mag)
      fit match {
        case Penalty(_, _) => sys.error("shit")
        case Valid(v) =>
          _position.modify((p: Position[F,Double]) =>
            if (mag > 0.0) p.adjustFit(Penalty(opt match {
              case Min => v + mag
              case Max => v - mag
            }, mag)) else p)(e)
      }
    }}.getOrElse(e)
  }
}
