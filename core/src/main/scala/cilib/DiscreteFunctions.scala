package cilib

object DiscreteFunctions {

  def hammingDistance[A](a: List[A]) =
    (x: List[A]) => Some(a.zip(x).map { case (ai, xi) =>
      ai != xi
    }.filter(z => z).length)

  def onemax(x: List[Boolean]) = Some(x.filter(_ == true).length)

  def order3Bipolar(x: List[Boolean]) =
    if (x.length % 6 == 0) {
      def sum(a: List[Boolean], s: Double): Double = {
        def next(s1: Double) = sum(a.drop(6), s + s1)

        a match {
          case List() => s
          case _      =>
            a.take(6).filter(_ == true).length match {
              case 0 | 6 => next(1.0)
              case 1 | 5 => next(0.0)
              case 2 | 4 => next(0.4)
              case _     => next(0.8)
            }
        }
      }

      Some(sum(x, 0.0))
    } else None

  def order3Deceptive(x: List[Boolean]) =
    if (x.length % 3 == 0) {
      def sum(a: List[Boolean], s: Double): Double = {
        def next(s1: Double) = sum(a.drop(3), s + s1)

        a match {
          case List() => s
          case _      => {
            val l = a.take(3).filter(_ == true).length
            l match {
              case 3 => next(1.0)
              case _ => next(0.9 - 0.3 * l)
            }
          }
        }

      }

      Some(sum(x, 0.0))
    } else None
}
