package cilib
package de

import spire.math._
import spire.algebra._
import spire.implicits._

import scalaz._
import Scalaz._

object DE {

  def de[S,A:Numeric](
    p_r: Double,
    p_m: Double,
    targetSelection: NonEmptyList[Individual[S,A]] => RVar[Individual[S,A]]
    //y: N,
    //z: Int
  ): NonEmptyList[Individual[S,A]] => Individual[S,A] => Step[A,Individual[S,A]] =
    collection => x => for {
      evaluated <- Step.eval((a: Position[A]) => a)(x)
      trial <- Step.pointR(basicMutation(Numeric[A].fromDouble(p_m), targetSelection, collection, x))
      pivots <- Step.pointR(bin(p_r, evaluated.pos))
      offspring = crossover(x, trial, pivots)
      evaluatedOffspring <- Step.eval((a: Position[A]) => a)(offspring)
      fittest <- better(evaluated, evaluatedOffspring)
    } yield fittest


  // Duplicated from PSO.....
   def better[S,A](a: Individual[S,A], b: Individual[S,A]): Step[A,Individual[S,A]] =
    Step.withCompare(comp => Comparison.compare(a, b).apply(comp))


  def basicMutation[S,A:Rng](
    p_m: A,
    selection: NonEmptyList[Individual[S,A]] => RVar[Individual[S,A]],
    collection: NonEmptyList[Individual[S,A]],
    x: Individual[S,A]): RVar[Position[A]] = {
      def createPairs[Z](acc: List[(Z, Z)], xs: List[Z]): List[(Z, Z)] =
        xs match {
          case Nil => acc
          case a :: b :: xss => createPairs((a, b) :: acc, xss)
          case _ => sys.error("ugg")
        }

      val target: RVar[Individual[S,A]] = selection(collection)
      val filtered = target.map(t => collection.list.filterNot(a => List(t,x).contains(a)))
      val pairs: RVar[List[Position[A]]] =
        filtered.flatMap(_.toNel match {
          case Some(l) =>
            RVar.shuffle(l)
              .map(a => createPairs(List.empty[(Individual[S,A],Individual[S,A])], a.toList.take(2))
                .map(z => z._1.pos - z._2.pos))
          case None =>
            RVar.point(List.empty)
        })

      for {
        t <- target
        p <- pairs
      } yield p.foldLeft(t.pos)((a,c) => a + (p_m *: c))
    }


  def crossover[S,A](target: Individual[S,A], trial: Position[A], pivots: NonEmptyList[Boolean]) =
    target.copy(
      pos = {
        target.pos.zip(trial).zip(Position(pivots, trial.boundary)).map {
          case ((a,b), p) => // Position apply function :(
            if (p) b else a
        }
      })


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
}
