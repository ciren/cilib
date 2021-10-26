package cilib

import zio.prelude._
import zio.prelude.newtypes.Natural

object Selection {

  def indexNeighbours[A](n: Int): (NonEmptyVector[A], A) => List[A] =
    (list: NonEmptyVector[A], x: A) => {
      val size  = list.size
      val point =
        list.zipWithIndex.find(_._1 == x) match {
          case None         => 0
          case Some((_, i)) => (i - (n / 2) + size) % size
        }
      val chunk = list.toChunk

      (0 until n).toList.map { c =>
        chunk((point + c) % size)
      }
    }

  def latticeNeighbours[A: zio.prelude.Equal]: (NonEmptyVector[A], A) => List[A] =
    (l: NonEmptyVector[A], x: A) => {
      val list               = l.zipWithIndex
      val np                 = l.length
      val index: Option[Int] = list.find(_._1 == x).map(_._2)
      val sqSide             = math.round(math.sqrt(np.toDouble)).toInt
      val nRows              = math.ceil(np / sqSide.toDouble).toInt
      val row: Option[Int]   = index.map(_ / sqSide)
      val col: Option[Int]   = index.map(_ % sqSide)

      @inline def indexInto(r: Int, c: Int) =
        r * sqSide + c

      @inline def colsInRow(r: Int) =
        if (r == nRows - 1) np - r * sqSide else sqSide

      val result = for {
        r     <- row
        c     <- col
        north <- list
                   .find(
                     _._2 == indexInto(
                       (r - 1 + nRows) % nRows - (if (c >= colsInRow(r - 1 + nRows) % nRows) 1 else 0),
                       c
                     )
                   )
                   .map(_._1)
        south <- list.find(_._2 == indexInto(if (c >= colsInRow(r + 1) % nRows) 0 else (r + 1) % nRows, c)).map(_._1)
        east  <- list.find(_._2 == indexInto(r, (c + 1) % colsInRow(r))).map(_._1)
        west  <- list.find(_._2 == indexInto(r, (c - 1 + colsInRow(r)) % colsInRow(r))).map(_._1)
      } yield List(x, north, south, east, west)

      result.getOrElse(sys.error("error in latticeNeighbours"))
    }

  def distanceNeighbours[F[+_]: ForEach, A: Ord](
    distance: MetricSpace[F[A], A]
  )(n: Int): (NonEmptyVector[F[A]], F[A]) => List[F[A]] =
    (l: NonEmptyVector[F[A]], x: F[A]) =>
      l.toChunk.toList.sortBy(li => distance.dist(li, x))(implicitly[Ord[A]].toScala).take(n)

  def wheel[A]: (NonEmptyVector[A], A) => List[A] =
    (l: NonEmptyVector[A], a: A) =>
      if (l.head == a) l.toChunk.toList
      else List(l.head, a)

  def star[A]: (NonEmptyVector[A], A) => List[A] =
    (l: NonEmptyVector[A], _: A) => l.toChunk.toList

  def tournament[F[_], A](n: Natural, l: NonEmptyVector[F[A]])(implicit
    F: Fitness[F, A, A]
  ): cilib.Comparison => RVar[Option[F[A]]] =
    o =>
      RVar
        .sample(n, l)
        .map(_.flatMap(_.reduceLeftOption((a, c) => o.apply(a, c))))

}
