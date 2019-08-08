---
id: constraint
title: Constraints
---

As we begin to explore more of the core concepts of CILib we start to see how they are utilized by other concepts.
With this in mind, you might see types specific to CILib you haven't come across yet.
Dont worry, these will be explained in their appropriate sections later in the book.
In this chapter we are going to look at `Constraint`.
`Constraints` are used to place, you guessed it, constraints on random variables.
`Constraint` provides us with several types as well as a companion object for us to use.


## Constraint Classes

```scala
LessThan[A,B](f: ConstraintFunction[A,B], v: B)

LessThanEqual[A,B](f: ConstraintFunction[A,B], v: B)

Equal[A,B](f: ConstraintFunction[A,B], v: B)

InInterval[A,B](f: ConstraintFunction[A,B], interval: Interval[B])

GreaterThan[A,B](f: ConstraintFunction[A,B], v: B)

GreaterThanEqual[A,B](f: ConstraintFunction[A,B], v: B)
```

All `Constraint` classes make use of two parameters

- The constraint function that will compute a result
- An expected or appropriate value to compare against the result

What are `Constraint` classes used for?
Well, it allows us to define the context for which our constraint isd based on.
For example, we need the sum of our list of numbers to be less than 12.
Our `ConstraintFunction` computes the sum of all the values in the list.
This context can then be used in other core components to determine if a list of numbers is a feasible or infeasible solution,
by comparing it with 12, our second parameter.

### ConstraintFunction

`ConstraintFunction` has a simple class definition `ConstraintFunction[A,B](f: NonEmptyList[A] => B)`.
All we have to do is supply a function that will that takes a `NonEmptyList` and produces a result.

<div class="callout callout-info">
`ConstraintFunctions` are used to produce a result that we may use to compare with in our constraint.
</div>

```scala mdoc:silent
import cilib._
import scalaz._
import Scalaz._
val sumCF = ConstraintFunction((l: NonEmptyList[Double]) => l.suml)
```
```scala mdoc
sumCF(NonEmptyList(2.0, 4.0, 7.5))
```

### Constraint Example

```scala mdoc
LessThan(sumCF, 12.0)
```

We have successfully defined our constraint context.
Hold on to this because we are going to put it into action in the next section.

## Constraint Companion Object

<div class="callout callout-info">
The `Constraint` object allows us to use our defined constraints against `NonEmptyLists`.
</div>

Through the object we are able to make use og the following methods

```scala
satisfies[A,B:Fractional](constraint: Constraint[A,B], cs: NonEmptyList[A])(implicit ev: Eq[B]): Boolean

violationCount[A,B:Fractional](constraints: List[Constraint[A,B]], cs: NonEmptyList[A]): ViolationCount

violationMagnitude[A,B:Fractional](beta: Double, eta: Double, constraints: List[Constraint[A,B]], cs: NonEmptyList[A])(implicit e: Eq[B]): Double
```

### satisfies

Given a `NonEmptyList`, it will be checked using the given `Constraint` and a `Boolean` will be returned to indicate if
the list meets the constraints.

```scala mdoc:invisible
import cilib._
import scalaz._
import Scalaz._
```
```scala mdoc:silent
import spire.implicits._

val sumLessThanCons = LessThan(sumCF, 12.0)
```
```scala mdoc
Constraint.satisfies(sumLessThanCons, NonEmptyList(2.0, 3.0, 4.0))
Constraint.satisfies(sumLessThanCons, NonEmptyList(9.0, 10.0, 11.0))
```

### violationCount

A `NonEmptyList` will be checked against a `List` of `Constraints`.
An `Int` is returned representing the number of `Constraints` the list violated.

```scala mdoc:silent
import spire.implicits._
import spire.algebra.Eq

// A constraint that ensures the first element of a list is 4
val firstNumberCons = cilib.Equal(ConstraintFunction((l: NonEmptyList[Double]) => l.head), 4.0)
```

```scala mdoc
Constraint.violationCount(List(sumLessThanCons, firstNumberCons), NonEmptyList(19.0, 37.23, 12.0))
Constraint.violationCount(List(sumLessThanCons, firstNumberCons), NonEmptyList(4.0, 3.0, 2.0))
Constraint.violationCount(List(sumLessThanCons, firstNumberCons), NonEmptyList(4.0, 3.0, 2.0)).count
```

### violationMagnitude

Determines the magnitude of the number of violated constraints.

```scala mdoc
Constraint.violationMagnitude(0.1, 0.9, List(sumLessThanCons, firstNumberCons), NonEmptyList(19.0, 37.23, 12.0))
```
## Exercises

### Question 1
Define a `Constraint` to ensure that the head of a list is between 5 and 10.

<div class="solution">
```scala mdoc:invisible
import cilib._
import scalaz._
import Scalaz._
import spire.implicits._
```
```scala mdoc:silent
import spire.math.Interval
InInterval(ConstraintFunction((l: NonEmptyList[Double]) => l.head), Interval(5, 10))
```
</div>

### Question 2
Assume random numbers are generated between 5 and 10.
Write a function that accepts such a list and returns true or false
whether the list meets the constraint you defined in the previous question.
You need to check each element in the list.

<div class="solution">
```scala mdoc:silent
val cons = InInterval(ConstraintFunction((l: NonEmptyList[Double]) => l.head), Interval(5, 10))
def checkList(l: List[Double]): Boolean = {
    if (l.size == 0) {
        true
    } else {
        val head = NonEmptyList(l.head)
        Constraint.satisfies(cons, head) && checkList(l.tail)
    }
}
```
</div>

## Summary

We now know how to implement constraints on our lists.
In the next chapter we will see how `Constraints` are used further in CILib.

<div class="callout callout-info">
All `Constraint` classes make use of two parameters

- The constraint function that will compute a result
- An expected or appropriate value to compare against the result

This defines a constraint context that can used on `NonEmptyLists` through the companion object.
</div>
