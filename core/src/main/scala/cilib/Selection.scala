package cilib

import scalaz.{Ordering => _, _}
import Scalaz._

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive

object Selection {

  private implicit class RicherEphemeralStream[A](val s: EphemeralStream[A]) extends AnyVal {
    def drop(n: Int): EphemeralStream[A] = {
      @annotation.tailrec
      def go(count: Int, c: Option[EphemeralStream[A]]): EphemeralStream[A] =
        if (count > 0) go(count - 1, c.flatMap(_.tailOption))
        else c.cata(x => x, EphemeralStream())

      go(n, Option(s))
    }
  }

  def indexNeighbours[A](n: Int): (NonEmptyList[A], A) => List[A] =
    (l: NonEmptyList[A], x: A) => {
      val list = l
      val size = l.size
      val point =
        l.list.indexWhere(_ == x) match {
          case None    => 0
          case Some(i) => (i - (n / 2) + size) % size
        }
      lazy val c: EphemeralStream[A] = EphemeralStream(list.toList: _*) ++ c

      c.drop(point).take(n).toList
    }

  def latticeNeighbours[A: scalaz.Equal]: (NonEmptyList[A], A) => List[A] =
    (l: NonEmptyList[A], x: A) => {
      val list = l.list
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
        r <- row
        c <- col
        north <- list.index(
          indexInto((r - 1 + nRows) % nRows - (if (c >= colsInRow(r - 1 + nRows) % nRows) 1 else 0),
                    c))
        south <- list.index(indexInto(if (c >= colsInRow(r + 1) % nRows) 0 else (r + 1) % nRows, c))
        east <- list.index(indexInto(r, (c + 1) % colsInRow(r)))
        west <- list.index(indexInto(r, (c - 1 + colsInRow(r)) % colsInRow(r)))
      } yield List(x, north, south, east, west)

      result.getOrElse(sys.error("error in latticeNeighbours"))
    }

  def distanceNeighbours[F[_]: Foldable, A: Order](distance: MetricSpace[F[A], A])(
      n: Int): (NonEmptyList[F[A]], F[A]) => List[F[A]] =
    (l: NonEmptyList[F[A]], x: F[A]) => l.sortBy(li => distance.dist(li, x)).toList.take(n)

  def wheel[A]: (NonEmptyList[A], A) => List[A] =
    (l: NonEmptyList[A], a: A) => {
      if (l.head == a) l.toList
      else List(l.head, a)
    }

  def star[A]: (NonEmptyList[A], A) => List[A] =
    (l: NonEmptyList[A], x: A) => l.toList

  def tournament[F[_], A](n: Int Refined Positive, l: NonEmptyList[F[A]])(
      implicit F: Fitness[F, A, A]): Comparison => RVar[Option[F[A]]] =
    o =>
      RVar
        .sample(n, l) // RVar[Option[List[F[A]]]]
        .map(_.flatMap(_.reduceLeftOption((a, c) => o.apply(a, c))))

}
