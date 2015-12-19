package cilib
package example

import scalaz.NonEmptyList
import scalaz.effect._
import scalaz.effect.IO._
import scalaz.std.list._
import scalaz.syntax.apply._
import scalaz.syntax.traverse._

//import spire.math.Interval

object QuantumPSO extends SafeApp {
  import scalaz.std.list._
  import PSO._
  import Lenses._

  import monocle._
  import spire.algebra._
  import spire.implicits._

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
        p2      <- evalParticleWithPenalty(p)
        p3      <- updateVelocity(p2, v)
        updated <- updatePBestBounds(p3)
      } yield updated
    }

  def evalParticleWithPenalty[S](entity: Particle[S,Double]) =
    Entity.eval[S,Double](x => x)(entity).flatMap(e => Step.withCompare(o => RVar.point {
      penalize(o.opt)(e)
    }))

  def penalize[S](opt: Opt) = (e: Particle[S,Double]) => {

    e.pos.objective match {
      case scalaz.Maybe.Empty() => sys.error("???")
      case scalaz.Maybe.Just(obj) => obj match {
        case Multi(_) => sys.error("adads")
        case Single(f, v) =>
          val magnitude = Constraint.violationMagnitude(5.0, 15.0, v, e.pos.pos.toList)//.filter(_ > 0.0)

          f match {
            case Adjusted(_,_) => sys.error("???? HOW??")
            case a @ Feasible(_) => e//sys.error("Asdasd")
            case i @ Infeasible(_,_) =>
              (_position[S,Double] composePrism _solutionPrism[Double] composeLens _objectiveLens composePrism _singleObjective[Double] composeLens _singleFitness).modify((x: Fit) =>
                i.adjust(v => opt match {
                  case Min => v + magnitude
                  case Max => v - magnitude
                }))(e)
          }
          //println("magnitude: " + magnitude)
          //println("fit: " + e.pos.fit)
          // val result = (magnitude |@| e.pos.objective/*fit*/) { (mag, obj) => {
          //   obj match {
          //     case Single(f,v)
          //         case Adjusted(_,_)   => sys.error("??? How?")
          //     case Feasible(_)     => sys.error("asdsad")
          //     case i @ Infeasible(_,_) =>
          //       (_position[S,Double] composeOptional _fitness).modify((x: Fit) =>
          //         i.adjust(v => opt match {
          //           case Min => v + mag
          //           case Max => v - mag
          //         }))(e)
          //   }
          // }}.getOrElse(e)
          //
          // //  println("result: " + result)
          // result
      }
    }

  }

  // Usage
  val domain = spire.math.Interval(0.0, 100.0)^2
  //val r = Iteration.sync(quantumPSO[QuantumState,List](0.729844, 1.496180, 1.496180, Guide.pbest, Guide.gbest))
  val qpso = Iteration.sync(quantumPSO[QuantumState](0.729844, 1.496180, 1.496180, Guide.pbest, Guide.dominance((c,_) => c), RVar.point(50.0)))
  val qpsoDist = Iteration.sync(quantumPSO[QuantumState](0.729844, 1.496180, 1.496180, Guide.pbest, Guide.gbest, Dist.cauchy(0.0, 10.0)))

  def swarm = Position.createCollection(
    PSO.createParticle(x => Entity(QuantumState(x, x.zeroed, 0.0), x)))(domain, 40)

  // 20% of the swarm are charged particles
  def pop =
    swarm.map(coll => coll.take(20).map(x => x.copy(state = x.state.copy(charge = 0.05))) ++ coll.drop(20)).flatMap(RVar.shuffle)

  val comparison = Comparison.dominance(Max)

  /*override val runc: IO[Unit] = {
    val sum = Problems.spherical[Double]
    val alg = Runner.repeat(1000, qpso, swarm)

    putStrLn(alg.run(Comparison.quality(Min))(sum)/*.map(_.map(_.pos))*/.run(RNG.fromTime).toString)
   }*/

  override val runc: IO[Unit] = {
    val x = for {
      rng <- IO(RNG.fromTime)//init(11223344))
      //r <- IO(Runner.repeat(1000, qpso, pop).run(comparison)(Problems.spherical[Double]).run(rng))
      r <- IO(Environments.static(1000, comparison).run(rng))
      peaks <- IO(r._2._1.map(x => (x.height, x.location.list)))//.mkString("", "\t", "\n")).mkString("Peaks[", "", "]"))
      //positions <- IO(r._2._2.map(_.pos.pos))//.mkString("", "\t", "\n")).mkString("", "", ""))
      particles <- IO(r._2._2.filter(x => QuantumState.QSMemory._charge.get(x.state) < 0.01).map(_.pos.pos))
      violations <- IO(r._2._2.map(_.pos.objective.map(_.violations).getOrElse(List.empty).toString).mkString("Violation(", ",", ")"))
//      meanFit <- IO(r._2._2.traverse(_.pos.fit).cata(l => l.map(_.fold(
//        penalty = _.v, valid = _.v
//      )).suml / l.length, 0.0).toString)
    } yield {
      println("positionseverything: " + r._2._2.map(_.pos).mkString("\n"))
      println("violations: " + violations)
      println("peaks: " + peaks)
      (r,peaks,particles,violations)//,meanFit)
    }

    //    x.replicateM(30).flatMap(a => putStrLn(a.map(x => x._1.toString + x._2.toString + x._3.toString).mkString("\n")))
    //x.flatMap(a => putStrLn(a.toString))//.replicateM(30).flatMap(a => putStrLn(a.map(x => x.toString).mkString("List(",",\n",")")))

    x.flatMap { case (r, peaks, positions, violations) => IO {
      import org.jfree.chart._
      import org.jfree.data.xy._

      val (ax,ay) = peaks.map(a => a._2).map(_ match {
        case a :: b :: Nil => (a,b)
        case _ => sys.error("asdsd")
      }).unzip

      val (px, py) = positions.map(_ match {
        case a :: b :: Nil => (a,b)
        case _ => sys.error("asdsad")
      }).unzip

      val dataset = new DefaultXYDataset
      dataset.addSeries("MPBPeaks", Array(ax.toArray, ay.toArray))
      dataset.addSeries("positions", Array(px.toArray, py.toArray))

      val frame = new ChartFrame(
        "Peaks and positions",
        ChartFactory.createScatterPlot(
          "Plot",
          "X",
          "Y",
          dataset,
          org.jfree.chart.plot.PlotOrientation.VERTICAL,
          true,true,false
        )
      )
      frame.pack()
      frame.setVisible(true)
    }}
  }

  object MPB {

    def initialPeaks(s: Double, domain: NonEmptyList[spire.math.Interval[Double]]): RVar[List[Problems.PeakCone]] =
      Problems.initPeaks(5, domain)//(1 to 2).toList.traverse(_ => Problems.defaultPeak(domain, s))

    def iteration(
      swarm: List[cilib.Entity[cilib.example.QuantumPSO.QuantumState,Double]]
    ): Step[Double,List[cilib.Entity[cilib.example.QuantumPSO.QuantumState,Double]]] = {
      import scalaz.syntax.std.list._
      import scalaz.syntax.std.option._
      import scalaz.syntax.functor._

      //swarm.toNel.cata(nel => qpso.run(nel.list).run.map(_.map(penalize(Max))), Step.point(List.empty))
      qpso.run(swarm)
    }

    import scalaz.StateT
    def mpb(heightSeverity: Double, widthSeverity: Double): StateT[RVar, (List[Problems.PeakCone],List[cilib.Entity[cilib.example.QuantumPSO.QuantumState,Double]]), Eval[Double]] =
      StateT { case (peaks, pop) => {
        val newPeaks: RVar[List[Problems.PeakCone]] = RVar.point(peaks)//.traverse(_.update(heightSeverity, widthSeverity))
        newPeaks.map(np => ((np, pop), Problems.peakEval(np).constrainBy(EnvConstraints.centerEllipse)))
      }}
  }

  object Environments {
    import scalaz.StateT

    def run2(comparison: Comparison)(eval: Eval[Double]): StateT[RVar, (List[Problems.PeakCone], List[cilib.Entity[cilib.example.QuantumPSO.QuantumState,Double]]), List[cilib.Entity[cilib.example.QuantumPSO.QuantumState,Double]]] =
      StateT { case (peaks, pop) => {
        println("running with eval: " + eval.hashCode)
        MPB.iteration(pop).run(comparison)(eval).map(r => ((peaks, r.toList), r.toList))
      }}

    import scalaz.syntax.applicative._
    def staticE(n: Int, c: Comparison) =
      MPB.mpb(0.0, 0.0).flatMap(e => run2(c)(e).replicateM(n))
    def progressiveE(n: Int, c: Comparison) =
      MPB.mpb(1.0, 0.05).flatMap(run2(c)).replicateM(n)
    def abruptE(n: Int, c: Comparison) =
      MPB.mpb(10.0, 0.05).flatMap(e => run2(c)(e).replicateM(200)).replicateM(n / 200)
    def chaosE(n: Int, c: Comparison) =
      MPB.mpb(10.0, 0.05).flatMap(run2(c)).replicateM(n)

    def static(n: Int, c: Comparison): RVar[(List[Problems.PeakCone], List[cilib.Entity[cilib.example.QuantumPSO.QuantumState,Double]])] =
      for {
        peaks <- MPB.initialPeaks(0.0, domain)
        swarm <- pop
        e <- staticE(n, c).exec((peaks, swarm))
      } yield e

    def progressive(n: Int, c: Comparison) =
      for {
        peaks <- MPB.initialPeaks(1.0, domain)
        swarm <- pop
        e <- progressiveE(n, c).exec((peaks, swarm))
      } yield e

    def abrupt(n: Int, c: Comparison) =
      for {
        peaks <- MPB.initialPeaks(50.0, domain)
        swarm <- pop
        e <- abruptE(n, c).exec((peaks, swarm))
      } yield e

    def chaos(n: Int, c: Comparison) =
      for {
        peaks <- MPB.initialPeaks(50.0, domain)
        swarm <- pop
        e <- chaosE(n, c).exec((peaks, swarm))
      } yield e

  }

  object EnvConstraints {
    // constraints
    val centerCircle = List(
      GreaterThanEqual(ConstraintFunction((l: List[Double]) =>
        math.pow(l(0) - 50, 2) + math.pow(l(1) - 50, 2)
      ), 100.0))
    val centerEllipse = List(
      // ellipse in center of search space
      GreaterThanEqual(ConstraintFunction((l: List[Double]) =>
        math.pow((l(0) - 50.0) / 1.5, 2) + math.pow((l(1) - 50.0), 2)
      ), 100.0)
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
