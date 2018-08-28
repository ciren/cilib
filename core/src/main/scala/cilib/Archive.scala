package cilib

import scalaz._
import Scalaz._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive

sealed trait ArchiveBound
final case class Bounded(limit: Int Refined Positive) extends ArchiveBound
final case class Unbounded() extends ArchiveBound

sealed abstract class Archive[A] {
  import Archive._

  def values: Option[List[A]] =
    this match {
      case Empty(_)       => None
      case NonEmpty(l, _) => Some(l)
    }

  def bound: ArchiveBound =
    this match {
      case Empty(b)       => b
      case NonEmpty(_, b) => b
    }

  def map[C](f: A => C): Archive[C] =
    this match {
      case Empty(b)       => Empty(b)
      case NonEmpty(l, b) => NonEmpty(l.map(f), b)
    }

  def fold(z: A)(f: (A, A) => A): A =
    this match {
      case Empty(_)       => z
      case NonEmpty(l, _) => l.fold(z)(f)
    }

  def insert(v: A): Archive[A] =
    this match {
      case Empty(b) => NonEmpty[A](List(v), b)
      case NonEmpty(l, b) =>
        b match {
          case Bounded(limit) =>
            if (l.size < limit.value)
              NonEmpty[A](v :: l, b)
            else NonEmpty[A](l, b)
          case Unbounded() => NonEmpty[A](v :: l, b)
        }
    }

  def insertWithCondition(f: (A, A) => Boolean)(v: A): Archive[A] =
    this match {
      case Empty(b) => NonEmpty[A](List(v), b)
      case NonEmpty(l, b) =>
        b match {
          case Bounded(limit) =>
            if (limit.value <= l.size && l.forall(x => f(v, x)))
              NonEmpty[A](v :: l, b)
            else
              NonEmpty[A](l, b)

          case Unbounded() => NonEmpty[A](v :: l, b)
        }
    }

  def delete(v: A): Archive[A] =
    deleteWithCondition(x => x.equals(v))

  def deleteWithCondition(f: A => Boolean): Archive[A] =
    this match {
      case Empty(b) => Empty(b)
      case NonEmpty(l, b) =>
        val newList = l.filterNot(x => f(x))
        if (newList.isEmpty) Empty[A](b)
        else NonEmpty(newList, b)
    }

  def max(implicit ord: scalaz.Order[A]): Option[A] =
    this match {
      case Empty(_)       => None
      case NonEmpty(l, _) => l.maximum
    }

  def min(implicit ord: scalaz.Order[A]): Option[A] =
    this match {
      case Empty(_)       => None
      case NonEmpty(l, _) => l.minimum
    }

  def replace(oldV: A, newV: A)(implicit E: scalaz.Equal[A]): Archive[A] =
    this match {
      case Empty(b)       => Empty(b)
      case NonEmpty(l, b) => NonEmpty(l.map(x => if (x === oldV) newV else x), b)
    }

  def dominates(f: (A, A) => Boolean)(v: A): Boolean =
    this match {
      case Empty(_)       => true
      case NonEmpty(l, _) => l.forall(x => f(v, x))
    }

  def empty: Archive[A] = Empty(bound)

  def isEmpty: Boolean =
    this match {
      case Empty(_)       => true
      case NonEmpty(_, _) => false
    }

  def head: Option[A] =
    this match {
      case Empty(_)       => None
      case NonEmpty(l, _) => l.headOption
    }

  def contains(v: A): Boolean =
    this match {
      case Empty(_)       => false
      case NonEmpty(l, _) => l.contains(v)
    }

  def size: Int =
    this match {
      case Empty(_)       => 0
      case NonEmpty(l, _) => l.size
    }

  def toList: List[A] =
    this match {
      case Empty(_)       => List.empty[A]
      case NonEmpty(l, _) => l
    }
}

object Archive {

  private final case class Empty[A](b: ArchiveBound) extends Archive[A]
  private final case class NonEmpty[A](l: List[A], b: ArchiveBound) extends Archive[A]

  def bounded[A](limit: Int Refined Positive): Archive[A] = Empty[A](Bounded(limit))
  def unbounded[A]: Archive[A] = Empty[A](Unbounded())

  def boundedNonEmpty[A](seeds: NonEmptyList[A], limit: Int Refined Positive): Archive[A] = {
    val emptyArchive: Archive[A] = bounded(limit)
    seeds.toList.foldLeft(emptyArchive)((archive, seed) => archive.insert(seed))
  }

  def unboundedNonEmpty[A](seeds: NonEmptyList[A]): Archive[A] = {
    val emptyArchive: Archive[A] = unbounded
    seeds.toList.foldLeft(emptyArchive)((archive, seed) => archive.insert(seed))
  }
}
