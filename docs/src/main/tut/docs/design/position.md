---
layout: docs
title: "Position"
---

# Position

Candidate solution vectors within a search space are the basic piece of data
that computational algorithms maintain. Granted, a neural network may have
a different repesentation (although generally they are feature vectors), but
within the space of population based algorithms, a collection of algorithm
participants are employed in the search of the problem space. Each represents
a possible solution to the problem at hand, but these candidate vectors
may be in one of two possible states:

1. It may be a "point" in the search space where no other information about
   the point is known, except for the value of the multi-dimensional vector
   representing the position within the search space
2. It may be a possible "solution", where the position in the multi-dimensional
   search space is known but, additionally, a value that represents the
   "quality" of the value is also maintained. This "quality" value is
   referred to as the fitness of the candidate solution.

`Position` is a data structure that encodes the above two cases
exactly, allowing a `Position` to either be a `Point` or a `Solution`.
Further more, `Position` is an Algebraic Data Type (ADT), whereby the set
of operations on the data type is closed. Any changes to a `Solution` will,
for example, will yield a `Point` - the new `Position` has not yet had a
fitness calculated for it and the quality of the `Position` is an unknown.

```tut:invisible
import cilib._
import scalaz.NonEmptyList
import spire.implicits._
import spire.math._
```

A `Position`, within a search space, can be created by providing the search
sapce bounds to the `createPosition` function. The search space must have
at least a single dimension and, a `NonEmptyList[Interval[Double]]` represents
the search space bounds. A `NonEmptyList` is a list that is guaranteed to have
at least one contained element

```tut
Position.createPosition(NonEmptyList(Interval(-5.12,5.12)))
```
