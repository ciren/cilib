## Creating a GA

Isn't this exciting!
All our work over the past chapters is coming together!
It should be noted that the only thing we will be using from `cilib-ga` is the type `Individual`, which, as we discussed before, is an `Enity` with a state of type `Unit` since GA's do not need a state.

### Imports

We will be using the following `imports` for our GA.

```tut:book:silent
import cilib._
import cilib.ga._
import scalaz._
import Scalaz._
import eu.timepit.refined.auto._
import spire.implicits._
import spire.math.Interval
```

### The Problem Environment

For our problem we are going to attempt to find the greatest area produced by a 2 dimensional rectangle. 
The sides can range from 0.1 to 12.

```tut:book:invisible
import cilib._
import scalaz._
import Scalaz._
import spire.math.Interval
import spire.implicits.{eu => _, _}

```
```tut:book
val env = Environment(
    cmp = Comparison.dominance(Max),
    eval =  Eval.unconstrained[NonEmptyList,Double](_.foldLeft1(_ * _)).eval,
    bounds = Interval(0.1, 12.0)^2
)
```

### The GA Algorithm

The GA algorithm is the exact same one used in `cilib-ga`.
The reason we are intentionally defining it as opposed to importing it is so that we can see the inner workings and learn from it.

```tut:book:invisible
import cilib._
import cilib.ga._
import scalaz._
import Scalaz._
import spire.implicits._

```
```tut:book
def ga[S](
             p_c: Double,
             parentSelection: NonEmptyList[Individual[S]] => RVar[List[Individual[S]]], 
             crossover: List[Individual[S]] => RVar[List[Individual[S]]],
             mutation: List[Individual[S]] => RVar[List[Individual[S]]]
         ): NonEmptyList[Individual[S]] => Individual[S] => Step[Double,List[Individual[S]]] =
    collection => x => for {
        parents   <- Step.pointR(parentSelection(collection))
        r         <- Step.pointR(Dist.stdUniform.map(_ < p_c))
        crossed   <- if (r) Step.pointR[Double,List[Individual[S]]](crossover(parents))
        else Step.point[Double,List[Individual[S]]](parents)
        mutated   <- Step.pointR[Double,List[Individual[S]]](mutation(crossed))
        evaluated <- mutated.traverseU(x => Step.eval((v: Position[Double]) => v)(x))
    } yield evaluated
```