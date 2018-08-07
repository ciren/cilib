package cilib

import scalaz._
import Scalaz._

sealed abstract class Archive[A: Order, B] {

    def insertWithKey(k: A, v: B): NonEmpty[A, B] =
        this match {
            case Empty() => NonEmpty[A, B](==>>.singleton(k, v))
            case NonEmpty(m) => NonEmpty[A, B](m.insert(k, v))
        }

    def getAllValues: Option[NonEmptyList[B]] =
        this match {
            case Empty() => None
            case NonEmpty(m) => m.values.toNel
        }

    def toList: List[B] =
        this match {
            case Empty() => List[B]()
            case NonEmpty(m) => m.values
        }

    def getLastKey: Option[A] =
        this match {
            case Empty() => None
            case NonEmpty(m) => Some(m.keys.last)
        }

    def delete(k: A): Archive[A, B] =
        this match {
            case Empty() => Empty[A, B]()
            case NonEmpty(m) => {
                val alteredM = m.delete(k) 

                if (alteredM.isEmpty)
                    Empty[A, B]()
                else
                    NonEmpty(alteredM)
            }
        }

    def deleteValue(v: B): Archive[A, B] =
        this match {
            case Empty() => Empty[A, B]()
            case NonEmpty(m) => {
                var removed = false
                val alteredM = m.filter(x => {
                        if (x == v && !removed){
                            removed = true
                            false
                        } else {
                            true
                        }
                    })

                if (alteredM.isEmpty)
                    Empty[A, B]()
                else
                    NonEmpty(alteredM)
            }
        }

    def member(k: A): Boolean =
        this match {
            case Empty() => false
            case NonEmpty(m) => m.member(k)
        }

    def size(): Int =
        this match {
            case Empty() => 0
            case NonEmpty(m) => m.size
        }

    def elemAt(i: Int): Option[(A, B)] =
        this match {
            case Empty() => None
            case NonEmpty(m) => m.elemAt(i)
        }

    def lookup(k: A): Option[B] =
        this match {
            case Empty() => None
            case NonEmpty(m) => m.lookup(k)
        }

    def isParetoOptimal(f: (B, B) => Boolean)(x: B): Boolean =
        this match {
            case Empty() => true
            case NonEmpty(m) => m.values.map(y => if (!f(y, x)) 0 else 1).sum == 0 
        }

    def deleteAllWithCondition(f: (B, B) => Boolean): Archive[A, B] =
        this match {
            case Empty() => Empty()
            case NonEmpty(m) => {
                val l = m.values
                val d = l.tail.fold(l.head)((x, y) => if (!f(y, x)) x else y) 
                val alteredM = m.filter(x => {
                    if(d == x) true
                    else !f(d, x) 
                })
                if (alteredM.isEmpty)
                    Empty[A, B]()
                else
                    NonEmpty[A, B](alteredM)
            }
        }

    def insertWithCondition(f: (B, B) => Boolean)(k: A, v: B): Archive[A, B] =
        this match {
            case Empty() => NonEmpty[A, B](==>>.singleton(k, v))
            case NonEmpty(m) => {
                if (m.values.map(y => if (f(v, y)) 0 else 1).sum == 0)
                    NonEmpty[A, B](m.insert(k, v))
                else
                    NonEmpty[A, B](m)
            }
        }

    def isEmpty: Boolean =
        this match {
            case Empty() => true
            case NonEmpty(m) => m.isEmpty
        }
}

final case class Empty[A: Order, B]() extends Archive[A, B]

final case class NonEmpty[A: Order, B](m: ==>>[A, B]) extends Archive[A, B]
