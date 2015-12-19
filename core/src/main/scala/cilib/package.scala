import scalaz._

package object cilib {

//  val ::  = ICons
//  val Nil = INil

  // Should expand into a typeclass? Getter?
  type Selection[A] = (List[A], A) => List[A]

  def positive(d: Double): Maybe[Double @@ Tags.Positive] =
    if (d > 0.0) Tag.subst(Maybe.just(d))
    else Maybe.empty

  def negative(d: Double): Maybe[Double @@ Tags.Negative] =
    if (d < 0.0) Tag.subst(Maybe.just(d))
    else Maybe.empty

  type StepS[A,S,B] = StateT[Step[A,?],S,B]

  // Find a better home for this
  implicit object DoubleMonoid extends Monoid[Double] {
    def zero = 0.0
    def append(a: Double, b: => Double) = a + b
  }

  import spire.algebra._
  implicit def PositionModule(implicit sc: Field[Double]) =
    new NormedVectorSpace[Position[Double],Double] {
      implicit def scalar = sc
      def negate(x: Position[Double]) =
        x.map(scalar.negate)

      import spire.implicits._
      def zero: Position[Double] =
        Position(NonEmptyList(0.0), NonEmptyList(spire.math.Interval(0.0, 0.0)))

      def timesl(r: Double, v: Position[Double]): Position[Double] =
        v map (scalar.times(r, _))

      def plus(x: Position[Double], y: Position[Double]) = {
        import scalaz.syntax.align._
        x.align(y).map(_.fold(
          s = x => x,
          t = x => x,
          q = scalar.plus(_, _)
        ))
      }

      import scalaz.syntax.foldable._
      // This is hardcoded to be the Euclidean norm. Can we make this generic?
      def norm(x: Position[Double]): Double =
        math.sqrt(x.foldMap(y => y*y))
    }


    implicit class IntervalOps[A](val interval: spire.math.Interval[A]) extends AnyVal {
    import spire.math.interval.{Bound,ValueBound}

    def ^(n: Int): NonEmptyList[spire.math.Interval[A]] =
      NonEmptyList.nel(interval, List.fill(n-1)(interval))

    private def getValue(b: Bound[A]) =
      ValueBound.unapply(b).getOrElse(sys.error("Empty and Unbounded bounds are not supported"))

    def lowerValue = getValue(interval.lowerBound)
    def upperValue = getValue(interval.upperBound)
  }

}
