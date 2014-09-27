package cilib

import _root_.scala.Predef.{any2stringadd => _, _}
import scalaz._
import Ordering._

import scalaz.std.anyVal._
import scalaz.syntax.equal._

sealed trait Fit
final case class Penalty(v: Double, p: Double) extends Fit
final case class Valid(v: Double) extends Fit
//final object Invalid extends Fit

@annotation.implicitNotFound("Cannot find instance of type class Fitness[${A}]")
trait Fitness[A] {
  def fitness(a: A): Option[Fit]
}

object Fitness {

  def compare[A](x: A, y: A)(implicit F: Fitness[A]): Reader[Opt, A] =
    Reader(o => if (o.order(F.fitness(x), F.fitness(y)) === GT) x else y)

}

sealed trait Opt extends Order[Option[Fit]] {
  def order(x: Option[Fit], y: Option[Fit]): Ordering
}

final case object Min extends Opt {
  private val D = Order[Double].reverseOrder
  private val fitnessOrder: Order[Fit] = new Order[Fit] {
    def order(x: Fit, y: Fit) =
      (x, y) match {
        case (Penalty(a, _), Penalty(b, _)) => D.order(a, b)
        case (Penalty(a, _), Valid(b)) => D.order(a, b)
        case (Valid(a), Penalty(b, _)) => D.order(a, b)
        case (Valid(a), Valid(b)) => D.order(a, b)
      }
  }

  private val ord = scalaz.std.option.optionOrder[Fit](fitnessOrder)

  def order(x: Option[Fit], y: Option[Fit]) =
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

  private val ord = scalaz.std.option.optionOrder[Fit](fitnessOrder)
  def order(x: Option[Fit], y: Option[Fit]) =
    ord.order(x, y)
}
