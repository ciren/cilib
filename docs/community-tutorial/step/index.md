# Step

What is this mysterious data type called `Step`? Well, it actually
represents a *step* in an algorithm. Nothing mysterious at all. How
does this happen? `Step` is a monad transformer and because monad
transformers are monads themselves, we can freely compose different
Step instances to create a larger computation.

In the last chapter we learnt how to create `Entities`, but what about
evaluating them? This wil be our first introduction to `Step`.

```tut:book:invisible
import cilib._
import spire.implicits._
import spire.math._
import scalaz._
import Scalaz._
```
```tut:book:silent
val rng = RNG.init(12)
val bounds = Interval(-5.12,5.12)^2

val env = Environment(
        cmp = Comparison.dominance(Min),
        eval = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
    )

val particle = Position.createPosition(bounds).map(p => Entity(Mem(p, p.zeroed), p)).eval(rng)
def explore (position: Position[Double]): Position[Double] = position.map(x => x * 0.73)
```
```tut:book
val myStep = Step.eval(explore)(particle)
```

This `Step` represents a step in some algorithm where the particle's
position is updated and evaluated to return a new `Entity`.

Understanding the `Step` concept is important as we will begin to
start making more complex *steps* that which may be used in an
algorithm.
