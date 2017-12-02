//package cilib

//import spire.math._

// sealed abstract case class Positive[A](value: A)
// object Positive {
//   def apply[A:Numeric](n: A): Option[Positive[A]] =
//     if (Numeric[A].toDouble(n) >= 0.0) Some(new Positive(n) {})
//     else None
// }

// sealed abstract case class Negative[A](value: A)
// object Negative {
//   def apply[A:Numeric](n: A): Option[Negative[A]] =
//     if (Numeric[A].toDouble(n) < 0.0) Some(new Negative(n) {})
//     else None
// }
