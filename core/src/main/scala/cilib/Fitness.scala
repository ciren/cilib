package cilib

import scalaz._
import Ordering._

import scalaz.std.anyVal._
import scalaz.syntax.equal._

sealed trait Fit {
  def fold[Z](penalty: Penalty => Z, valid: Valid => Z): Z =
    this match {
      case p @ Penalty(_,_) => penalty(p)
      case v @ Valid(_)     => valid(v)
    }
}
final case class Penalty(v: Double, p: Double) extends Fit
final case class Valid(v: Double) extends Fit

@annotation.implicitNotFound("No instance of Quality[${A}] is available in current scope.")
trait Quality[A] {
  def quality(a: A): Maybe[(Fit, ViolationCount)]
}

abstract class Comparison(val o: Opt) {
  def apply[A: Quality](a: A, b: A): A
}

object Comparison {

  def compare[A: Quality](x: A, y: A): Reader[Comparison, A] =
    Reader { _.apply(x, y) }

  def quality(o: Opt) = new Comparison(o) {
    def apply[A](a: A, b: A)(implicit Q: Quality[A]) =
      if (o.order(Q.quality(a), Q.quality(b)) === GT) a else b
  }

  def dominance(o: Opt) = new Comparison(o) {
    def apply[A](a: A, b: A)(implicit Q: Quality[A]) = {
      val qA = Q.quality(a)
      val qB = Q.quality(b)

      (qA, qB) match {
        case (Maybe.Just((fA, vA)), Maybe.Just((fB, vB))) =>
          val va = vA.count
          val vb = vB.count

          if (va == 0 && va == 0) quality(o)(a, b)
          else if (va == 0) a
          else if (vb == 0) b
          else if (va < vb) a else b
        case _ => sys.error("Unable to perform comparison. One of the arguments is Maybe.Nothing")
      }
    }
  }
}

sealed trait Opt {
  def order(x: Maybe[(Fit,ViolationCount)], y: Maybe[(Fit,ViolationCount)]): Ordering
}

final case object Min extends Opt {
  private val D = Order[Double].reverseOrder
  private val fitOrder: Order[Fit] = new Order[Fit] {
    def order(x: Fit, y: Fit) =
      (x, y) match {
        case (Penalty(a, _), Penalty(b, _)) => D.order(a, b)
        case (Penalty(a, _), Valid(b)) => D.order(a, b)
        case (Valid(a), Penalty(b, _)) => D.order(a, b)
        case (Valid(a), Valid(b)) => D.order(a, b)
      }
  }

  private def qualityOrder(fitnessOrder: Order[Fit]) =
    new Order[(Fit,ViolationCount)] {
      def order(x: (Fit,ViolationCount), y: (Fit,ViolationCount)) = {
        val a = (x._1, x._2.count)
        val b = (y._1, y._2.count)

        (a,b) match {
          case ((f1, 0), (f2, 0)) =>
            fitnessOrder.order(f1, f2) // feasible (i.e: no constraint violations) compare Fit
          case ((_, 0), (_, _)) => LT
          case ((_, _), (_, 0)) => GT
          case ((_, c1), (_, c2)) =>
            Order[Int].reverseOrder.order(c1, c2) // Check this!
        }
      }
    }

  private val ord = scalaz.Maybe.maybeOrder[(Fit,ViolationCount)](qualityOrder(fitOrder))

  def order(x: Maybe[(Fit,ViolationCount)], y: Maybe[(Fit,ViolationCount)]) =
    ord.order(x, y)
}

final case object Max extends Opt {
  private val D = Order[Double]
  private val fitnessOrder: Order[Fit] = new Order[Fit] {
    def order(x: Fit, y: Fit) =
      (x, y) match {
        case (Penalty(a, _), Penalty(b, _)) => D.order(a, b)
        case (Penalty(a, _), Valid(b)) => D.order(a, b)
        case (Valid(a), Penalty(b, _)) => D.order(a, b)
        case (Valid(a), Valid(b)) => D.order(a, b)
      }
  }

  private def qualityOrder(fitnessOrder: Order[Fit]) =
    new Order[(Fit,ViolationCount)] {
      def order(x: (Fit,ViolationCount), y: (Fit,ViolationCount)) = {
        val a = (x._1, x._2.count)
        val b = (y._1, y._2.count)

        (a,b) match {
          case ((f1, 0), (f2, 0)) =>
            // Both feasible (i.e: no constraint violations) compare Fit
            fitnessOrder.order(f1, f2)
          case ((_, 0), (_, _)) => LT
          case ((_, _), (_, 0)) => GT
          case ((_, c1), (_, c2)) =>
            Order[Int].order(c1, c2) // Check this!
        }
      }
    }

  private val ord = scalaz.Maybe.maybeOrder[(Fit,ViolationCount)](qualityOrder(fitnessOrder))
  def order(x: Maybe[(Fit,ViolationCount)], y: Maybe[(Fit,ViolationCount)]) =
    ord.order(x, y)
}
