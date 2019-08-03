## Position Companion Object

The companion object offers a few ways for us create instances of a `Position` or collections of `Positions`.

```scala
eval[A](e: Eval[A], pos: Position[A]): RVar[Position[A]]

createPosition[A](domain: NonEmptyList[Interval[Double]]): RVar[Position[A]]

createPositions(domain: NonEmptyList[Interval[Double]], n: Int Refined GreaterEqual[_1]): RVar[List[Position[A]]]

createCollection[A](f: Position[Double] => A)(domain: NonEmptyList[Interval[Double]], n: Int Refined GreaterEqual[_1]): RVar[List[A]]
```

There is also a handful implicit definitions within the object definition that if you want can check out over here [here][cilib-position-object].

You might have noticed that these methods all return an `RVar[A]`.
This is because we want to generate random positions within our search space.
And due to `Position's` class definition we will be able to transform our `Positions` within the `RVar` context.

As we explore the object will be making use of the following code: 

```tut:book:invisible
import cilib._
```
```tut:book:silent
val rng = RNG.init(12)
```

### eval

For `eval` we will need to supply a `Position`, which we will create, as well as a list of `Intervals`.
This method will an `RVar` of type `Solution` (another `Position`type).
```tut:book:invisible
import cilib._
import spire.implicits.{eu => _, _}
import spire.math._
import scalaz._
import Scalaz._

val rng = RNG.init(12)
```
```tut:book:silent
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
val list = NonEmptyList(5.0, 4)
val intervals = NonEmptyList(Interval(0.0, 4.0), Interval(8.0, 9.0))
val point = cilib.Point(list, intervals)
```
```tut:book
Position.eval(e, point).run(rng)
```

### createPosition

`createPosition` will return a `RVar[Position[A]]`, where this `Position` will be a `Point`.
When the `RVar` is *run*, it will produce the usual tuple we are familiar with, where the `Point` will contain a randomly generated number
for each `Interval` (dimension).
Each `Interval` in the list represents a dimension.
The `Intervals` do not need to be the same, meaning we can have different `Intervals` for different dimensions.
The use of `NonEmptyLists` ensure that we will, at minimum, we have one dimension.

```tut:book
Position.createPosition(intervals).run(rng)
```

CILib also adds some syntax has been added to the `Interval` data constructor to allow for repetition in a more convenient way.
Allowing us to create multi dimensional positions with ease.

```tut:book
Position.createPosition(Interval(-5.12,5.12)^5)
```

We created a 5 dimensional search space where each dimension ranges from -5.12 to 5.12

### createPositions

Like `createPosition`, but will result in a `RVar` of a list of `Positions`.
Where n is the size of the parameter.

```tut:book:silent
import eu.timepit.refined.auto._
val intervals = NonEmptyList(Interval(0.0, 4.0), Interval(8.0, 9.0))
```
```tut:book
val pos = Position.createPositions(intervals, 2)
```

### createCollection

Like `createPositions`, but will result in a `RVar` of a list of `A`.
We are able to have a result of `List[A]` because the method allows us to pass our own function as a parameter.

```tut:book:silent
import eu.timepit.refined.auto._
```
```tut:book
Position.createCollection(x => x)(intervals, 2).run(rng)
Position.createCollection(x => x.map(_ * 2))(intervals, 2).run(rng)
```