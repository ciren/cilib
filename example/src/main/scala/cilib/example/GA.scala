package cilib
package example

import scalaz._
import Scalaz._
import scalaz.effect._
import scalaz.effect.IO.putStrLn

import eu.timepit.refined.auto._

import spire.implicits._
import spire.math.Interval

import cilib.ga._
import Lenses._

object GAExample extends SafeApp {
  type Ind = Individual[Unit]

  val bounds = Interval(-5.12, 5.12) ^ 30

  val env =
    Environment(cmp = Comparison.dominance(Min),
                eval = Eval.unconstrained((xs: NonEmptyList[Double]) =>
                  Feasible(cilib.benchmarks.Benchmarks.spherical(xs))))

  def onePoint(xs: List[Ind]): RVar[List[Ind]] =
    xs match {
      case a :: b :: _ =>
        val point: RVar[Int] = Dist.uniformInt(Interval(0, a.pos.pos.size - 1))
        point.map(
          p =>
            List(
              a.pos.take(p) ++ b.pos.drop(p),
              b.pos.take(p) ++ a.pos.drop(p)
            ).traverse(_.toNel.map(x => Entity((), Position(x, a.pos.boundary))))
              .getOrElse(List.empty[Ind]))
      case _ => sys.error("Incorrect number of parents")
    }

  def mutation(p_m: Double)(xs: List[Ind]): RVar[List[Ind]] =
    xs.traverse(x => {
      _position.modifyF((p: Position[Double]) =>
        p.traverse(z =>
          for {
            za <- Dist.stdUniform.map(_ < p_m)
            zb <- if (za) Dist.stdNormal.flatMap(Dist.gaussian(0, _)).map(_ * z) else RVar.pure(z)
          } yield zb))(x)
    })

  val randomSelection: NonEmptyList[Ind] => RVar[List[Ind]] =
    (l: NonEmptyList[Ind]) => RVar.sample(2, l).map(_.getOrElse(List.empty))

  val ga: NonEmptyList[Ind] => (Ind => Step[Double, List[Ind]]) =
    GA.ga(0.7, randomSelection, onePoint, mutation(0.2))

  val swarm = Position.createCollection[Ind](x => Entity((), x))(bounds, 20)

  val cullingGA: Kleisli[Step[Double, ?], NonEmptyList[Ind], NonEmptyList[Ind]] =
    Iteration
      .sync(ga)
      .map(_.suml)
      .flatMapK(
        r =>
          Step
            .withCompare(o => r.sortWith((x, y) => Comparison.fitter(x.pos, y.pos).apply(o)))
            .map(_.take(20).toNel.getOrElse(sys.error("asdas"))))

  // Our IO[Unit] that runs at the end of the world
  override val runc: IO[Unit] =
    putStrLn(exec.Runner.repeat(1000, cullingGA, swarm).run(env).run(RNG.fromTime).toString)
}
