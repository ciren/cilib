## Selection Object

All of our selection based needs can be solved with the `Selection` object.
This object is simple in regards that is it acts as a helper object to contain methods to create selection functions.
Like a companion object without the companion part.
Another interesting point is that all the methods in the object return functions.
The reason for this is so that we may, for example, specify types to a general
tournament selection method to return a tournament function specify to our types.
Now that we are familiar with the `Selection` object, here is a list of the currently available:

* Index Neighbours
* Lattice Neighbours
* Distance Neighbours
* Wheel
* Star
* Tournament

With the following formal definitions:

```scala
indexNeighbours[A](n: Int): (NonEmptyList[A], A) => List[A]

latticeNeighbours[A: scalaz.Equal]: (NonEmptyList[A], A) => List[A]

distanceNeighbours[F[_]: Foldable, A: Order](distance: MetricSpace[F[A],A])(n: Int): (NonEmptyList[F[A]], F[A]) => List[F[A]]

wheel[A]: (NonEmptyList[A], A) => List[A]

star[A]: (NonEmptyList[A], A) => List[A]

tournament[F[_],A](n: Int, l: NonEmptyList[F[A]])(implicit F: Fitness[F,A]): Comparison => RVar[Option[F[A]]]
```

Most of functions returned require some `NonEmptyList` of type `A`, with another value of Type `A`.
However, ``distanceNeighbours`` and ``tournament`` are a bit unique so we will exploring these in our examples.

### Example 1 - Tournament Selection

```tut:book:silent
import cilib._
import scalaz._
import Scalaz._
import eu.timepit.refined.auto._
import spire.implicits.{eu => _, _}
import spire.math.Interval

val rng = RNG.init(12L)
val intervals = NonEmptyList(Interval(-5.0, 5.0), Interval(10.0, 15.0))
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x*x).suml).eval
val solutions = Position.createPositions(intervals, 9).eval(rng).map(p => Position.eval(e, p).eval(rng))
```
```tut:book
Selection.tournament(3, solutions).apply(Comparison.dominance(Max)).eval(rng).get
```

What is going on here is that we created `List` of 9 `Solutions`.
We then passed the solutions to the tournament selection along with an `Int` that represents how many `Solutions` will partake in the tournament.

### Example 2 - Distance Neighbours Selection

```tut:book:silent
import cilib._
import scalaz._
import Scalaz._
import spire.implicits._

val a = NonEmptyList(1.0, 2.0)
val b = NonEmptyList(3.0, 4.0)
val c = NonEmptyList(5.0, 6.0)
val d = NonEmptyList(7.0, 8.0)
val e = NonEmptyList(9.0, 10.0)
val collection = NonEmptyList(a, b, c, d, e)
```
```tut:book
val ringDistance = Selection.distanceNeighbours[NonEmptyList,Double](MetricSpace.euclidean)(3)

ringDistance(collection, a) 
ringDistance(collection, c)
ringDistance(collection, e)
```

Here we used our knowledge of the previous chapter, metric spaces, to create a selection based on the distance between neighbours.