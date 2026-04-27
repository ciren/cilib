package cilib
package example

import cilib.NonEmptyVector
import cilib.exec._
import cilib.pso._
import zio._
import zio.stream._

object QuantumPSO extends zio.ZIOAppDefault {
  import PSO._

  case class QuantumState(b: Position[Double], v: Position[Double], charge: Double)

  type QuantumParticle = Particle[QuantumState, Double]

  object QuantumState {
    implicit object QSMemory
        extends HasMemory[QuantumState, Double]
        with HasVelocity[QuantumState, Double]
        with HasCharge[QuantumState] {
      def _memory: Lens[QuantumState, Position[Double]]   = Lens[QuantumState, Position[Double]](
        state => state.b,
        (state, newB) => state.copy(b = newB)
      )
      def _velocity: Lens[QuantumState, Position[Double]] = Lens[QuantumState, Position[Double]](
        state => state.v,
        (state, newV) => state.copy(v = newV)
      )
      def _charge: Lens[QuantumState, Double]             = Lens[QuantumState, Double](
        state => state.charge,
        (state, newCharge) => state.copy(charge = newCharge)
      )
    }
  }

  def quantumPSO(
    w: Double,
    c1: Double,
    c2: Double,
    cognitive: Guide[QuantumState, Double],
    social: Guide[QuantumState, Double],
    cloudR: (Position[Double], Position[Double]) => RVar[Double]
  )(implicit
    C: HasCharge[QuantumState],
    V: HasVelocity[QuantumState, Double],
    M: HasMemory[QuantumState, Double]
  ): NonEmptyVector[QuantumParticle] => QuantumParticle => Step[QuantumParticle] =
    collection =>
      x =>
        for {
          cog     <- cognitive(collection, x)
          soc     <- social(collection, x)
          v       <- stdVelocity(x, soc, cog, w, c1, c2)
          p       <- if (C._charge.get(x.state) < 0.01) stdPosition(x, v)
                     else quantum(x.pos, cloudR(soc, cog), (_, _) => Dist.stdUniform).flatMap(replace(x, _))
          p2      <- Step.eval(p)(identity)
          p3      <- updateVelocity(p2, v)
          updated <- updatePBestBounds(p3)
        } yield updated

  // Usage
  val domain: NonEmptyVector[Interval] = Interval(0.0, 100.0) ^ 2

  val qpso: Kleisli[Step, NonEmptyVector[QuantumParticle], NonEmptyVector[QuantumParticle]] =
    Kleisli(
      Iteration.sync(
        quantumPSO(
          0.729844,
          1.496180,
          1.496180,
          Guide.pbest,
          Guide.dominance(Selection.star),
          (_, _) => RVar.pure(50.0)
        )
      )
    )

  def swarm: cilib.RVar[NonEmptyVector[Particle[QuantumState, Double]]] =
    Position
      .createCollection(PSO.createParticle(x => Entity(QuantumState(x, x.zeroed, 0.0), x)))(domain, positiveInt(40))

  def pop: RVar[NonEmptyVector[QuantumParticle]] =
    swarm.map { coll =>
      val C          = implicitly[HasCharge[QuantumState]]
      val chargeLens = Lenses._state[QuantumState, Double].compose(C._charge)

      coll.zipWithIndex.map { case (current, index) =>
        chargeLens.modify(z => if (index % 2 == 1) 0.1 else z)(current)
      }
    }.flatMap(RVar.shuffle)

  val comparison: Comparison = Comparison.dominance(Opt.Max)

  val problemStream: UStream[Problem] = Runner.staticProblem(
    "spherical",
    Eval.unconstrained((x: NonEmptyVector[Double]) => Fit.feasible(ExampleHelper.spherical(x)))
  )
  val algStream: UStream[Algorithm[
    Kleisli[Step, NonEmptyVector[Particle[QuantumState, Double]], NonEmptyVector[Particle[QuantumState, Double]]]
  ]] =
    Runner.staticAlgorithm("quantumPSO", qpso)

  def run: ZIO[cilib.example.QuantumPSO.Environment with ZIOAppArgs with Scope, Any, Any] =
    Runner
      .foldStep(
        comparison,
        RNG.fromTime,
        pop,
        algStream,
        problemStream,
        (x: NonEmptyVector[Particle[QuantumState, Double]], _: Eval[NonEmptyVector]) => RVar.pure(x)
      )
      .take(1000)
      .runLast
      .fold(eh => println(eh.toString), ah => println(ah.toString))
}
