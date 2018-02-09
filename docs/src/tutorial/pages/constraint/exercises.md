## Exercises

### Question 1
Define a `Constraint` to ensure that the head of a list is between 5 and 10.

<div class="solution">
```tut:book:invisible
import cilib._
import scalaz._
import Scalaz._ 
import spire.implicits._
```
```tut:book:silent
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
```tut:book:silent
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