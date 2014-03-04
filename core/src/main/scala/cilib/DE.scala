package cilib

import scala.Predef.{any2stringadd => _, _}
import scalaz._
import scalaz.std.list._
import scalaz.syntax.foldable._
import scalaz.Id.Id
import spire.algebra._
import spire.implicits._

/*
 NB NB NB:
 =========

 Need to make the de as such:

 The DE itself is nothing more than the implementation of DE/x/y/z which is simply the
 definition of the algorithm (per individual or population)

 The iteration scheme used for the algorithm then uses the DE algorithm definition to
 perform the algorithm synchronously or asynchronously
 */
object DE {
  import Scalaz._

  type Selection[A] = NonEmptyList[A] => RVar[A] //Kleisli[RVar, NonEmptyList[A], A]
  type Crossover[A] = (Solution, Solution) => RVar[Solution]

  type TrialVector = Solution

  final def de(x: Selection[Solution], y: Int, z: Crossover[Solution]): NonEmptyList[Solution] => Solution => RVar[Solution] =
    xs => current => for {
      target <- x(xs)
      diffs <- diffVectors(y).run(xs)
      cross <- z(current, target + diffs)
    } yield cross

  private final def diffVectors(n: Int): Kleisli[RVar, NonEmptyList[Solution], Solution] =
    Kleisli(xs => {
      val l = xs.list
      (1 to n).toList.traverse(_ => RVar.choices(2, xs)).map(diffs => {
        println("selected diffs: " + diffs)
        val parts = diffs map {
          case a :: b :: _ => a - b
          case _ => sys.error("Cannot select two vectors for difference vector calculation.")
        }
        0.5 *: parts.reduceLeft(_ + _)
      })
    })

  def binomial(p_r: Double)(n: Int): RVar[List[Boolean]] =
    for {
      rngs <- Dist.stdUniform.map(_ < p_r).replicateM(n)
      point <- Dist.uniformInt(0, n)
    } yield rngs.zipWithIndex.map(x => if (x._1 && x._2 != point) true else false)

  def exponential(p_r: Double)(n: Int): RVar[List[Boolean]] =
    ???

  def crossover(points: Int => RVar[List[Boolean]]): (Solution, Solution) => RVar[Solution] = (parent: Solution, trial: Solution) =>
    points(parent.length).map(p => (p zip (parent zip trial)).map(x => if (x._1) x._2._2 else x._2._2).toVector)

  def bin = crossover(binomial(0.5))
  def exp = crossover(exponential(0.8))

  def mkEntity(x: Solution) =
    Entity(x, Invalid)

  def syncIter(alg: NonEmptyList[Solution] => Solution => RVar[Solution]): Kleisli[RVar, NonEmptyList[Entity], NonEmptyList[Entity]] =
    Kleisli(xs => {
      val solutions = xs.map(_.x)
      val a = alg(solutions)
      solutions.traverse(a(_).map(mkEntity))
    })

  val compositionTest = syncIter(de(RVar.choose, 1, bin))
}
