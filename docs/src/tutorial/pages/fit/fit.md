## The Fit Type

In the past we have seen `Feasible` and `Infeasible`.
But, you might have noticed, there is a third type, `Adjusted`.
These types used to indicate information about a possible solution.

```scala
Feasible(v: Double)

Infeasible(v: Double, violations: Int)

Adjusted (original: Infeasible, adjust: Double)
```

And the purpose of this is to allow for pattern matching so that we may control the flow of logic.
For example

```tut:book:invisible
import cilib._ 
```
```tut:book:silent
def control (fit: Fit) : Double = {
    fit match {
        case Feasible(v) => v
        case Infeasible(_, _) => -1.0
        case Adjusted (_, _) => -1.0
    }
}
```

### Feasible

<div class="callout callout-info">
A data type to represent a feasible solution.
</div>

```tut:book
val f = Feasible.apply(5)
Feasible.unapply(f)

```

### Infeasible and Adjusted

<div class="callout callout-info">
Infeasible is a data type to represent an infeasible solution.
</div>

From the `Infeasible` constructor we know that it requires a `Double` for our solution, and an `Int`
to show the amount of `Constraints` violated.
`Infeasible` offers an `adjust` method, `adjust(f: Double => Double): Adjusted`, which will create
adjust the solution using the given parameter function.

<div class="callout callout-info">
Adjusted indactes we have adjusted an `Infeasible` solution. It contains the `Infeasible` we had to adjust and the new adjusted value.
</div>

```tut:book:invisible
import scalaz._
```
```tut:book
val badSolution = Infeasible(45.0, 1)
badSolution.adjust(x => x * 0.73)
```

With `Adjust` we are given a second chance at correcting an incorrect solution.