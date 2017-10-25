package cilib
package example

import scalaz.effect._
import scalaz.effect.IO.putStrLn
import scalaz.std.list._
import scalaz.std.option._
import scalaz.syntax.traverse._
import spire.implicits._
import spire.math.Interval

import cilib.ga._
import Lenses._

object GAExample extends SafeApp {
  val sum = Problems.spherical

  def onePoint(xs: List[Individual]): RVar[List[Individual]] =
    xs match {
      case a :: b :: _ =>
        val point: RVar[Int] = Dist.uniformInt(Interval(0, a.pos.pos.size - 1))
        point.map(p => List(
          a.pos.take(p) ++ b.pos.drop(p),
          b.pos.take(p) ++ a.pos.drop(p)
        ).traverse(_.toNel.map(x => Entity((), Point(x, a.pos.boundary)))).getOrElse(List.empty[Individual]))
      case _ => sys.error("Incorrect number of parents")
    }

  def mutation(p_m: Double)(xs: List[Individual]): RVar[List[Individual]] = {

    if (p_m == 0.2) println(xs(0).pos.pos.index(0))
    val stdO = 2.0
    val step = 3.1
    val myList = xs(0).pos.pos.toList
      println(myList)
      println(myList.map(x => x + (stdO * step)))



    xs.traverse(x => {
      _position.get(x).traverse(z => for {
        zb <- Dist.stdNormal.flatMap(Dist.gaussian(0,_)).map(_ * z)
      } yield zb).map(a => _position.set(a)(x))
    })
  }

  val randomSelection = (l: List[Individual]) => RVar.sample(2, l).getOrElse(List.empty[Individual])
  val ga = GA.ga(0.7, randomSelection, onePoint, mutation(0.2))

  val swarm = Position.createCollection[Individual](x => Entity((), x))(Interval(-5.12,5.12)^3, 5)
//  val iter: Kleisli[Step[Double,?],List[GA.Individual],List[GA.Individual]] = Iteration.sync(ga).map(_.flatten)

  val cullingGA =
    Iteration.sync(ga).map(_.suml)
      .flatMapK(r => Step.withCompare(o => RVar.point(r.sortWith((x,y) => Comparison.fittest(x.pos,y.pos).apply(o))) map (_.take(5))))

  // Our IO[Unit] that runs at the end of the world
  override val runc: IO[Unit] =
    putStrLn(Runner.repeat(1, cullingGA, swarm).run(Comparison.dominance(Min))(sum).run(RNG.fromTime).toString)
}
