package cilib
package de

import spire.math._
import spire.algebra._
import spire.implicits.{eu => _, _}

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._

import scalaz._
import Scalaz._

object DE {

  def de[S, A: Numeric: Equal](
      p_r: Double,
      p_m: Double,
      targetSelection: NonEmptyList[Individual[S, A]] => Step[A, (Individual[S, A], Position[A])],
      y: Int Refined Positive,
      z: (Double, Position[A]) => RVar[NonEmptyList[Boolean]] // Double check this shape
  ): NonEmptyList[Individual[S, A]] => Individual[S, A] => Step[A, Individual[S, A]] =
    collection =>
      x =>
        for {
          evaluated <- Step.eval((a: Position[A]) => a)(x)
          trial <- basicMutation(Numeric[A].fromDouble(p_m), targetSelection, y, collection, x)
          pivots <- Step.liftR(z(p_r, evaluated.pos))
          offspring = crossover(x, trial, pivots)
          evaluatedOffspring <- Step.eval((a: Position[A]) => a)(offspring)
          fittest <- Comparison.fittest(evaluated, evaluatedOffspring)
        } yield fittest

  def basicMutation[S, A: Rng: Equal](
      p_m: A,
      selection: NonEmptyList[Individual[S, A]] => Step[A, (Individual[S, A], Position[A])],
      y: Int Refined Positive,
      collection: NonEmptyList[Individual[S, A]],
      x: Individual[S, A]): Step[A, Position[A]] = {

    def createPairs[Z](acc: List[(Z, Z)], xs: List[Z]): List[(Z, Z)] =
      xs match {
        case Nil           => acc
        case a :: b :: xss => createPairs((a, b) :: acc, xss)
        case _             => sys.error("ugg")
      }

    val target: Step[A, (Individual[S, A], Position[A])] = selection(collection)
    val filtered = target.map(t => collection.list.filterNot(a => a === t._1 || a === x))
    val pairs: Step[A, List[Position[A]]] =
      filtered.flatMap(x =>
        x.toNel match {
          case Some(l) =>
            Step.liftR(
              RVar
                .shuffle(l)
                .map(
                  a =>
                    createPairs(List.empty[(Individual[S, A], Individual[S, A])],
                                a.toList.take(2 * y))
                      .map(z => z._1.pos - z._2.pos))
            )
          case None =>
            Step.pure(List.empty)
      })

    for {
      t <- target
      p <- pairs
    } yield p.foldLeft(t._2)((a, c) => a + (p_m *: c))
  }

  def crossover[S, A](target: Individual[S, A], trial: Position[A], pivots: NonEmptyList[Boolean]) =
    target.copy(pos = {
      target.pos.zip(trial).zip(Position(pivots, trial.boundary)).map {
        case ((a, b), p) => // Position apply function :(
          if (p) b else a
      }
    })

  def bin[F[_]: Foldable1, A](
      p_r: Double,
      parent: F[A]
  ): RVar[NonEmptyList[Boolean]] =
    Dist
      .uniformInt(spire.math.Interval(0, parent.length - 1))
      .flatMap(j =>
        parent.toNel.zipWithIndex.traverse {
          case (_, i) =>
            if (i == j) RVar.pure(true)
            else Dist.stdUniform.map(_ < p_r)
      })

  def exp[F[_]: Foldable1, A](
      p_r: Double,
      parent: F[A]
  ): RVar[NonEmptyList[Boolean]] = {
    val length = parent.length
    for {
      start <- Dist.uniformInt(spire.math.Interval(0, length - 1))
      circular = Stream.continually((0 to length).toStream).flatten
      randoms <- Dist.stdUniform.replicateM(length)
    } yield {
      val paired = circular.drop(start).take(length).toList.zip(randoms)
      val adjacent = paired.head :: paired.tail.takeWhile(t => t._2 < p_r)

      (0 to length)
        .foldRight(List.empty[Boolean])((c, a) =>
          adjacent.find(_._1 == c) match {
            case None    => false :: a
            case Some(_) => true :: a
        })
        .toNel
        .getOrElse(sys.error("Impossible -> there has to be at least 1 element"))
    }
  }

  // Selections
  def randSelection[S, A](
      collection: NonEmptyList[Entity[S, A]]): Step[A, (Entity[S, A], Position[A])] =
    Step.liftR(RVar.choose(collection).map(x => (x, x.pos)))

  def bestSelection[S, A](
      collection: NonEmptyList[Entity[S, A]]): Step[A, (Entity[S, A], Position[A])] =
    collection.tail
      .foldLeftM(collection.head) {
        case (acc, curr) =>
          Comparison.fittest(acc, curr)
      }
      .map(x => (x, x.pos))

  def randToBestSelection[S, A: Numeric](gamma: Double)(
      collection: NonEmptyList[Entity[S, A]]): Step[A, (Entity[S, A], Position[A])] =
    for {
      best <- bestSelection(collection)
      rand <- randSelection(collection)
    } yield {
      val R = implicitly[Numeric[A]]
      val combination =
        (R.fromDouble(gamma) *: best._2) + (R.fromDouble(1.0 - gamma) *: rand._2)

      (rand._1, combination)
    }

  def currentToBestSelection[S, A: Numeric](p_m: Double)(
      collection: NonEmptyList[Entity[S, A]]): Step[A, (Entity[S, A], Position[A])] =
    for {
      best <- bestSelection(collection)
      rand <- randSelection(collection)
    } yield {
      val x = rand._2 + Numeric[A].fromDouble(p_m) *: (best._2 - rand._2)
      (rand._1, x)
    }

  def best_1_bin[S, A: Numeric: Equal](p_r: Double, p_m: Double) =
    best_bin(p_r, p_m, 1)

  def rand_1_bin[S, A: Numeric: Equal](p_r: Double, p_m: Double) =
    rand_bin(p_r, p_m, 1)

  def best_1_exp[S, A: Numeric: Equal](p_r: Double, p_m: Double) =
    best_exp(p_r, p_m, 1)

  def best_bin[S, A: Numeric: Equal](p_r: Double, p_m: Double, y: Int Refined Positive) =
    de(p_r, p_m, bestSelection[S, A], y, bin[Position, A])

  def rand_bin[S, A: Numeric: Equal](p_r: Double, p_m: Double, y: Int Refined Positive) =
    de(p_r, p_m, randSelection[S, A], y, bin[Position, A])

  def best_exp[S, A: Numeric: Equal](p_r: Double, p_m: Double, y: Int Refined Positive) =
    de(p_r, p_m, bestSelection[S, A], y, exp[Position, A])

  def rand_exp[S, A: Numeric: Equal](p_r: Double, p_m: Double, y: Int Refined Positive) =
    de(p_r, p_m, randSelection[S, A], y, exp[Position, A])

  def randToBest[S, A: Numeric: Equal](p_r: Double,
                                       p_m: Double,
                                       gamma: Double,
                                       y: Int Refined Positive,
                                       z: (Double, Position[A]) => RVar[NonEmptyList[Boolean]]) =
    de(p_r, p_m, randToBestSelection[S, A](gamma), y, z)

  def currentToBest[S, A: Numeric: Equal](p_r: Double,
                                          p_m: Double,
                                          y: Int Refined Positive,
                                          z: (Double, Position[A]) => RVar[NonEmptyList[Boolean]]) =
    de(p_r, p_m, currentToBestSelection[S, A](p_m), y, z)

}
