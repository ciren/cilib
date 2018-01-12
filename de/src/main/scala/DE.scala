package cilib
package de

import spire.math._
import spire.algebra._
import spire.implicits.{eu => _, _}

import scalaz._
import Scalaz._
import cilib.Position.PositionVectorOps
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._

object DE {
  def de[S, A: Numeric : Rng](
                                 p_r: Double,
                                 p_m: Double,
                                 targetSelection: NonEmptyList[Individual[S, A]] => RVar[Individual[S, A]], // x
                                 numberOfDifferenceVectors: Int Refined Positive, // y
                                //pivotMethod: (Double, F[A]) => RVar[NonEmptyList[Boolean]] // z
                             ): NonEmptyList[Individual[S, A]] => Individual[S, A] => Step[A, Individual[S, A]] =
    collection => x => for {
      evaluated <- Step.eval((a: Position[A]) => a)(x)
      trial <- Step.pointR(basicMutation(Numeric[A].fromDouble(p_m), targetSelection, collection, x, numberOfDifferenceVectors))
      pivots <- Step.pointR(bin(p_r, evaluated.pos))
      offspring = crossover(x, trial, pivots)
      evaluatedOffspring <- Step.eval((a: Position[A]) => a)(offspring)
      fittest <- better(evaluated, evaluatedOffspring)
    } yield fittest

  // DEs
  def best_1_bin[S, A: Numeric : Rng](p_r: Double, p_m: Double) =
    de(p_r, p_m, selectBestTarget[S, A], 1)

  def best_y_bin[S, A: Numeric : Rng](p_r: Double, p_m: Double, y: Int Refined Positive) =
    de(p_r, p_m, selectBestTarget[S, A], y)

  def rand_1_bin[S, A: Numeric : Rng](p_r: Double, p_m: Double) =
    de(p_r, p_m, (xs: NonEmptyList[Individual[S, A]]) => RVar.shuffle(xs).map(_.head), 1)

  def rand_y_bin[S, A: Numeric : Rng](p_r: Double, p_m: Double, y: Int Refined Positive) =
    de(p_r, p_m, (xs: NonEmptyList[Individual[S, A]]) => RVar.shuffle(xs).map(_.head), y)

  // Selection Methods
  def selectBestTarget[S, A](individuals: NonEmptyList[Individual[S, A]]): RVar[Individual[S, A]] = {
    val maxComparison = Comparison.dominance(Max)
    RVar.point(individuals.foldLeft1((a, b) => maxComparison.apply(a, b)(Entity.entityFitness)))
  }

  // Mutation Methods
  def basicMutation[S, A: Rng](
                                  p_m: A,
                                  selection: NonEmptyList[Individual[S, A]] => RVar[Individual[S, A]],
                                  collection: NonEmptyList[Individual[S, A]],
                                  x: Individual[S, A],
                                  numberOfDifferenceVectors: Int Refined Positive
                              ): RVar[Position[A]] = {
    val target = selection(collection)
    val filtered = filter(target, collection, x)
    val differenceVector = sumDifferenceVector(getDifferenceVector(filtered, numberOfDifferenceVectors))
    for {
      t <- target
      p <- differenceVector
    } yield p.foldLeft(t.pos)((a, c) => a + (p_m *: c))
  }

  def randToBestMutation[S, A: Rng](
                                       p_m: A,
                                       selection: NonEmptyList[Individual[S, A]] => RVar[Individual[S, A]],
                                       collection: NonEmptyList[Individual[S, A]],
                                       x: Individual[S, A],
                                       numberOfDifferenceVectors: Int Refined Positive,
                                       greediness: Double
                                   ): RVar[Position[A]] = {
    val target = selection(collection)
    val filtered = filter(target, collection, x)
    val differenceVector = sumDifferenceVector(getDifferenceVector(filtered, numberOfDifferenceVectors))
    for {
      t <- target
      p <- differenceVector
    } yield p.foldLeft((1 - greediness) *: t.pos)((a, c) => a + (p_m *: c))
  }

  def currentToBestMutation[S, A: Rng](
                                          p_m: A,
                                          selection: NonEmptyList[Individual[S, A]] => RVar[Individual[S, A]],
                                          collection: NonEmptyList[Individual[S, A]],
                                          x: Individual[S, A],
                                          numberOfDifferenceVectors: Int Refined Positive
                                      ): RVar[Position[A]] = {
    val target = selection(collection)
    val filtered = filter(target, collection, x)
    val differenceVector = sumDifferenceVector(getDifferenceVector(filtered, numberOfDifferenceVectors))

    for {
      t <- target
      p <- differenceVector
    } yield p.foldLeft(t.pos + (p_m *: (x.pos - t.pos)))((a, c) => a + (p_m *: c))
  }

  // Crossover Methods
  def crossover[S, A](target: Individual[S, A], trial: Position[A], pivots: NonEmptyList[Boolean]) =
    target.copy(
      pos = {
        target.pos.zip(trial).zip(Position(pivots, trial.boundary)).map { case ((a, b), p) => // Position apply function :(
          if (p) b else a
        }
      })

  // Pivot Calculation Methods
  def bin[F[_]:Foldable1,A](
                               p_r: Double,
                               parent: F[A]
                           ): RVar[NonEmptyList[Boolean]] =
    Dist.uniformInt(spire.math.Interval(0, parent.length - 1))
        .flatMap(j => parent.toNel.zipWithIndex.traverse {
          case (_, i) =>
            if (i == j) RVar.point(true)
            else Dist.stdUniform.map(_ < p_r)
        })


  def exp[F[_]:Foldable1,A](
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

      (0 to length).foldRight(List.empty[Boolean])(
        (c, a) => adjacent.find(_._1 == c) match {
          case None => false :: a
          case Some(_) => true :: a
        }).toNel.getOrElse(sys.error("Impossible -> there has to be at least 1 element"))
    }
  }


  // Helper Methods .... Private?
  // Duplicated from PSO.....
  def better[S, A](a: Individual[S, A], b: Individual[S, A]): Step[A, Individual[S, A]] =
    Step.withCompare(comp => Comparison.compare(a, b).apply(comp))

  def filter[S, A](target: RVar[Individual[S, A]], collection: NonEmptyList[Individual[S, A]], x: Individual[S, A]): RVar[IList[Individual[S, A]]] =
    target.map(t => collection.list.filterNot(a => List(t, x).contains(a)))

  def createPairs[Z](acc: List[(Z, Z)], xs: List[Z]): List[(Z, Z)] =
    xs match {
      case Nil => acc
      case a :: b :: xss => createPairs((a, b) :: acc, xss)
      case _ => sys.error("ugg")
    }

  def getDifferenceVector[S, A](filteredCollection: RVar[IList[Individual[S, A]]], numberOfDifferenceVectors: Int Refined Positive)(implicit M: Module[Position[A],A]): RVar[List[Position[A]]] = {
    filteredCollection.flatMap(_.toNel match {
      case Some(l) =>
        RVar.shuffle(l)
            .map(a => createPairs(List.empty[(Individual[S, A], Individual[S, A])], a.toList.take(numberOfDifferenceVectors * 2))
                .map(z => z._1.pos - z._2.pos))
      case None =>
        RVar.point(List.empty)
    })
  }

  // Used Double and not A. Gave error "could not find implicit value for parameter A: spire.algebra.Rng[A]"
  def sumDifferenceVector[A](xs: RVar[List[Position[A]]])(implicit A: Rng[A]): RVar[List[Position[A]]] = {
    xs.map(l => List(l.foldRight(l.head.zeroed)(_ + _)))
  }
}
