---
id: position
title: Position
---

:::caution
Work in progress
:::

The vectors within a population-based optimization algorithm (such as evolutionary and swarm-intelligence algorithms) represent the possible solutions to the current optimization problem.
These "candidate solutions" are locations within the problem search space which the optimization problem is currently evaluating.
Because the candidate solutions change over time as the optimization proceeds with the problme solution search, the candidate solutions may exist in one of two posible states:

1. A candidate solution may be a "point" within the search space. For this location within multi-dimensional vector space, no other information is known.
2. The candidate solution's location within the multi-dimensional vector space is known, but also the "quality" of the candidate solution has been deteremined.
   The quality of the candidate solution is determined by evaluating the optimization problem with the candidate solution as input.

The data structure `Position` represents the above description of candidate solutions, and has 2 possible states which cannot be extended:

1. `Point` - a candidate solution that maintains the location within the problem search space
2. `Solution` - canidate solutions that maintain both the location within the problem search space and the evaluaiton result of the location by the optimization problem

`Position` values implement a *closed* algebra of operations.
This operations (such as addition and multiplication) are the same operations that are expected to exist when workting with vectors.
Notably, when the resulting `Position` value from an operation is a new location within the problem search space, the `Position` will be a `Point` value.
Similarly, re-evaluating a `Solution` value will not actually re-evaulate the candidate solution, as the candidate solution is an immutable value and the already evaluated value can be used as is.


## Creating Positions

Creating a `Position` value results in an opaque value - it is not a given that the value will be either a `Point` or a `Solution`.



<!--
:::caution
Candidate solution vectors within a search space are the basic pieces of information that computational algorithms maintain and,
includes feature vectors that represent training patterns in a neural network.
Within population based algorithms, a collection of algorithm participants are employed in a search of the problem space.
Each represents a possible solution to the problem at hand, and may be in one of two possible states:

1. It may be a "point" in the search space where no other information about the point is known, except for the value of the multi-dimensional vector representing the position within the search space
2. It may be a possible "solution", where the position in the multi-dimensional search space is known but, an additional value representing the "quality" of the vector is also maintained. This "quality" is referred to as the fitness of the candidate solution.
:::

The above explanation was provided by [Gary Pamparà][link-gary] on the [CILib docs project][cilib-docs].
Up until now we have primarily been generating random candidate solutions and evaluating them.
In the last chapter's questions we briefly looked at modifying candidate solutions.
However, we weren't really exploring the search space.
In this chapter we will look at CILib's `Position` data type and how we can start defining search spaces.

`Position` is a data structure that can either be a:

- `Point`
- `Solution`

Because of these two classes we are able to represent the above cases that Gary mentioned perfectly.
`Point` has the following constructor `Point(x: NonEmptyList[A], b: NonEmptyList[Interval[Double]])` where

- x is a list of point that make up the position in the search space.
- b are the intervals that which the points lie in.

While `Solution` contains these, as well an `Object` to represent its fitness.
Thus has the following constructor `Solution(x: NonEmptyList[A], b: NonEmptyList[Interval[Double]], o: Objective[A])`.
Any changes to a `Solution` will result in a `Point`.
Remember that a `Solution` is `Point` with a fitness evaluation.

`Position` is Algebraic Data Type (ADT) meaning that the set of possible representations may not be extended.
And of course, we are provided with a companion object.
Unlike other chapters we are first going to explore the companion object before we go any further.

:::note
This is the first chapter were you are going to be using [Refined][Refined-link].
The following imports should be sufficient

```scala :silent
import cilib._
import scalaz._
import Scalaz._

import eu.timepit.refined.auto._

import spire.implicits.{eu => _, _}
import spire.math.Interval
```
:::


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

```scala :invisible
import cilib._
```
```scala :silent
val rng = RNG.init(12)
```

### eval

For `eval` we will need to supply a `Position`, which we will create, as well as a list of `Intervals`.
This method will an `RVar` of type `Solution` (another `Position`type).
```scala
import cilib._
import spire.implicits.{eu => _, _}
import spire.math._
import scalaz._
import Scalaz._

val rng = RNG.init(12)
```
```scala
val e = Eval.unconstrained[NonEmptyList,Double](vec => Feasible(vec.map(x => x * x).suml)).eval
val list = NonEmptyList(5.0, 4)
val intervals = NonEmptyList(Interval(0.0, 4.0), Interval(8.0, 9.0))
val point = cilib.Point(list, intervals)
```
```scala
Position.eval(e, point).run(rng)
```

### createPosition

`createPosition` will return a `RVar[Position[A]]`, where this `Position` will be a `Point`.
When the `RVar` is *run*, it will produce the usual tuple we are familiar with, where the `Point` will contain a randomly generated number
for each `Interval` (dimension).
Each `Interval` in the list represents a dimension.
The `Intervals` do not need to be the same, meaning we can have different `Intervals` for different dimensions.
The use of `NonEmptyLists` ensure that we will, at minimum, we have one dimension.

