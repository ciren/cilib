package cilib
package example

import scalaz.NonEmptyList
import scalaz.effect._
import scalaz.effect.IO._
import scalaz.std.list._
import scalaz.syntax.apply._
import scalaz.syntax.traverse._

import monocle._
import spire.algebra._
import spire.implicits._

object QuantumPSO extends SafeApp {
  import PSO._
  import Lenses._

  case class QuantumState(b: Position[Double], v: Position[Double], charge: Double)

  object QuantumState {
    implicit object QSMemory
        extends Memory[QuantumState,Double]
        with Velocity[QuantumState,Double]
        with Charge[QuantumState] {
      def _memory = Lens[QuantumState,Position[Double]](_.b)(b => a => a.copy(b = b))
      def _velocity = Lens[QuantumState, Position[Double]](_.v)(b => a => a.copy(v = b))
      def _charge = Lens[QuantumState,Double](_.charge)(b => a => a.copy(charge = b))
    }
  }

  def quantumPSO[S](
    w: Double,
    c1: Double,
    c2: Double,
    cognitive: Guide[S,Double],
    social: Guide[S,Double],
    cloudR: RVar[Double])(
    implicit C: Charge[S], V: Velocity[S,Double], M: Memory[S,Double], mod: Module[Position[Double],Double]
  ): List[Particle[S,Double]] => Particle[S,Double] => Step[Double,Particle[S,Double]] =
    collection => x => {
      for {
        cog     <- cognitive(collection, x)
        soc     <- social(collection, x)
        v       <- stdVelocity(x, soc, cog, w, c1, c2)
        r       <- Step.pointR(cloudR)
        p       <- if (C._charge.get(x.state) < 0.01) stdPosition(x, v)
                   else quantum(collection, x, soc, r).flatMap(replace(x, _))
        p2      <- evalParticle(p)
        p3      <- updateVelocity(p2, v)
        updated <- updatePBestBounds(p3)
      } yield updated
    }

  def penalize[S](opt: Opt) = (e: Particle[S,Double]) => {
    val magnitude = e.pos.violations.map(x => Constraint.violationMagnitude(5.0, 5.0, x, e.pos.pos.toList))

    (magnitude |@| e.pos.fit) { (mag, fit) => {
      fit match {
        case Penalty(_, _) => sys.error("??? How?")
        case Valid(v) =>
          (_position[S,Double] composeOptional _fitness).modify((x: Fit) =>
            if (mag > 0.0) Penalty(opt match {
              case Min => v + mag
              case Max => v - mag
            }, mag) else x
          )(e)
      }
    }}.getOrElse(e)
  }

 

  // Usage
  val domain = Interval(closed(0.0),closed(100.0)) ^ 5
  val qpso = Iteration.sync(quantumPSO[QuantumState](0.729844, 1.496180, 1.496180, Guide.pbest, Guide.gbest, RVar.point(20.0)))
  val qpsoDist = Iteration.sync(quantumPSO[QuantumState](0.729844, 1.496180, 1.496180, Guide.pbest, Guide.gbest, Dist.cauchy(0.0, 10.0)))

  def swarm = Position.createCollection(
    PSO.createParticle(x => Entity(QuantumState(x, x.zeroed, 0.0), x)))(domain, 40)

  // 20% of the swarm are charged particles
  def pop: RVar[List[cilib.Entity[cilib.example.QuantumPSO.QuantumState,Double]]] =
    swarm.map(coll => coll.take(8).map(x => x.copy(state = x.state.copy(charge = 0.05))) ++ coll.drop(8))

  def initialPeaks(s: Double): RVar[List[Problems.PeakCone]] =
    (1 to 15).toList.traverse(_ => Problems.defaultPeak(domain, s))

  def iteration(
    swarm: List[cilib.Entity[cilib.example.QuantumPSO.QuantumState,Double]]
  ): Step[Double,List[cilib.Entity[cilib.example.QuantumPSO.QuantumState,Double]]] = {
    import scalaz.syntax.std.list._
    import scalaz.syntax.std.option._

    swarm.toNel.cata(nel => qpso.run(nel.list).map(_.map(penalize(Max))), Step.point(List.empty))
  }






  import scalaz.StateT
  def mpb(heightSeverity: Double, widthSeverity: Double): StateT[RVar, (List[Problems.PeakCone],List[cilib.Entity[cilib.example.QuantumPSO.QuantumState,Double]]), Eval[Double]] =
    StateT { case (peaks, pop) => {
      val newPeaks: RVar[List[Problems.PeakCone]] = peaks.traverse(_.update(heightSeverity, widthSeverity))
      newPeaks.map(np => ((np, pop), Problems.peakEval(np).constrainBy(EnvConstraints.centerEllipse)))
    }}

