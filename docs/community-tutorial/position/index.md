# Position

<div class="callout callout-danger">

Candidate solution vectors within a search space are the basic pieces of information that computational algorithms maintain and,
includes feature vectors that represent training patterns in a neural network.
Within population based algorithms, a collection of algorithm participants are employed in a search of the problem space.
Each represents a possible solution to the problem at hand, and may be in one of two possible states:

1. It may be a "point" in the search space where no other information about the point is known, except for the value of the multi-dimensional vector representing the position within the search space
2. It may be a possible "solution", where the position in the multi-dimensional search space is known but, an additional value representing the "quality" of the vector is also maintained. This "quality" is referred to as the fitness of the candidate solution.

</div>

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

<div class="callout callout-warning">
This is the first chapter were you are going to be using [Refined][Refined-link].
The following imports should be sufficient

```tut:book:silent
import cilib._
import scalaz._
import Scalaz._

import eu.timepit.refined.auto._

import spire.implicits.{eu => _, _}
import spire.math.Interval
```
</div>