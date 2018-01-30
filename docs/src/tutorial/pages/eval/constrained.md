## Constrained

<div class="callout callout-info">
An unconstrained evaluation applies a function that takes a monad and produces a double in a contained environment.
The feasibility of every solution is determined by the constraints.
</div>

```tut:book:invisible
import cilib._ 
import scalaz._
import Scalaz._
```
```tut:book:silent
val lesThanCons = LessThan(ConstraintFunction((l: NonEmptyList[Double]) => l.suml), 12.0)
```
```tut:book
val e = Eval.constrained[NonEmptyList,Double](_.map(x => x * x).suml, List(lesThanCons))
```
### eval

Performs the same function as an unconstrained.
However, our result may either be feasible or infeasible.
If it is infeasible, you will notice that the result contains a violation count as well a `List` of the violated `Constraints`.

```tut:book:silent
var l = NonEmptyList(20.0, 4.0, 5.0)
```
```tut:book
e.eval.run(RNG.fromTime)._2.apply(l)
```
```tut:book:silent
l = NonEmptyList(1.0, 4.0, 5.0)
```
```tut:book
e.eval.run(RNG.fromTime)._2.apply(l)
```

### constrainBy

Will return a new instance of a constrained `Eval` based on the parameter provided and the existing `Eval's` *evaluation* function.
Note that the existing `Constraints` are not carried over, they are replaced by the parameter.

### unconstrain

Will return a new instance of a unconstrained `Eval` based on the existing `Eval's` *evaluation* function.