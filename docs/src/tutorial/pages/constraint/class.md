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

```tut:book:silent
import cilib._
import scalaz._
import Scalaz._
val sumCF = ConstraintFunction((l: NonEmptyList[Double]) => l.suml)
```
```tut:book
sumCF(NonEmptyList(2.0, 4.0, 7.5))
```

### Constraint Example

```tut:book
LessThan(sumCF, 12.0)
```

We have successfully defined our constraint context.
Hold on to this because we are going to put it into action in the next section.