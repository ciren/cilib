package cilib

object Selection {

  def indexNeighbours[A](n: Int): Selection[A] =
    (l: List[A], x: A) => {
      val size = l.size
      val y = n / 2
      val point = (l.indexOf(x) - (n / 2) + size) % size

      Stream.continually(l).flatten.drop(point).take(n).toList
    }
}
