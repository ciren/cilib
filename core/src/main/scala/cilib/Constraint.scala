package cilib

import spire.algebra.Eq
import spire.math._
import spire.implicits._

case class ConstraintFunction[A,B](f: List[A] => B) {
  def apply(a: List[A]): B =
    f(a)
}

sealed trait Constraint[A,B]
case class LessThan[A,B](f: ConstraintFunction[A,B], v: B) extends Constraint[A,B]
case class LessThanEqual[A,B](f: ConstraintFunction[A,B], v: B) extends Constraint[A,B]
case class Equal[A,B](f: ConstraintFunction[A,B], v: B) extends Constraint[A,B]
case class InInterval[A,B](f: ConstraintFunction[A,B], interval: Interval[B]) extends Constraint[A,B]

object Constraint {

  import scalaz.{Foldable, Functor}
  def constrain[M[_],F[_]:Foldable](ma: M[Eval[F,Double]], cs: List[Constraint[Double,Double]])(implicit M: Functor[M]) =
    M.map(ma)(_.constrainBy(cs))

  def violationMagnitude[A,B:Fractional](beta: Double, eta: Double, constraints: List[Constraint[A,B]], cs: List[A])(implicit e: Eq[B]): Double = {
    constraints.map(_ match {
      case LessThan(f, v) =>
        val v2 = f(cs)
        if (v2 < v) 0.0
        else math.pow(math.abs(v2.toDouble - v.toDouble), beta) + eta
      case LessThanEqual(f, v) =>
        val v2 = f(cs)
        if (v2 <= v) 0.0
        else math.pow(math.abs(v2.toDouble - v.toDouble), beta) + eta
      case Equal(f, v) =>
        val v2 = f(cs)
        if (e.eqv(v2, v)) 0.0 // Doubles are "equal" if they are equivalent using IEEE floats.
        else math.pow(math.abs(v2.toDouble - v.toDouble), beta) + eta
      case InInterval(f, i) =>
        val v2 = f(cs)
        val left = i.lower match {
          case Closed(value) => value <= v2
          case Open(value) => value < v2
        }
        val right = i.upper match {
          case Closed(value) => v2 <= value
          case Open(value) => v2 < value
        }

        (left, right) match {
          case (true, true) => 0.0
          case (false, _) => math.pow(math.abs(i.lower.value.toDouble - v2.toDouble), beta) + (i.lower match { case Closed(_) => 0; case Open(_) => 1})*eta
          case (_, false) => math.pow(math.abs(v2.toDouble - i.upper.value.toDouble), beta) + (i.upper match { case Closed(_) => 0; case Open(_) => 1})*eta
        }
    }).sum
  }

  def violationCount[A,B:Fractional](constraints: List[Constraint[A,B]], cs: List[A]) =
    constraints.map(satisfies(_, cs)).filterNot(x => x).length

  def satisfies[A,B:Fractional](constraint: Constraint[A,B], cs: List[A])(implicit ev: Eq[B]) =
    constraint match {
      case LessThan(f, v) => f(cs) < v
      case LessThanEqual(f, v) => f(cs) <= v
      case Equal(f, v) => ev.eqv(f(cs), v)
      case InInterval(f, i) =>
        val v2 = f(cs)
        val c1 = i.lower match {
          case Open(value) => value < v2
          case Closed(value) => value <= v2
        }
        val c2 = i.upper match {
          case Open(value) => v2 < value
          case Closed(value) => v2 <= value
        }
        c1 && c2
    }
}

