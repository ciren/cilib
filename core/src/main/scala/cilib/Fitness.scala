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
  def quality(a: A): Maybe[(Fit, Int)]
}

abstract class Comparison(val o: Opt) {
  def apply[A: Quality](a: A, b: A): A
}

object Comparison {

  def compare[A: Quality](x: A, y: A): Reader[Comparison, A] =
    Reader { _.apply(x,y) }

  def quality(o: Opt) = new Comparison(o) {
    def apply[A](a: A, b: A)(implicit Q: Quality[A]) =
      if (o.order(Q.quality(a), Q.quality(b)) === GT) a else b
  }

  // def dominance(o: Opt) = new Comparison(o) {
  //   def apply[A](a: A, b: A)(implicit Q: Quality[A]) = {
  //     val qA = Q.quality(a)
  //     val qB = Q.quality(b)
  //
  //     (qA, qB) match {
  //       case (Maybe.Just((fA, vA)), Maybe.Just((fB, vB))) =>
  //         if (vA == 0 && vB == 0) quality(o)(a, b)
  //         else if (vA == 0) a
  //         else if (vB == 0) b
  //         else if (vA < vB) a else b
  //       case _ => sys.error("A")
  //     }
  //   } 
  // }

}

sealed trait Opt {
  def order(x: Maybe[(Fit,Int)], y: Maybe[(Fit,Int)]): Ordering
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
    new Order[(Fit,Int)] {
      def order(x: (Fit,Int), y: (Fit,Int)) =
        (x,y) match {
          case ((f1, 0), (f2, 0)) =>
            // Both feasible (i.e: no constraint violations) compare Fit
            fitnessOrder.order(f1, f2)
          case ((_, 0), (_, _)) => LT
          case ((_, _), (_, 0)) => GT
          case ((_, c1), (_, c2)) =>
            Order[Int].reverseOrder.order(c1, c2) // Check this!
        }
    }

  private val ord = scalaz.Maybe.maybeOrder[(Fit,Int)](qualityOrder(fitOrder))

  def order(x: Maybe[(Fit,Int)], y: Maybe[(Fit,Int)]) =
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
    new Order[(Fit,Int)] {
      def order(x: (Fit,Int), y: (Fit,Int)) =
        (x,y) match {
          case ((f1, 0), (f2, 0)) =>
            // Both feasible (i.e: no constraint violations) compare Fit
            fitnessOrder.order(f1, f2)
          case ((_, 0), (_, _)) => LT
          case ((_, _), (_, 0)) => GT
          case ((_, c1), (_, c2)) =>
            Order[Int].order(c1, c2) // Check this!
        }
    }

  private val ord = scalaz.Maybe.maybeOrder[(Fit,Int)](qualityOrder(fitnessOrder))
  def order(x: Maybe[(Fit,Int)], y: Maybe[(Fit,Int)]) =
    ord.order(x, y)
}
