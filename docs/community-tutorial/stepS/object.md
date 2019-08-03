## StepS Companion Object

The companion object offers us several methods for us to use, which we
will explore shortly.

```scala
lensIso[A,B]

apply[A,S,B](f: S => Step[A,(S, B)]): StepS[A,S,B]

pointR[A,S,B](a: RVar[B]): StepS[A,S,B]

pointS[A,S,B](a: Step[A,B]): StepS[A,S,B]

liftK[A,S,B](a: Comparison => B): StepS[A,S,B]

liftS[A,S,B](a: State[S, B]): StepS[A,S,B]
```

Not only does it offer us `StepS` creation methods, there are two
implicits that you should be mindful about.

```scala
implicit def stepSMonad[A,S]: Monad[StepS[A,S,?]]

implicit def stepSMonadState[A,S]: MonadState[StepS[A,S,?], S]
```

### lensIso

This method allows us to transform a `scalaz` lense into a `monocle`
lenses that we may use.

```tut:book:invisible
import cilib._
import scalaz._
import Scalaz._
import eu.timepit.refined.auto._
import spire.implicits.{eu => _, _}
import spire.math.Interval
```
```tut:book
StepS.lensIso.get(scalaz.Lens.firstLens[Unit, Double])
```

### apply

`apply` we have seen before in the previous section, specifically "Our
First StepS" where you can find a thorough example.

### pointR

Creating a `StepS` based on an `RVar` computation. It is important to
remember that the initial value for `run` is a state value, the second
type parameter. In this case `Double`.

```tut:book:invisible
val bounds = Interval(-5.12,5.12)^2

val env = Environment(
    cmp = Comparison.dominance(Min),
    eval = Eval.unconstrained[NonEmptyList,Double](_.map(x => x *
        x).suml).eval
)
```
```tut:book
StepS.pointR[Double, Double, NonEmptyList[Entity[Unit,Double]]](Position.createCollection(x => Entity((), x))(bounds, 3))
```

### pointS

Creating a `StepS` based on an `Step` computation.

### liftK

`liftK` when provided with a comparison will yield `StepS` based on a
`Step.withCompare`.

### liftS

`liftS` when provided with a `State` will yield `StepS` with a state
type of `State`.
