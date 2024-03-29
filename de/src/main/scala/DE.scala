package cilib
package de

import zio.ChunkBuilder
import zio.prelude.newtypes.Natural
import zio.prelude.{ Comparison => _, _ }

import Predef.{ any2stringadd => _, _ }

object DE {

  def de[S, A: Numeric: Equal: NumericTo](
    p_r: Double,
    p_m: Double,
    targetSelection: NonEmptyVector[Individual[S, A]] => Step[(Individual[S, A], Position[A])],
    y: Natural,
    z: (Double, Position[A]) => RVar[NonEmptyVector[Boolean]] // Double check this shape
  ): NonEmptyVector[Individual[S, A]] => Individual[S, A] => Step[Individual[S, A]] =
    collection =>
      x =>
        for {
          evaluated          <- Step.eval(x)(identity)
          trial              <- basicMutation(p_m, targetSelection, y, collection, x)
          pivots             <- Step.liftR(z(p_r, evaluated.pos))
          offspring           = crossover(x, trial, pivots)
          evaluatedOffspring <- Step.eval(offspring)(identity)
          fittest            <- Comparison.fittest(evaluated, evaluatedOffspring)
        } yield fittest

  def basicMutation[S, A: Equal: scala.math.Numeric: NumericTo](
    p_m: Double,
    selection: NonEmptyVector[Individual[S, A]] => Step[(Individual[S, A], Position[A])],
    y: Natural,
    collection: NonEmptyVector[Individual[S, A]],
    x: Individual[S, A]
  ): Step[Position[A]] = {
    def createPairs[Z](acc: List[(Z, Z)], xs: List[Z]): List[(Z, Z)] =
      xs match {
        case Nil           => acc
        case a :: b :: xss => createPairs((a, b) :: acc, xss)
        case _             => sys.error("Incorrect number of individuals")
      }

    val target: Step[(Individual[S, A], Position[A])] = selection(collection)
    val filtered                                      = target.map(t => collection.toChunk.filterNot(a => a === t._1 || a === x))
    val pairs: Step[List[Position[A]]]                =
      filtered.flatMap(x =>
        NonEmptyVector.fromIterableOption(x) match {
          case Some(l) =>
            Step.liftR(
              RVar
                .shuffle(l)
                .map(a =>
                  createPairs(List.empty[(Individual[S, A], Individual[S, A])], a.toChunk.toList.take(2 * y)).map {
                    case (z1, z2) => z1.pos - z2.pos
                  }
                )
            )
          case None    =>
            Step.pure(List.empty)
        }
      )

    for {
      t <- target
      p <- pairs
    } yield p.foldLeft(t._2)((a, c) => a + (NumericTo[A].fromDouble(p_m) *: c))
  }

  def crossover[S, A](target: Individual[S, A], trial: Position[A], pivots: NonEmptyVector[Boolean]) =
    target.copy(pos = target.pos.zip(trial).zip(Position(pivots, trial.boundary)).map {
      case ((a, b), p) => // Position apply function :(
        if (p) b else a
    })

  def bin[F[+_], A](
    p_r: Double,
    parent: F[A]
  )(implicit F: NonEmptyForEach[F]): RVar[NonEmptyVector[Boolean]] =
    Dist
      .uniformInt(0, F.size(parent) - 1)
      .flatMap { j =>
        F.zipWithIndex(parent).forEach1 { case (_, i) =>
          if (i == j) RVar.pure(true)
          else Dist.stdUniform.map(_ < p_r)
        }
      }
      .map { x =>
        val chunk = F.reduceMapLeft(x)(ChunkBuilder.make() += _)(_ += _).result().toList
        NonEmptyVector(chunk.head, chunk.tail: _*)
      }

