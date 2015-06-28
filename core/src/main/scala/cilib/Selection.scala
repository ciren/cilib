package cilib

object Selection {

  import scalaz._
  import scalaz.syntax.applicative._
  import scalaz.syntax.std.option._
  import scalaz.syntax.std.list._
  import scalaz.std.option._
  import scalaz.std.list._

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

  def indexNeighbours[A](n: Int): Selection[A] =
    (l: NonEmptyList[A], x: A) => {
      val list = l.list
      val size = l.size
      val point = (list.indexOf(x) - (n / 2) + size) % size
      lazy val c: EphemeralStream[A] = EphemeralStream(list: _*) ++ c

      c.drop(point).take(n).toList.toNel
    }

  def latticeNeighbours[A: Order]: Selection[A] =
    (l: NonEmptyList[A], x: A) => {
      import scalaz.syntax.foldable._
      val list = IList.fromList(l.list)
      val np = list.length
      val index = list.indexOf(x) // This returns Option[Int] instead of Int, which is awesome :)
      val sqSide = math.round(math.sqrt(np.toDouble)).toInt
      val nRows = math.ceil(np / sqSide.toDouble).toInt
      val row: Option[Int] = index.map(_ / sqSide)
      val col: Option[Int] = index.map(_ % sqSide)

      @inline def indexInto(r: Int, c: Int) =
        r * sqSide + c

      @inline val colsInRow =
        (r: Int) => if (r == nRows - 1) np - r * sqSide else sqSide

      for {
        r     <- row
        c     <- col
        north <- list.index(indexInto((r - 1 + nRows) % nRows - (if (c >= colsInRow(r - 1 + nRows) % nRows) 1 else 0), c))
        south <- list.index(indexInto(if (c >= colsInRow(r + 1) % nRows) 0 else (r + 1) % nRows, c))
        east  <- list.index(indexInto(r, (c + 1) % colsInRow(r)))
        west  <- list.index(indexInto(r, (c - 1 + colsInRow(r)) % colsInRow(r)))
      } yield NonEmptyList(x, north, south, east, west)

    }
}
