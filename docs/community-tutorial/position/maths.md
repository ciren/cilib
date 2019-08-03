## Maths With Positions

`Positions` also may be used with normal vector operations.
This allows for simpler usage as it mirrors the mathematics defined in literature more closely.

```tut:book:invisible
import cilib._
import spire.implicits._
import spire.math._
import scalaz._
import Scalaz._
```

```tut:book:silent
val rng = RNG.init(1234L)
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x*x).suml).eval
val a = Position.createPosition(Interval(-5.12,5.12)^3).eval(rng)
val b = Position.createPosition(Interval(-5.12,5.12)^3).flatMap(p => Position.eval(e, p)).eval(rng)
```

```tut:book
a + b // Add Point and Solution
a + a // Add Point and Point
b + b // Add Solution and Solution
a - b // Subtract Solution from Point
```
