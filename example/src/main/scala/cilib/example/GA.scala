package cilib
package example

import scalaz.Kleisli
import scalaz.effect._
import scalaz.effect.IO.putStrLn
import scalaz.std.list._
import scalaz.syntax.std.list._
import scalaz.syntax.traverse._
import spire.implicits._
import spire.math.Interval

import cilib.ga._
import Lenses._

object GAExample extends SafeApp {
  val sum = Problems.spherical[Double]

  def onePoint(xs: List[Individual]): RVar[List[Individual]] =
    xs match {
      case a :: b :: _ =>
        val point: RVar[Int] = Dist.uniformInt(Interval(0, a.pos.pos.size - 1))
        point.map(p => List(
          a.pos.pos.take(p) ++ b.pos.pos.drop(p),
          b.pos.pos.take(p) ++ a.pos.pos.drop(p)
        ).map(x => Entity((), Point(x, a.pos.boundary))))
      case _ => sys.error("Incorrect number of parents")
    }

  def mutation(p_m: Double)(xs: List[Individual]): RVar[List[Individual]] = {
    xs.traverse(x => {
      _position.get(x).traverse(z => for {
        za <- Dist.stdUniform.map(_ < p_m)
        zb <- if (za) Dist.stdNormal.flatMap(Dist.gaussian(0,_)).map(_ * z) else RVar.point(z)
      } yield zb).map(a => _position.set(a)(x))
    })
  }

  val randomSelection = (l: List[Individual]) => RVar.sample(2, l).getOrElse(List.empty[Individual])
  val ga = GA.ga(0.7, randomSelection, onePoint, mutation(0.2))

  val swarm = Position.createCollection[Individual](x => Entity((), x))(Interval(-5.12,5.12)^30, 20)
//  val iter: Kleisli[Step[Double,?],List[GA.Individual],List[GA.Individual]] = Iteration.sync(ga).map(_.flatten)

  val cullingGA = Iteration.sync(ga) map (_.suml) flatMapK (r => Step.withCompare(o => RVar.point(r.sortWith((x,y) => Comparison.fittest(x.pos,y.pos).run(o))))) map (_.take(20))

  // Our IO[Unit] that runs at the end of the world
  override val runc: IO[Unit] =
    putStrLn(Runner.repeat(1000, cullingGA, swarm).run(Comparison.dominance(Min))(sum).run(RNG.fromTime).toString)
}
