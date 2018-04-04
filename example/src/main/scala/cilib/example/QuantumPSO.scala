package cilib
package example

//import scalaz._
//import Scalaz._
import scalaz.effect._

//import cilib.pso._

object QuantumPSO extends SafeApp {
  /* import PSO._
  import Lenses._
  import spire.implicits._

  import monocle._

  case class QuantumState(b: Position[Double], v: Position[Double], charge: Double)

  object QuantumState {
    implicit object QSMemory
        extends HasMemory[QuantumState,Double]
        with HasVelocity[QuantumState,Double]
        with HasCharge[QuantumState] {
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
    cloudR: (Position[Double],Position[Double]) => RVar[Double])(
    implicit C: HasCharge[S], V: HasVelocity[S,Double], M: HasMemory[S,Double]
  ): List[Particle[S,Double]] => Particle[S,Double] => Step[Double,Particle[S,Double]] =
    collection => x => {
      for {
        cog     <- cognitive(collection, x)
        soc     <- social(collection, x)
        v       <- stdVelocity(x, soc, cog, w, c1, c2)
        p       <- if (C._charge.get(x.state) < 0.01) stdPosition(x, v)
                   else quantum(x, cloudR(soc, cog), (_,_) => Dist.stdUniform).flatMap(replace(x, _))
        p2      <- evalParticleWithPenalty(p)
        p3      <- updateVelocity(p2, v)
        updated <- updatePBestBounds(p3)
      } yield updated
    }

  def evalParticleWithPenalty[S](entity: Particle[S,Double]) =
    Entity.eval[S,Double](x => x)(entity).flatMap(e => Step.withCompare(o => RVar.pure {
      penalize(o.opt)(e)
    }))

  def penalize[S](opt: Opt) = (e: Particle[S,Double]) => {

    e.pos.objective match {
      case None => sys.error("???")
      case Some(obj) => obj match {
        case Multi(_) => sys.error("adads")
        case Single(f, v) =>
          val magnitude = Constraint.violationMagnitude(5.0, 15.0, v, e.pos.pos)//.filter(_ > 0.0)

          f match {
            case Adjusted(_,_) => sys.error("???? HOW??")
            case Feasible(_) => e//sys.error("Asdasd")
            case i @ Infeasible(_,_) =>
              (_position[S,Double] composeOptional _singleFitness[Double]).modify((x: Fit) =>
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
  val qpso = Iteration.sync(quantumPSO[QuantumState](0.729844, 1.496180, 1.496180, Guide.pbest, Guide.dominance(Selection.star), (_,_) => RVar.pure(50.0)))
  val qpsoDist = Iteration.sync(quantumPSO[QuantumState](0.729844, 1.496180, 1.496180, Guide.pbest, Guide.gbest, (_,_) => Dist.cauchy(0.0, 10.0)))

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

  override val runc: IO[Unit] = IO {}
//     val x = for {
//       rng <- IO(RNG.fromTime)//init(11223344))
//       //r <- IO(Runner.repeat(1000, qpso, pop).run(comparison)(Problems.spherical[Double]).run(rng))
//       r <- IO(Environments.static(1000, comparison).run(rng))
//       peaks <- IO(r._2._1.map(x => (x.height, x.location.list)))//.mkString("", "\t", "\n")).mkString("Peaks[", "", "]"))
//       //positions <- IO(r._2._2.map(_.pos.pos))//.mkString("", "\t", "\n")).mkString("", "", ""))
//       particles <- IO(r._2._2.filter(x => QuantumState.QSMemory._charge.get(x.state) < 0.01).map(_.pos.pos))
//       violations <- IO(r._2._2.map(_.pos.objective.map(_.violations).getOrElse(List.empty).toString).mkString("Violation(", ",", ")"))
// //      meanFit <- IO(r._2._2.traverse(_.pos.fit).cata(l => l.map(_.fold(
// //        penalty = _.v, valid = _.v
// //      )).suml / l.length, 0.0).toString)
//     } yield {
//       println("positionseverything: " + r._2._2.map(_.pos).mkString("\n"))
//       println("violations: " + violations)
//       println("peaks: " + peaks)
//       (r,peaks,particles,violations)//,meanFit)
//     }

//     //    x.replicateM(30).flatMap(a => putStrLn(a.map(x => x._1.toString + x._2.toString + x._3.toString).mkString("\n")))
//     //x.flatMap(a => putStrLn(a.toString))//.replicateM(30).flatMap(a => putStrLn(a.map(x => x.toString).mkString("List(",",\n",")")))

//     x.flatMap { case (r, peaks, positions, violations) => IO {
//       import org.jfree.chart._
//       import org.jfree.data.xy._

//       val (ax,ay) = peaks.map(a => a._2).map(_ match {
//         case ICons(a, ICons(b, INil())) => (a,b)
//         case _ => sys.error("asdsd")
//       }).unzip

//       val (px, py) = positions.map(l => (l.index(0) |@| l.index(1)) { (a, b) => (a,b) }.getOrElse(sys.error("asdsad"))).unzip

//       val dataset = new DefaultXYDataset
//       dataset.addSeries("MPBPeaks", Array(ax.toArray, ay.toArray))
//       dataset.addSeries("positions", Array(px.toArray, py.toArray))

//       val frame = new ChartFrame(
//         "Peaks and positions",
//         ChartFactory.createScatterPlot(
//           "Plot",
//           "X",
//           "Y",
//           dataset,
//           org.jfree.chart.plot.PlotOrientation.VERTICAL,
//           true,true,false
//         )
//       )
//       frame.pack()
//       frame.setVisible(true)
//     }}
//   }

  object MPB {

    def initialPeaks(/*s: Double,*/ domain: NonEmptyList[spire.math.Interval[Double]]): RVar[NonEmptyList[Problems.PeakCone]] =
      Problems.defaultPeaks(5, domain)

    def iteration(
      swarm: List[cilib.Entity[cilib.example.QuantumPSO.QuantumState,Double]]
    ): Step[Double,List[cilib.Entity[cilib.example.QuantumPSO.QuantumState,Double]]] = {
      //swarm.toNel.cata(nel => qpso.run(nel.list).run.map(_.map(penalize(Max))), Step.pure(List.empty))
      qpso.run(swarm)
    }

    import scalaz.StateT
    def mpb(heightSeverity: Double, widthSeverity: Double): StateT[RVar, (NonEmptyList[Problems.PeakCone],List[cilib.Entity[cilib.example.QuantumPSO.QuantumState,Double]]), Eval[NonEmptyList,Double]] =
      StateT { case (peaks, pop) => {
        val newPeaks: RVar[NonEmptyList[Problems.PeakCone]] = RVar.pure(peaks)//.traverse(_.update(heightSeverity, widthSeverity))
        newPeaks.map(np => ((np, pop), Problems.peakEval(np).constrain(EnvConstraints.centerEllipse)))
      }}
  }

  object EnvConstraints {
    // constraints
    val centerCircle = List(
      GreaterThanEqual(ConstraintFunction((l: NonEmptyList[Double]) =>
        (l.index(0) |@| l.index(1)) { (a,b) =>
          math.pow(a - 50, 2) + math.pow(b - 50, 2)
        }.getOrElse(0.0)), 100.0)
    )
    val centerEllipse = List(
      // ellipse in center of search space
      GreaterThanEqual(ConstraintFunction((l: NonEmptyList[Double]) =>
        (l.index(0) |@| l.index(1)) { (a,b) =>
          math.pow((a - 50.0) / 1.5, 2) + math.pow((b - 50.0), 2)
          }.getOrElse(0.0)), 100.0)
    )
    val disjointCircles = List(
      GreaterThan(ConstraintFunction((l: NonEmptyList[Double]) =>
        (l.index(0) |@| l.index(1)) { (a, b) =>
          math.pow(a - 25.0, 2) + math.pow(b - 25.0, 2)
        }.getOrElse(0.0)), 64.0),
      GreaterThan(ConstraintFunction((l: NonEmptyList[Double]) =>
        (l.index(0) |@| l.index(1)) { (a, b) =>
          math.pow(a - 25.0, 2) + math.pow(b - 75.0, 2)
        }.getOrElse(0.0)), 64.0),
      GreaterThan(ConstraintFunction((l: NonEmptyList[Double]) =>
        (l.index(0) |@| l.index(1)) { (a, b) =>
          math.pow(a - 75.0, 2) + math.pow(b - 25.0, 2)
        }.getOrElse(0.0)), 64.0),
      GreaterThan(ConstraintFunction((l: NonEmptyList[Double]) =>
        (l.index(0) |@| l.index(1)) { (a, b) =>
          math.pow(a - 75.0, 2) + math.pow(b - 75.0, 2)
        }.getOrElse(0.0)), 64.0)
    )
    val linear = List(
      GreaterThan(ConstraintFunction((l: NonEmptyList[Double]) =>
        (l.index(0) |@| l.index(1)) { (a, b) =>
          a * 2.0 + b / 2.0
        }.getOrElse(0.0)), 1.0),
      LessThan(ConstraintFunction((l: NonEmptyList[Double]) =>
        (l.index(0) |@| l.index(1)) { (a, b) =>
          ((b + 70.0) / 2.0) + (2.0*a - 70.0)
        }.getOrElse(0.0)), 1.0)
    )
    val combined1 = centerEllipse ++ linear
    val combined2 = disjointCircles ++ linear
  }
 */
}