  def exp[F[+_], A](
    p_r: Double,
    parent: F[A]
  )(implicit F: NonEmptyForEach[F]): RVar[NonEmptyVector[Boolean]] = {
    val length = F.size(parent)
    for {
      start   <- Dist.uniformInt(0, length - 1)
      circular = Iterator.continually((0 to length).toList).flatMap(x => x)
      randoms <- Dist.stdUniform.replicateM(length).map(_.toList)
    } yield {
      val paired   = circular.drop(start).take(length).toList.zip(randoms)
      val adjacent = paired.head :: paired.tail.takeWhile(t => t._2 < p_r)

      val options = (0 to length)
        .foldRight(List.empty[Boolean])((c, a) => adjacent.find(_._1 == c).isDefined :: a)

      NonEmptyVector
        .fromIterableOption(options)
        .getOrElse(sys.error("Impossible -> there has to be at least 1 element"))
    }
  }

  // Selections
  def randSelection[S, A](collection: NonEmptyVector[Entity[S, A]]): Step[(Entity[S, A], Position[A])] =
    Step.liftR(RVar.choose(collection).map(x => (x, x.pos)))

  def bestSelection[S, A](collection: NonEmptyVector[Entity[S, A]]): Step[(Entity[S, A], Position[A])] =
    collection.toChunk.tail
      .foldLeftM(collection.head) { case (acc, curr) =>
        Comparison.fittest(acc, curr)
      }
      .map(x => (x, x.pos))

  def randToBestSelection[S, A: Numeric: NumericTo](
    gamma: Double
  )(collection: NonEmptyVector[Entity[S, A]]): Step[(Entity[S, A], Position[A])] =
    for {
      best <- bestSelection(collection)
      rand <- randSelection(collection)
    } yield {
      val R           = NumericTo[A]
      val combination =
        (R.fromDouble(gamma) *: best._2) + (R.fromDouble(1.0 - gamma) *: rand._2)

      (rand._1, combination)
    }

  def currentToBestSelection[S, A: Numeric: NumericTo](
    p_m: Double
  )(collection: NonEmptyVector[Entity[S, A]]): Step[(Entity[S, A], Position[A])] =
    for {
      best <- bestSelection(collection)
      rand <- randSelection(collection)
    } yield {
      val x = rand._2 + NumericTo[A].fromDouble(p_m) *: (best._2 - rand._2)
      (rand._1, x)
    }

  def best_1_bin[S, A: Numeric: Equal: NumericTo](p_r: Double, p_m: Double) =
    best_bin(p_r, p_m, positiveInt(1))

  def rand_1_bin[S, A: Numeric: Equal: NumericTo](p_r: Double, p_m: Double) =
    rand_bin(p_r, p_m, positiveInt(1))

  def best_1_exp[S, A: Numeric: Equal: NumericTo](p_r: Double, p_m: Double) =
    best_exp(p_r, p_m, positiveInt(1))

  def best_bin[S, A: Numeric: Equal: NumericTo](p_r: Double, p_m: Double, y: Natural) =
    de(p_r, p_m, bestSelection[S, A], y, bin[Position, A])

  def rand_bin[S, A: Numeric: Equal: NumericTo](p_r: Double, p_m: Double, y: Natural) =
    de(p_r, p_m, randSelection[S, A], y, bin[Position, A])

  def best_exp[S, A: Numeric: Equal: NumericTo](p_r: Double, p_m: Double, y: Natural) =
    de(p_r, p_m, bestSelection[S, A], y, exp[Position, A])

  def rand_exp[S, A: Numeric: Equal: NumericTo](p_r: Double, p_m: Double, y: Natural) =
    de(p_r, p_m, randSelection[S, A], y, exp[Position, A])

  def randToBest[S, A: Numeric: Equal: NumericTo](
    p_r: Double,
    p_m: Double,
    gamma: Double,
    y: Natural,
    z: (Double, Position[A]) => RVar[NonEmptyVector[Boolean]]
  ) =
    de(p_r, p_m, randToBestSelection[S, A](gamma), y, z)

  def currentToBest[S, A: Numeric: Equal: NumericTo](
    p_r: Double,
    p_m: Double,
    y: Natural,
    z: (Double, Position[A]) => RVar[NonEmptyVector[Boolean]]
  ) =
    de(p_r, p_m, currentToBestSelection[S, A](p_m), y, z)

}
