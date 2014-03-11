package cilib

import scala.Predef.{any2stringadd => _, _}
import scalaz._
import scalaz.std.list._
import scalaz.syntax.foldable._
import scalaz.syntax.traverse._
import spire.algebra._
import spire.implicits._

/*
  NB NB NB:
  =========
  The DE itself is nothing more than the implementation of DE/x/y/z which is simply the
  definition of the algorithm (per individual or population)

  The iteration scheme used for the algorithm then uses the DE algorithm definition to
  perform the algorithm synchronously or asynchronously
 */
object DE {
  type Selection[A] = NonEmptyList[A] => RVar[A]
  type Crossover[A] = (A, A) => Kleisli[RVar, Double, A]

  type TrialVector = Solution

  case class DEParams(beta: Double, p_c: Double)

  val betaL: Lens[DEParams, Double] = Lens.lensu((a, b) => a.copy(beta = b), _.beta)
  val p_cL:  Lens[DEParams, Double] = Lens.lensu((a, b) => a.copy(beta = b), _.p_c)

  final def de[A](betaL: Lens[A, Double], p_c: Lens[A, Double])
                 (x: Selection[Solution], y: Int, z: Crossover[Solution]): NonEmptyList[Solution] => Solution => Kleisli[RVar, A, Solution] =
    xs => current => Kleisli(params => for {
      target <- x(xs)
      diffs <- diffVectors(y, betaL get params) run xs
      cross <- z(current, target + diffs).run(p_c get params)
    } yield cross)

  private final def diffVectors(n: Int, beta: Double): Kleisli[RVar, NonEmptyList[Solution], Solution] =
    Kleisli(xs => {
      val l = xs.list
      (0 until n).toList.traverse(_ => RVar.choices(2, xs)).map(diffs => {
        println("selected diffs: " + diffs)
        val parts = diffs map {
          case a :: b :: _ => a - b
          case _ => sys.error("Cannot select two vectors for difference vector calculation.")
        }
        beta *: parts.reduceLeft(_ + _)
      })
    })

  def binomial(n: Int): Kleisli[RVar, Double, List[Boolean]] =
    Kleisli(p_c =>
      for {
        rngs <- Dist.stdUniform.map(_ < p_c).replicateM(n)
        point <- Dist.uniformInt(0, n - 1)
      } yield rngs.zipWithIndex.map(x => x._1 && x._2 != point)
    )

  def exponential(n: Int): Kleisli[RVar, Double, List[Boolean]] = {
    ???
  }

  def crossover(points: Int => Kleisli[RVar, Double, List[Boolean]]): (Solution, Solution) => Kleisli[RVar, Double, Solution] =
    (parent: Solution, trial: Solution) =>
      points(parent.length).map { p =>
        (p zip (parent zip trial)).map(x => if (x._1) x._2._2 else x._2._1).toVector
      }

  def bin = crossover(binomial)
  def exp = crossover(exponential)

  def mkEntity(x: Solution) =
    Entity(x, Invalid)

  def syncIter[A](alg: NonEmptyList[Solution] => Solution => Kleisli[RVar, A, Solution]): A => Kleisli[RVar, NonEmptyList[Entity], NonEmptyList[Entity]] =
    params => Kleisli(xs => {
      val solutions = xs.map(_.x)
      val a = alg(solutions)
      solutions.traverseU(a(_).run(params).map(mkEntity))
    })

  val compositionTest = syncIter(de(betaL, p_cL)(RVar.choose, 1, bin))
}
