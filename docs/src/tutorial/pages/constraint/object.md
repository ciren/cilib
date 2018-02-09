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

```tut:book:invisible
import cilib._
import scalaz._
import Scalaz._
val sumCF = ConstraintFunction((l: NonEmptyList[Double]) => l.suml)
```
```tut:book:silent
import spire.implicits._

val sumLessThanCons = LessThan(sumCF, 12.0)
```
```tut:book
Constraint.satisfies(sumLessThanCons, NonEmptyList(2.0, 3.0, 4.0))
Constraint.satisfies(sumLessThanCons, NonEmptyList(9.0, 10.0, 11.0))
```

### violationCount

A `NonEmptyList` will be checked against a `List` of `Constraints`.
An `Int` is returned representing the number of `Constraints` the list violated.

```tut:book:silent
import spire.implicits._
import spire.algebra.Eq

val sumLessThanCons = LessThan(sumCF, 12.0)
val firstNumberCons = cilib.Equal(ConstraintFunction((l: NonEmptyList[Double]) => l.head), 4.0) 
// A constraint that ensures the first element of a list is 4
```
```tut:book
Constraint.violationCount(List(sumLessThanCons, firstNumberCons), NonEmptyList(19.0, 37.23, 12.0))
Constraint.violationCount(List(sumLessThanCons, firstNumberCons), NonEmptyList(4.0, 3.0, 2.0))
Constraint.violationCount(List(sumLessThanCons, firstNumberCons), NonEmptyList(4.0, 3.0, 2.0)).count
```

### violationMagnitude

Determines the magnitude of the number of violated constraints.

```tut:book
Constraint.violationMagnitude(0.1, 0.9, List(sumLessThanCons, firstNumberCons), NonEmptyList(19.0, 37.23, 12.0))
```