## Unconstrained

<div class="callout callout-info">
An unconstrained evaluation applies a function that takes a monad and produces a double in a contained environment.
Every solution is feasible since there are not constraints.
</div>

```tut:book:invisible
import cilib._ 
import scalaz._
import Scalaz._
```
```tut:book
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml)
```

Here, we are creating an unconstrained evaluation instance that produces the sum of a non-empty list where each element is squared.
With our newly created instance we can use the following methods.

### eval

Will return a function that produces an `Objective`, either `Single` or `Multi` with a `Feasible` solution as there is no constraints.
This function is wrapped in an `RVar`. So in oder to *extract* the function from within the `RVar` we can use `run`.
Finally, we may use our function with other given list.

```tut:book:silent
val l = NonEmptyList(20.0, 4.0, 5.0)
```
```tut:book
e.eval
e.eval.run(RNG.fromTime)
e.eval.run(RNG.fromTime)._2
e.eval.run(RNG.fromTime)._2.apply(l)
```

### constrainBy

Will return a new instance of a constrained `Eval` based on the parameter provided and the existing `Eval's` *evaluation* function.

### unconstrain

This will return itself.