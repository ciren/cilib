package cilib

import scala.Predef.{any2stringadd => _, _}
import scalaz._
import Scalaz._

/*
  NB NB NB:
  =========
  The DE itself is nothing more than the implementation of DE/x/y/z which is simply the
  definition of the algorithm (per individual or population)

  The iteration scheme used for the algorithm then uses the DE algorithm definition to
  perform the algorithm synchronously or asynchronously
 */
object DE {

  import spire.algebra.{ Monoid }
  import spire.math._
  import scalaz._

  type Selection[F[_], A] = NonEmptyList[Position[F, A]] => RVar[Position[F, A]]
  type Crossover[F[_], A] = (Position[F, A], Position[F, A]) => Kleisli[RVar, Double, Position[F, A]]

/*  def de[F[_]: Monad :Traverse, A: Numeric: Monoid](x: Selection[F, A], y: Int, z: Crossover[F, A]): Position[F, A] => NonEmptyList[Position[F, A]] => ReaderT[RVar, Opt, Position[F, A]] = {
    import Position._
    current => collection => Kleisli(opt => for {
      target <- x(collection)
      diffs <- diffVectors(y)(collection) getOrElse current.map(_ => Monoid[A].id)
      cross <- z(target, target + diffs) run 0.5
    } yield Fitness.compare(current, cross) run opt)
  }

  final def diffVectors[F[_]: Traverse, A: Numeric](n: Int)(xs: NonEmptyList[Position[F, A]]) = {
    import Position._

    def difference = RVar.choices(2, xs.list).map(_ match {
      case a :: b :: _ => a - b
      case _ => sys.error("impossible")
    })

    (0 until n).toList.traverseU(_ => difference).map(/*beta *:*/ _.reduceLeft(_ + _))
  }


 */

  // ----------------

  // def binomial[F[_]: Traverse, A](parent: Position[F, A]): ReaderT[RVar, Double, List[Boolean]] = {
  //   import Scalaz._
  //   (parent.traverseU(_ => Dist.stdUniform.map(_ < 0.5)) |@| Dist.uniformInt(0, n - 1)) { _.zipWithIndex.map(x => x._1 || x._2 == _)}
  // }
    // Kleisli(p_c => for {
    //   rngs <- Dist.stdUniform.map(_ < p_c) replicateM n
    //   point <- Dist.uniformInt(0, n - 1)
    // } yield rngs.zipWithIndex.map(x => x._1 || x._2 == point))

  def exponential(n: Int): Kleisli[RVar, Double, List[Boolean]] =
    ???

  // def crossover[F[_]: Monad, A](points: Position[F, A] => Kleisli[RVar, Double, List[Boolean]]): Crossover[F, A] =
  //   (parent: Position[F, A], trial: Position[F, A]) => for {
  //     a <- points(parent)
  //     z <- Kleisli[RVar, Double, Position[F, (A, A)]]((_: Double) => RVar.point(parent zip trial))
  //   } yield z.map(x => a.zip(x))


      // points(parent).map { p =>
      //   (p zip (parent zip trial)).map(x => if (x._1) x._2._2 else x._2._1).toVector
      // }

  // def bin[F[_]: Traverse, A] = crossover[F, A](binomial)

/*  import Entity._

 type Selection[A] = NonEmptyList[A] => RVar[A]
  //type Crossover[A] = (A, A) => Kleisli[RVar, Double, A]

  type TrialVector = Solution

  case class DEParams(scale: Double, p_c: Double)

  val scaleL: Lens[DEParams, Double] = Lens.lensu((a, b) => a.copy(scale = b), _.scale)
  val p_cL:  Lens[DEParams, Double] = Lens.lensu((a, b) => a.copy(p_c = b), _.p_c)

  final def de[A](scaleL: Lens[A, Double], p_c: Lens[A, Double])
    (x: Selection[MeasuredEntity], y: Int, z: Crossover[MeasuredEntity]):
      Opt => NonEmptyList[MeasuredEntity] => MeasuredEntity => Kleisli[RVar, A, MeasuredEntity] =
    opt => xs => current => Kleisli(params =>
      for {
        target <- x(xs)
        c = current.pos
        t = target.pos
        diffs <- diffVectors(y, scaleL get params)(xs.list.filterNot(x => x == current || x == target).map(_.pos)) getOrElse Vector.fill(c.length)(0.0)
        cross <- z.run(c, t + diffs).run(p_c get params)
      } yield Fitness.compare(current, cross)) // select parent or offspring

 private final def diffVectors(n: Int, beta: Double)(xs: List[Solution]) = {
 def difference = RVar.choices(2, xs).map(_ match {
 case a :: b :: _ => a - b
 case _ => sys.error("impossible")
 })

 (0 until n).toList.traverseU(_ => difference).map(beta *: _.reduceLeft(_ + _))
 }

  def binomial(n: Int): ReaderT[RVar, Double, List[Boolean]] =
    Kleisli(p_c => for {
      rngs <- Dist.stdUniform.map(_ < p_c).replicateM(n)
      point <- Dist.uniformInt(0, n - 1)
    } yield rngs.zipWithIndex.map(x => x._1 || x._2 == point))

  def exponential(n: Int): Kleisli[RVar, Double, List[Boolean]] =
    ???

  def crossover(points: Int => Kleisli[RVar, Double, List[Boolean]]): Crossover[Solution] =
    Crossover((parent: Solution, trial: Solution) =>
      points(parent.length).map { p =>
        (p zip (parent zip trial)).map(x => if (x._1) x._2._2 else x._2._1).toVector
      })

  def bin = crossover(binomial)
  def exp = crossover(exponential)

  // A => M[B]
  def syncIter[A](alg: Opt => NonEmptyList[MeasuredEntity] => MeasuredEntity => Kleisli[RVar, A, MeasuredEntity]): Opt => A => Kleisli[RVar, NonEmptyList[MeasuredEntity], NonEmptyList[MeasuredEntity]] =
    opt => params => Kleisli(xs => {
      val a = alg(opt)(xs)
      xs.traverseU(x => a(x).run(params))
    })

  def runner[A](xs: NonEmptyList[Entity], n: Int, alg: Opt => A => Kleisli[RVar, NonEmptyList[Entity], NonEmptyList[Entity]]): Opt => A => NonEmptyList[Entity] =
    (o: Opt) => (params: A) => {
    val aa = alg(o)(params)
    val combined = (1 to n).foldLeft(aa)((a, c) => a >=> a)
    combined.run(xs).run(RNG.init()).run._2
  }

  val compositionTest = syncIter(de(scaleL, p_cL)(RVar.choose, 1, bin map mkEntity))

  // represents the function (Solution, Solution) => Kleisli[RVar, Double, A]
  case class Crossover[A](run: (Solution, Solution) => Kleisli[RVar, Double, A]) {
    def map[B](f: A => B) =
      Crossover(run(_, _) map f)
  }
 */
}
