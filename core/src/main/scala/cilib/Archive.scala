package cilib

import scalaz._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive

sealed trait ArchiveBound
final case class Bounded(limit: Int Refined Positive) extends ArchiveBound
final case class Unbounded() extends ArchiveBound

sealed abstract class Archive[A, B] {

  def keys: Option[List[A]] =
    this match {
      case Empty(_)       => None
      case NonEmpty(m, _) => Some(m.keys)
    }

  def values: Option[List[B]] =
    this match {
      case Empty(_)       => None
      case NonEmpty(m, _) => Some(m.values)
    }

  def bound: ArchiveBound =
    this match {
      case Empty(b)       => b
      case NonEmpty(_, b) => b
    }

  def map[C](f: B => C): Archive[A, C] =
    this match {
      case Empty(b)       => Empty(b)
      case NonEmpty(m, b) => NonEmpty(m.map(f), b)
    }

  def fold[C](z: C)(f: (A, B, C) => C): Option[C] =
    this match {
      case Empty(_)       => None
      case NonEmpty(m, _) => Some(m.fold(z)(f))
    }

  def insert(k: A, v: B)(implicit A: Order[A]): NonEmpty[A, B] =
    this match {
      case Empty(b) => NonEmpty[A, B](==>>.singleton(k, v), b)
      case NonEmpty(m, b) =>
        b match {
          case Bounded(limit) =>
            if (limit.value <= m.size) NonEmpty[A, B](m.insert(k, v), b)
            else NonEmpty[A, B](m, b)
          case Unbounded() => NonEmpty[A, B](m.insert(k, v), b)
        }
    }

  def insertWithCondition(f: (B, B) => Boolean)(k: A, v: B)(implicit A: Order[A]): Archive[A, B] =
    this match {
      case Empty(b) => NonEmpty[A, B](==>>.singleton(k, v), b)
      case NonEmpty(m, b) =>
        b match {
          case Bounded(limit) =>
            if (limit.value <= m.size && m.values.forall(x => f(v, x)))
              NonEmpty[A, B](m.insert(k, v), b)
            else NonEmpty[A, B](m, b)
          case Unbounded() => NonEmpty[A, B](m.insert(k, v), b)
        }

    }

  def deleteByKey(k: A)(implicit A: Order[A]): Archive[A, B] =
    this match {
      case Empty(b) => Empty[A, B](b)
      case NonEmpty(m, b) =>
        val newMap = m.delete(k)
        if (newMap.isEmpty) Empty[A, B](b)
        else NonEmpty(newMap, b)
    }

  def deleteWithCondition(f: B => Boolean)(implicit A: Order[A]): Archive[A, B] =
    this match {
      case Empty(b) => Empty(b)
      case NonEmpty(m, b) =>
        val newMap = m.filter(x => f(x))
        if (newMap.isEmpty) Empty[A, B](b)
        else NonEmpty(newMap, b)
    }

  def deleteMax: Archive[A, B] =
    this match {
      case Empty(b)       => Empty[A, B](b)
      case NonEmpty(m, b) => NonEmpty(m.deleteMax, b)
    }

  def deleteMin: Archive[A, B] =
    this match {
      case Empty(b)       => Empty[A, B](b)
      case NonEmpty(m, b) => NonEmpty(m.deleteMin, b)
    }

  def findMax: Option[(A, B)] =
    this match {
      case Empty(_)       => None
      case NonEmpty(m, _) => m.findMax
    }

  def findMin: Option[(A, B)] =
    this match {
      case Empty(_)       => None
      case NonEmpty(m, _) => m.findMin
    }

  def adjust(k: A, f: B => B)(implicit A: Order[A]): Archive[A, B] =
    this match {
      case Empty(b)       => Empty(b)
      case NonEmpty(m, b) => NonEmpty(m.adjust(k, f), b)
    }

  def adjustWithKey(k: A, f: (A, B) => B)(implicit A: Order[A]): Archive[A, B] =
    this match {
      case Empty(b)       => Empty(b)
      case NonEmpty(m, b) => NonEmpty(m.adjustWithKey(k, f), b)
    }

  def dominates(f: (B, B) => Boolean)(v: B): Boolean =
    this match {
      case Empty(_)       => true
      case NonEmpty(m, _) => m.values.forall(x => f(v, x))
    }

  def empty: Empty[A, B] =
    Empty(bound)

  def isEmpty: Boolean =
    this match {
      case Empty(_)       => true
      case NonEmpty(_, _) => false
    }

  def lastKey: Option[A] =
    this match {
      case Empty(_)       => None
      case NonEmpty(m, _) => m.keys.lastOption
    }

  def lookup(k: A)(implicit A: Order[A]): Option[B] =
    this match {
      case Empty(_)       => None
      case NonEmpty(m, _) => m.lookup(k)
    }

  def member(k: A)(implicit A: Order[A]): Boolean =
    this match {
      case Empty(_)       => false
      case NonEmpty(m, _) => m.member(k)
    }

  def size: Int =
    this match {
      case Empty(_)       => 0
      case NonEmpty(m, _) => m.size
    }
}

final case class Empty[A, B] private (b: ArchiveBound) extends Archive[A, B]
final case class NonEmpty[A, B] private (m: ==>>[A, B], b: ArchiveBound) extends Archive[A, B]

object Archive {

  def bounded[A, B](limit: Int Refined Positive): Empty[A, B] = Empty[A, B](Bounded(limit))
  def unbounded[A, B]: Empty[A, B] = Empty[A, B](Unbounded())

}
