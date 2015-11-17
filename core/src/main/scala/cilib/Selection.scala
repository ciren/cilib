package cilib

import scala.math.Ordering

import spire.algebra.{Field,NRoot,Signed}
import spire.implicits._

import scalaz.{Ordering => _, _}
import scalaz.syntax.std.option._

object Selection {

  implicit class RicherEphemeralStream[A](s: EphemeralStream[A]) {
    def drop(n: Int): EphemeralStream[A] = {
      def go(count: Int, c: Option[EphemeralStream[A]]): EphemeralStream[A] = {
        if (count > 0) {
          go(count - 1, c.flatMap(_.tailOption))
        } else c.cata(x => x, EphemeralStream())
      }

      go(n, Option(s))
    }
  }

  def indexNeighbours[A](n: Int): Selection[A] =
    (l: List[A], x: A) => {
      val size = l.size
      val point = (l.indexOf(x) - (n / 2) + size) % size
      lazy val c: EphemeralStream[A] = EphemeralStream(l: _*) ++ c

      c.drop(point).take(n).toList
    }

  def distanceNeighbours[F[_]: Foldable, A: Field : Ordering : NRoot : Signed](distance: Distance[F,A])(n: Int) =
    (l: List[F[A]], x: F[A]) => l.sortBy(li => distance(li, x)).take(n)

  def wheel[A]: Selection[A] =
    (l: List[A], a: A) => l match {
      case x :: _ if (x == a) => l
      case x :: _ => List(x, a)
    }

  def star[A]: Selection[A] =
    (l: List[A], _) => l
}
