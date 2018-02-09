## Position Class

Great. Now that we know how to create positions we may look at what we can do with the `Position` instances,
`Point` and `Solution`.

```scala
pos: NonEmptyList[A]

boundary: NonEmptyList[Interval[A]]

zip[B](other: Position[B]): Position[(A, B)]

take(n: Int): IList[A]

drop(n: Int): IList[A]

objective: Option[Objective[A]]

toPoint: Position[A]

map[B](f: A => B): Position[B]

flatMap[B](f: A => Position[B]): Position[B]

traverse[G[_]: Applicative, B](f: A => G[B]): G[Position[B]]

forall(f: A => Boolean)
```

Now you might recognize some, if not most, of these functions from your own experience with scala.
And since this book is not only a guide of CILib but also to serve as documentation, we will quickly check out these methods.

We will be using the following code to test the methods.
```tut:book:invisible
import cilib._
import spire.implicits._
import spire.math._
import scalaz._
import Scalaz._
```
```tut:book:silent
val rng = RNG.init(12)
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
val intervals = NonEmptyList(Interval(0.0, 4.0), Interval(8.0, 9.0))
val myPos = Position.createPosition(intervals).eval(rng)
```

### pos

Returns the actual position.

```tut:book
myPos.pos
```

### boundary

Returns the boundary.

```tut:book
myPos.boundary
```

### zip

Combines the values of two `Positions` instances in to one instance.

```tut:book
val otherPos = Position.createPosition(intervals).eval(RNG.fromTime)
myPos.zip(otherPos)
```

### take

Returns n amount of points from the `Position`.

```tut:book
myPos.take(1)
```

### drop

Does the same as take. 

```tut:book
myPos.drop(1)
```

### objective

Will return the objective of a `Position`. If the `Position` is a

- `Point` it will return `none`
- `Solution` will return `some(o)` where `o` is the parameter.

### toPoint

Will convert a `Position` into a `Point` type.

### map

```tut:book
myPos.map(x => x * 0.2)
```

### flatMap

```tut:book
myPos.flatMap(x => cilib.Point(NonEmptyList(x * 2), myPos.boundary))
```

### traverse

If we had a list of 3 and 8, and we wanted a list where each element is -x and / or 2x.
The list would be:

- -3, -8
- -3, 16
- 6, -8
- 6, 18

```tut:book
myPos.traverse(x => NonEmptyList(x * -1, x * 2))
```

### forall

Applies a condition to each element.

```tut:book
myPos.forall(x => x > 1)
```