  val comparison = Comparison.dominance(Max)

  def run2(eval: Eval[Double]): StateT[RVar, (List[Problems.PeakCone], List[cilib.Entity[cilib.example.QuantumPSO.QuantumState,Double]]), List[cilib.Entity[cilib.example.QuantumPSO.QuantumState,Double]]] =
    StateT { case (peaks, pop) => { println("running"); iteration(pop).run(comparison)(eval).map(r => ((peaks, r), r))} }

  import scalaz.syntax.applicative._
  def staticE(n: Int) = mpb(0.0, 0.0).flatMap(e => run2(e).replicateM(n))
  def progressiveE(n: Int) = mpb(1.0, 0.05).flatMap(run2).replicateM(n)
  def abruptE(n: Int) = mpb(10.0, 0.05).flatMap(e => run2(e).replicateM(200)).replicateM(n / 200)
  def chaosE(n: Int) = mpb(10.0, 0.05).flatMap(run2).replicateM(n)

  def static(n: Int): RVar[(List[Problems.PeakCone], List[cilib.Entity[cilib.example.QuantumPSO.QuantumState,Double]])] =
    for {
      peaks <- initialPeaks(0.0)
      swarm <- pop
      e <- staticE(n).exec((peaks, swarm))
    } yield e

  def progressive(n: Int) =
    for {
      peaks <- initialPeaks(1.0)
      swarm <- pop
      e <- progressiveE(n).exec((peaks, swarm))
    } yield e

  def abrupt(n: Int) =
    for {
      peaks <- initialPeaks(50.0)
      swarm <- pop
      e <- abruptE(n).exec((peaks, swarm))
    } yield e

  def chaos(n: Int) =
    for {
      peaks <- initialPeaks(50.0)
      swarm <- pop
      e <- chaosE(n).exec((peaks, swarm))
    } yield e

  override val runc: IO[Unit] = {
    val x = for {
      rng <- IO(RNG.fromTime)
      r <- IO(static(1000).run(rng))
      peaks <- IO(r._2._1.map(_.location.pos.list.mkString("(", ",", ")")).mkString("", "", "|"))
      positions <- IO(r._2._2.map(_.pos.pos.list.mkString("(", ",", ")")).mkString("", "", "|"))
      meanFit <- IO(r._2._2.traverse(_.pos.fit).cata(l => l.map(_.fold(
        penalty = _.v, valid = _.v
      )).suml / l.length, 0.0).toString)
    } yield meanFit//(peaks,positions,meanFit)

    //    x.replicateM(30).flatMap(a => putStrLn(a.map(x => x._1.toString + x._2.toString + x._3.toString).mkString("\n")))
    x.flatMap(a => putStrLn(a))//.replicateM(30).flatMap(a => putStrLn(a.map(x => x.toString).mkString("List(",",\n",")")))
  }


  object EnvConstraints {
    // constraints
    val centerEllipse = List(
      GreaterThanEqual(ConstraintFunction((l: List[Double]) => math.pow((l(0) - 50.0) / 45.0, 2) + math.pow((l(1) - 50.0) / 20.0, 2)), 1.0) // ellipse in center of search space
    )
    val disjointCircles = List(
      GreaterThan(ConstraintFunction((l: List[Double]) => {
        math.pow(l(0) - 25.0, 2) + math.pow(l(1) - 25.0, 2)
      }), 64.0),
      GreaterThan(ConstraintFunction((l: List[Double]) => {
        math.pow(l(0) - 25.0, 2) + math.pow(l(1) - 75.0, 2)
      }), 64.0),
      GreaterThan(ConstraintFunction((l: List[Double]) => {
        math.pow(l(0) - 75.0, 2) + math.pow(l(1) - 25.0, 2)
      }), 64.0),
      GreaterThan(ConstraintFunction((l: List[Double]) => {
        math.pow(l(0) - 75.0, 2) + math.pow(l(1) - 75.0, 2)
      }), 64.0)
    )
    val linear = List(
      GreaterThan(ConstraintFunction((l: List[Double]) => {
        l(1) / 2.0 + l(0) * 2.0
      }), 1.0),
      LessThan(ConstraintFunction((l: List[Double]) => {
        ((l(1) + 70.0) / 2.0) + (2.0*l(0) - 70.0)
      }), 1.0)
    )
    val combined1 = centerEllipse ++ linear
    val combined2 = disjointCircles ++ linear
  }

}
