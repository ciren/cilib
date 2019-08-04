package cilib
package example

import pso._
import pso.Heterogeneous._
import pso.PSO.{GCParams, defaultGCParams}

import syntax.algorithm._
import syntax.step._

import eu.timepit.refined.auto._

import scalaz.{Lens => _, _}
import scalaz.std.stream._
import scalaz.effect._
import scalaz.effect.IO.putStrLn

import scalaz.syntax.foldable._

import spire.math.Interval
import spire.implicits._

object HPSO extends SafeApp {

  type BehaviourPool = Pool[Behaviour[PState, Double, Params]]
  type Entity = User[Particle[PState, Double], Behaviour[PState, Double, Params]]

  case class PState(mem: Mem[Double], stag: Int)
  object PState {
    implicit val myParticleMemLens =
      monocle.Lens[PState, Mem[Double]](_.mem)(b => a => a.copy(mem = b))

    implicit object PStateMemory extends HasMemory[PState, Double] {
      def _memory = PState.myParticleMemLens ^|-> HasMemory.memMemory._memory
    }

    implicit object PStateVelocity extends HasVelocity[PState, Double] {
      def _velocity = PState.myParticleMemLens ^|-> HasVelocity.memVelocity._velocity
    }

    implicit object PStatePBestStagnation extends HasPBestStagnation[PState] {
      def _pbestStagnation = monocle.Lens[PState, Int](_.stag)(b => a => a.copy(stag = b))
    }
  }

  val cognitive = Guide.pbest[PState, Double]
  val social = Guide.gbest[PState]

  case class Params(gc1: GCParams, gc2: GCParams)
  object Params {
    def gc1 = scalaz.Lens.lensu[Params, GCParams]((a, b) => a.copy(gc1 = b), _.gc1)
    def gc2 = scalaz.Lens.lensu[Params, GCParams]((a, b) => a.copy(gc2 = b), _.gc2)
  }

  // used to create pools with changing parameters
  def createBehaviours(n: Double, i: Int): NonEmptyList[Behaviour[PState, Double, Params]] =
    NonEmptyList(
      Defaults
        .gbest(linear(0.9, 0.4, n)(i), 1.496, 1.496, cognitive, social)
        .flatMap(updateStagnation[PState, Double])
        .liftStepS[Params],
      Defaults
        .cognitive(linear(0.9, 0.4, n)(i), 1.496, cognitive)
        .flatMap(updateStagnation[PState, Double])
        .liftStepS[Params],
      Defaults
        .social(linear(0.9, 0.4, n)(i), 1.496, social)
        .flatMap(updateStagnation[PState, Double])
        .liftStepS[Params],
      Defaults
        .gcpso(linear(0.9, 0.4, n)(i), 1.496, 1.496, social)
        .flatMap(x => updateStagnation[PState, Double](x).liftStepS)
        .zoom(Params.gc1),
      Defaults
        .gcpso(linear(0.9, 0.4, n)(i), 1.496, 1.496, social)
        .flatMap(x => updateStagnation[PState, Double](x).liftStepS)
        .zoom(Params.gc2)
    )

  def linear(i: Double, f: Double, t: Double) = (x: Int) => (f - i) * (x / t) + i

  def behaviourProfile(p: BehaviourPool, xs: NonEmptyList[Entity]): NonEmptyList[Int] = {
    def one(acc: NonEmptyList[Int], p: BehaviourPool, xs: List[Entity]): NonEmptyList[Int] =
      xs match {
        case Nil => acc
        case h :: t =>
          val b = h.item
          val ind: Option[Int] = p.list.indexWhere(_ eq b)
          one(acc.zipWithIndex.map {
            case (x, index) =>
              ind.filter(_ == index).map(_ + 1).getOrElse(x)
          }, p, t)
      }

    one(p.map(_ => 0), p, xs.toList)
  }

  val particleBuilder = PSO.createParticle { (x: Position[Double]) =>
    Entity(PState(Mem(x, x.map(_ / 10.0)), 0), x)
  } _

  val bounds = Interval(-5.12, 5.12) ^ 2
  val env =
    Environment(cmp = Comparison.quality(Min),
                eval = Eval.unconstrained((xs: NonEmptyList[Double]) =>
                  Feasible(cilib.benchmarks.Benchmarks.spherical(xs))))

  val population =
    StepS
      .liftR[Double, BehaviourPool, NonEmptyList[Particle[PState, Double]]](
        Position.createCollection(particleBuilder)(bounds, 10))
      //.liftStepS[Double,BehaviourPool]
      .flatMap(assignRandom)
      .zoom(StepS.lensIso.get(scalaz.Lens.firstLens[BehaviourPool, Params]))

  //val iteration = Iteration.syncS(dHPSO[PState,List,Double, Params](10))
  val iteration = Iteration.syncS(fkPSO[PState, Double, Params](2, 2))

  type SI[A] = StepS[Double, (BehaviourPool, Params), A]

  val algorithm = (l: NonEmptyList[Entity]) => {
    import StepS._
    implicit val SP = StateT.stateTMonadState[BehaviourPool, Step[Double, ?]]
    val poolLens = scalaz.Lens.firstLens[BehaviourPool, Params]

    Range
      .inclusive(1, 1000)
      .toStream
      .map((_, iteration.run))
      .foldLeftM[SI, NonEmptyList[Entity]](l) { (a, c) =>
        for {
          newPopulation <- c._2(a)
          oldPool <- StepS(SP.get.zoom(poolLens))
          newPool = Pool.mkFromOldPool(
            oldPool.map(_.lastK(10)),
            createBehaviours(1000, c._1)
          )
          _ <- StepS(SP.put(newPool).zoom(poolLens))
        } yield Pool.updateUserBehaviours(oldPool, newPool)(newPopulation)
      }
  }

  val finalResult = population
    .flatMap(algorithm)
    .run((Pool.mkPoolListScore(Pool.mkZeroPool(createBehaviours(1000, 0))),
          Params(defaultGCParams, defaultGCParams)))
    .run(env)
    .run(RNG.fromTime)

  override val runc: IO[Unit] =
    finalResult._2 match {
      case -\/(error) =>
        throw error
      case \/-(value) =>
        putStrLn(behaviourProfile(value._1._1, value._2).toString)
    }

}
