## How Would We Use Eval?

Okay, so we know what `Eval` *is*, but what can we do with it?
Say there exists a problem called problem X.
It requires 4 numbers.
Where all numbers need to be in the interval of 0 and 50. 
However, there are some constraints. 

1. The third number needs to be greater than 20.
2. Our fitness function can't yield a result greater than 5.

Our fitness function will add the first three numbers, and divide the result by the fourth.
Pretty simple. So far we have..

```tut:book:invisible
import cilib._ 
import scalaz._
import Scalaz._
```
```tut:book:silent
def fitness (values: NonEmptyList[Double]) : Double = {
    var result = values.index(0).getOrElse(0.0)
    result += values.index(1).getOrElse(0.0)
    result += values.index(2).getOrElse(0.0)
    result /= values.index(3).getOrElse(0.0)
    result
}
val cons1 = GreaterThan(ConstraintFunction((l: NonEmptyList[Double]) => l.index(3).getOrElse(0.0)), 20.0)
val cons2 = LessThanEqual(ConstraintFunction((l: NonEmptyList[Double]) => fitness(l)), 5.0)

val e = Eval.constrained[NonEmptyList,Double](fitness(_), List( cons1, cons2))
```

Cool. We know that when we use an `Eval` it will return an `Objective`, *the objective of a function*.
Since we know the `Objective` is going to be `Single` we should focus our attention there.
A `Single` will contain a `Fit` type, either `Feasible` or `Infeasible`, as well as a `List` of
any `Constraints` that were violated.

Because of all the class types being used we are able to use pattern matching in order to control our
program logic.

```tut:book:silent
def getSolution (fit: Fit) : Double = {
    fit match {
        case Feasible(v) => v
        case Infeasible(_, _) => -1.0
        case Adjusted (_, _) => -1.0
    }
}

def evalObjective (objective: Objective[Double]) : Double = {
    objective match {
        case Single(f, _) => getSolution(f)
        case Multi(_) => -1.0
    }
}
```

As you can see, we are using -1.0 to indicate an infeasible solution. 

Now we can put everything together and test our code.

```tut:book:invisible
import spire.implicits._
import spire.math.Interval
```
```tut:book:silent
val rng = RNG.init(12L)
val interval = Interval(0.0, 50.0)
val l = Dist.uniform(interval).replicateM(4).eval(rng).toNel.getOrElse(NonEmptyList(0.0))
```
```tut:book
evalObjective(e.eval.run(rng)._2(l))
```
`.toNel.getOrElse(NonEmptyList(0.0))` is converting the returned `List[Double]` from our `RVar` evaluation to `NonEmptyList[Double]`.