```scala
Position.createPosition(intervals).run(rng)
```

CILib also adds some syntax has been added to the `Interval` data constructor to allow for repetition in a more convenient way.
Allowing us to create multi dimensional positions with ease.

```scala
Position.createPosition(Interval(-5.12,5.12)^5)
```

We created a 5 dimensional search space where each dimension ranges from -5.12 to 5.12

### createPositions

Like `createPosition`, but will result in a `RVar` of a list of `Positions`.
Where n is the size of the parameter.

```scala :silent
import eu.timepit.refined.auto._
val intervals = NonEmptyList(Interval(0.0, 4.0), Interval(8.0, 9.0))
```
```scala
val pos = Position.createPositions(intervals, 2)
```

### createCollection

Like `createPositions`, but will result in a `RVar` of a list of `A`.
We are able to have a result of `List[A]` because the method allows us to pass our own function as a parameter.

```scala :silent
import eu.timepit.refined.auto._
```
```scala
Position.createCollection(x => x)(intervals, 2).run(rng)
Position.createCollection(x => x.map(_ * 2))(intervals, 2).run(rng)
```

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
```scala :invisible
import cilib._
import spire.implicits._
import spire.math._
import scalaz._
import Scalaz._
```
```scala :silent
val rng = RNG.init(12)
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
val intervals = NonEmptyList(Interval(0.0, 4.0), Interval(8.0, 9.0))
val myPos = Position.createPosition(intervals).eval(rng)
```

### pos

Returns the actual position.

```scala
myPos.pos
```

### boundary

Returns the boundary.

```scala
myPos.boundary
```

### zip

Combines the values of two `Positions` instances in to one instance.

```scala
val otherPos = Position.createPosition(intervals).eval(RNG.fromTime)
myPos.zip(otherPos)
```

### take

Returns n amount of points from the `Position`.

```scala
myPos.take(1)
```

### drop

Does the same as take.

```scala
myPos.drop(1)
```

### objective

Will return the objective of a `Position`. If the `Position` is a

- `Point` it will return `none`
- `Solution` will return `some(o)` where `o` is the parameter.

### toPoint

Will convert a `Position` into a `Point` type.

### map

```scala
myPos.map(x => x * 0.2)
```

### flatMap

```scala
myPos.flatMap(x => cilib.Point(NonEmptyList(x * 2), myPos.boundary))
```

### traverse

If we had a list of 3 and 8, and we wanted a list where each element is -x and / or 2x.
The list would be:

- -3, -8
- -3, 16
- 6, -8
- 6, 18

```scala
myPos.traverse(x => NonEmptyList(x * -1, x * 2))
```

### forall

Applies a condition to each element.

```scala
myPos.forall(x => x > 1)
```


## Maths With Positions

`Positions` also may be used with normal vector operations.
This allows for simpler usage as it mirrors the mathematics defined in literature more closely.

```scala :invisible
import cilib._
import spire.implicits._
import spire.math._
import scalaz._
import Scalaz._
```

```scala :silent
val rng = RNG.init(1234L)
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x*x).suml).eval
val a = Position.createPosition(Interval(-5.12,5.12)^3).eval(rng)
val b = Position.createPosition(Interval(-5.12,5.12)^3).flatMap(p => Position.eval(e, p)).eval(rng)
```

```scala
a + b // Add Point and Solution
a + a // Add Point and Point
b + b // Add Solution and Solution
a - b // Subtract Solution from Point
```

## Summary

CILib makes creating search spaces incredibly easy.
And with this we can start making our programs simpler while adding more functionality.
For example, in the previous chapter you were asked to create a program to solve for a cost effective solution for building a bow and arrow. With search spaces it could look something like this...

```scala :invisible
import cilib._
import scalaz._
import Scalaz._

import eu.timepit.refined.auto._

import spire.implicits.{eu => _, _}
import spire.math.Interval
```
```scala :silent
val rng = RNG.init(12)
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval

val rng = RNG.init(12L)
def fitness (l: NonEmptyList[Double]): Double = l.suml
val distanceConstraint = GreaterThanEqual(ConstraintFunction((l: NonEmptyList[Double]) => l.index(0).getOrElse(0)) , 38)
val damageConstraint = GreaterThanEqual(ConstraintFunction((l: NonEmptyList[Double]) => l.index(1).getOrElse(0)), 520)
val e = Eval.constrained[NonEmptyList, Double](fitness(_), List( damageConstraint, distanceConstraint)).eval
val intervals = NonEmptyList(Interval(0.0, 5.0), Interval(0.0, 10.0))

val result = for {
     positions <- Position.createPositions(intervals, 5) // This creates a NonEmptyList[Position] for us
     evaluated <- positions.traverse(p => Position.eval(e, p)) // We need to "traverse" the collection so that we apply the given function to all values within the RVar sub-program
} yield evaluated
```
```scala
result.eval(rng)
```

:::info
`Position` allows us points in a search space.
These points can then be evaluated which will yield a solution.
A solution is a point with a fitness evaluation.
:::
-->
