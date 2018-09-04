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

  // Needs work
  def insertWith(f: (A, A) => Boolean)(v: A): Archive[A] =
    this match {
      case Empty(b) => NonEmpty[A](List(v), b)
      case NonEmpty(l, b) =>
        b match {
          case Bounded(limit) =>
            if (l.size == 0)
              NonEmpty[A](v :: l, b)
            else if (l.size < limit.value && l.forall(x => f(v, x)))
              NonEmpty[A](NonEmpty[A](v :: l, b).toList.filterNot(x => this.management.strategy(x)), b) // Cleanup after insert: Idea is to call/use a user specified archive management function that was specified at archive creation passed to constructor?
            else if(l.size == limit.value && l.forall(x => f(v, x))) // Still to add removal of mostCrowded when archive is full
              ???
            else
              NonEmpty[A](l, b)

          case Unbounded() =>
            if (l.size == 0)
              NonEmpty[A](v :: l, b)
            else if(l.forall(x => f(v,x)))
              NonEmpty[A](NonEmpty[A](v :: l, b).toList.filterNot(x => this.management.strategy(x)), b)
            else
              NonEmpty[A](l, b)
        }
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
    seeds.foldLeft(emptyArchive)((archive, seed) => archive.insert(seed))
  }

  def unboundedNonEmpty[A](seeds: NonEmptyList[A]): Archive[A] = {
    val emptyArchive: Archive[A] = unbounded
    seeds.foldLeft(emptyArchive)((archive, seed) => archive.insert(seed))
  }
}
