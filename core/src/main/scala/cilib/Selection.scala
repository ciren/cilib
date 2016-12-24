package cilib

import scala.math.Ordering

import spire.algebra.{Field,NRoot,Signed}
import spire.implicits._

import scalaz.{Ordering => _, _}
import scalaz.syntax.std.option._

object Selection {

  implicit class RicherEphemeralStream[A](val s: EphemeralStream[A]) extends AnyVal {
    def drop(n: Int): EphemeralStream[A] = {
      def go(count: Int, c: Option[EphemeralStream[A]]): EphemeralStream[A] = {
        if (count > 0) {
          go(count - 1, c.flatMap(_.tailOption))
        } else c.cata(x => x, EphemeralStream())
      }

      go(n, Option(s))
    }
  }

  def indexNeighbours[A](n: Int): (List[A], A) => List[A] =
    (l: List[A], x: A) => {
      val list = l
      val size = l.size
      val point = (list.indexOf(x) - (n / 2) + size) % size
      lazy val c: EphemeralStream[A] = EphemeralStream(list: _*) ++ c

      c.drop(point).take(n).toList
    }

  def latticeNeighbours[A: scalaz.Equal]: (List[A], A) => List[A] =
    (l: List[A], x: A) => {
      import scalaz.syntax.foldable._
      val list = IList.fromList(l)
      val np = list.length
      val index: Option[Int] = list.indexOf(x) // This returns Option[Int] instead of Int, which is awesome :)
      val sqSide = math.round(math.sqrt(np.toDouble)).toInt
      val nRows = math.ceil(np / sqSide.toDouble).toInt
      val row: Option[Int] = index.map(_ / sqSide)
      val col: Option[Int] = index.map(_ % sqSide)

      @inline def indexInto(r: Int, c: Int) =
        r * sqSide + c

      @inline val colsInRow =
        (r: Int) => if (r == nRows - 1) np - r * sqSide else sqSide

      val result = for {
        r     <- row
        c     <- col
        north <- list.index(indexInto((r - 1 + nRows) % nRows - (if (c >= colsInRow(r - 1 + nRows) % nRows) 1 else 0), c))
        south <- list.index(indexInto(if (c >= colsInRow(r + 1) % nRows) 0 else (r + 1) % nRows, c))
        east  <- list.index(indexInto(r, (c + 1) % colsInRow(r)))
        west  <- list.index(indexInto(r, (c - 1 + colsInRow(r)) % colsInRow(r)))
      } yield List(x, north, south, east, west)

      result.getOrElse(sys.error("error in latticeNeighbours"))
    }

  def distanceNeighbours[F[_]: Foldable, A: Field : Ordering : NRoot : Signed](distance: MetricSpace[F[A],A])(n: Int) =
    (l: List[F[A]], x: F[A]) => l.sortBy(li => distance.dist(li, x)).take(n)

  def wheel[A] =
    (l: List[A], a: A) => {
      l match {
        case x :: _ if (x == a) => l
        case x :: _ => List(x, a)
      }
    }

  def star[A] =
    (l: List[A], x: A) => l

  def tournament[F[_],A](n: Int, l: List[F[A]])(implicit F: Fitness[F,A]): Comparison => RVar[Option[F[A]]] =
    o => RVar.sample(n, l)
      .map(_.reduceLeftOption((a,c) => o.apply(a, c)))
      .run
      .map(_.flatten)

}
