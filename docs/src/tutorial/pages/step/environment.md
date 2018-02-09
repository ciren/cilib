## Environment

An environment is simply a *container* for the specifications of our problem.
And as we can see from the class definition, it uses types we are very familiar with. 

``` 
final case class Environment[A](
    cmp: Comparison,
    eval: RVar[NonEmptyList[A] => Objective[A]],
    bounds: NonEmptyList[spire.math.Interval[Double]])
```

An example of creating an `Environment` would be the following...

```tut:book:invisible
import cilib._
import scalaz._
import Scalaz._
import spire.math.Interval
import spire.implicits.{eu => _, _}

```
```tut:book
val env = Environment(
    cmp = Comparison.dominance(Min),
    eval =  Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval,
    bounds = Interval(-5.12,5.12)^2
)
```