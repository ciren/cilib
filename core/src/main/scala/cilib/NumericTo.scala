package cilib

trait NumericTo[@specialized A] {
  def fromDouble(a: Double): A
}

object NumericTo {
  @inline def apply[A](implicit N: NumericTo[A]) = N

  implicit val NumericToDouble: NumericTo[Double] =
    new NumericTo[Double] {
      def fromDouble(a: Double): Double = a
    }
}
