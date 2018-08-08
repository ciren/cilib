package cilib

import scalaz._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive

sealed trait Bound
final case class Closed(limit: Int Refined Positive) extends Bound
final case class Open() extends Bound

sealed abstract class Archive[A: Order, B] {

    def keys: Option[List[A]] =
        this match {
            case Empty(_) => None
            case NonEmpty(m, _) => Some(m.keys)
        }

    def values: Option[List[B]] =
        this match {
            case Empty(_) => None
            case NonEmpty(m, _) => Some(m.values)
        }

    def bound: Bound =
        this match {
            case Empty(b) => b
            case NonEmpty(_, b) => b
        }

    def map[C](f: B => C): Archive[A, C] =
        this match {
            case Empty(b) => Empty(b)
            case NonEmpty(m, b) => NonEmpty(m.map(f), b)
        }

    def fold[C](z: C)(f: (A, B, C) => C): Option[C] =
        this match {
            case Empty(_) => None
            case NonEmpty(m, _) => Some(m.fold(z)(f))
        }

    def foldlWithKey(z: B)(f: (B, A, B) => B): Option[B] =
        this match {
            case Empty(_) => None
            case NonEmpty(m, _) => Some(m.foldlWithKey(z)(f))
        }

    def foldrWithKey(z: B)(f: (A, B, B) => B): Option[B] =
        this match {
            case Empty(_) => None
            case NonEmpty(m, _) => Some(m.foldrWithKey(z)(f))
        }

    def insert(k: A, v: B): NonEmpty[A, B] =
        this match {
            case Empty(b) => NonEmpty[A, B](==>>.singleton(k, v), b)
            case NonEmpty(m, b) =>
                b match {
                    case Closed(limit) =>
                        if (limit.value <= m.size) NonEmpty[A, B](m.insert(k, v), b)
                        else NonEmpty[A, B](m, b)
                    case Open() => NonEmpty[A, B](m, b)
                }
        }

    def insertWithCondition(f: (B, B) => Boolean)(k: A, v: B): Archive[A, B] =
        this match {
            case Empty(b) => NonEmpty[A, B](==>>.singleton(k, v), b)
            case NonEmpty(m, b) =>
                if (m.values.forall(x => f(v, x))) NonEmpty[A, B](m.insert(k, v), b)
                else NonEmpty[A, B](m, b)
        }

    def deleteByKey(k: A): Archive[A, B] =
        this match {
            case Empty(b) => Empty[A, B](b)
            case NonEmpty(m, b) =>
                val newMap = m.delete(k)
                if (newMap.isEmpty) Empty[A, B](b)
                else NonEmpty(newMap, b)
        }

    def deleteByValue(v: B): Archive[A, B] =
        this match {
            case Empty(b) => Empty[A, B](b)
            case NonEmpty(m, b) =>
                val newMap = m.filter(x => x != v)
                if (newMap.isEmpty) Empty[A, B](b)
                else NonEmpty(newMap, b)
        }

    def deleteWithCondition(f: B => Boolean): Archive[A, B] =
        this match {
            case Empty(b) => Empty(b)
            case NonEmpty(m, b) =>
                val newMap = m.filter(x => f(x))
                if (newMap.isEmpty) Empty[A, B](b)
                else NonEmpty(newMap, b)
        }

    def adjust(k: A, f: B => B): Archive[A, B] =
        this match {
            case Empty(b) => Empty(b)
            case NonEmpty(m, b) => NonEmpty(m.adjust(k, f), b)
        }

    def adjustWithKey(k: A, f: (A, B) => B): Archive[A, B] =
        this match {
            case Empty(b) => Empty(b)
            case NonEmpty(m, b) => NonEmpty(m.adjustWithKey(k, f), b)
        }

    def dominates(f: (B, B) => Boolean)(v: B): Boolean =
        this match {
            case Empty(_) => true
            case NonEmpty(m, _) => m.values.forall(x => f(v, x))
        }

    def elemAt(i: Int): Option[(A, B)] =
        this match {
            case Empty(_) => None
            case NonEmpty(m, _) => m.elemAt(i)
        }

    def empty: Empty[A, B] =
        this match {
            case Empty(b) => Empty(b)
            case NonEmpty(_, b) => Empty(b)
        }

    def isEmpty: Boolean =
        this match {
            case Empty(_) => true
            case NonEmpty(m, _) => m.isEmpty
        }

    def lastKey: Option[A] =
        this match {
            case Empty(_) => None
            case NonEmpty(m, _) => m.keys.lastOption
        }

    def lookup(k: A): Option[B] =
        this match {
            case Empty(_) => None
            case NonEmpty(m, _) => m.lookup(k)
        }

    def member(k: A): Boolean =
        this match {
            case Empty(_) => false
            case NonEmpty(m, _) => m.member(k)
        }

    def size: Int =
        this match {
            case Empty(_) => 0
            case NonEmpty(m, _) => m.size
        }
}

final case class Empty[A: Order, B] private[cilib] (b: Bound) extends Archive[A, B]
final case class NonEmpty[A: Order, B] private[cilib] (m: ==>>[A, B], b: Bound) extends Archive[A, B]
