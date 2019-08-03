## StepS Class

`final case class StepS[A,S,B](run: StateT[Step[A,?],S,B])`

As you can see the class header bares some resemblance to that of
`Step`. Here, however, the parameter `run` is a state transformer,
`StateT`. `StateT` is defined in `scalaz`. The other similarities
are that `StepS` also has `map` and `flatmap` methods.

```scala
def map[C](f: B => C): StepS[A,S,C]

def flatMap[C](f: B => StepS[A,S,C]): StepS[A,S,C]
```

But what also makes `StepS` unique is its `zoom` method.

```scala
def zoom[S2](l: monocle.Lens[S2,S]): StepS[A,S2,B]
```

The `zoom` method uses a type of lense from the `monocle` library,
called `Iso`. Information about `iso` can be found over here at this
[link][iso-link]. We will see how all of these new data types come as
we start by creating our first `StepS`!

### Our First StepS

Imagine we had the following situation. We wanted to update a
position by adjusting each point by some factor, as well as producing
a new factor. We would end up with some return type of
`(Position[Double], Double)`. All while keeping the same *chaining*
for comprehension ability of of `Step`. Tada! `StepS` to save the
day. In this situation, our factor will be our state. First, the
basics.

```tut:book:invisible
import cilib._
import scalaz._
import Scalaz._
import spire.implicits._
import spire.math.Interval
```
```tut:book:silent
val bounds = Interval(-5.12,5.12)^2

val env = Environment(
    cmp = Comparison.dominance(Min),
    eval = Eval.unconstrained[NonEmptyList,Double](_.map(x => x *
        x).suml).eval
)
val rng = RNG.init(12)
```
```tut:book
// Our Position
val position = Position.createPosition(bounds).eval(rng)
```

And then here is our function that we will be using to get an updated
`Position` and state.

```tut:book
def explore (position: Position[Double], factor: Double): (Position[Double], Double) =
    (position.map(x => x * factor), 0.73 * factor)
```

Now, putting it all together to make a `StepS`. Pay close attention
to the resulting types.

```tut:book
val myStepS = StepS(StateT[Step[Double, ?], Position[Double], Double](x => Step.point(explore(x, 0.96))))
val step = myStepS.run(position) // Supply an initial value
val rvar = step.run(env)
val result = rvar.eval(rng)
```

Now that we have a `StepS` at our disposal let's start looking at its
class methods.

### zoom

`zoom` allows us to plugin in a lens for our `StepS`. In this case,
using `zoom` with a `_position` lense we are able to pass an `Entity`.
This creates a `StepS` that offers the same functionality as our
original but allows us to pass in a different data type.

```tut:book
val particle = Position.createPosition(bounds).map(p => Entity((), p)).eval(rng)
myStepS.zoom(Lenses._position[Unit, Double]).run(particle).run(env).eval(rng)
```

### map

Using the `map` method we are able to modify the state value.

```tut:book
myStepS.map(x => 4.0 * x).run(position).run(env).eval(rng)
```

### flatMap

Similarly, using the `flatMap` method we are able to modify the state
as well as the value at hand by chaining together `StepS`s.

```tut:book:silent
def negate (position: Position[Double]): (Position[Double], Double) = (position.map(x => x * -1), -1.0)
val myStepS2 = StepS(StateT[Step[Double, ?], Position[Double], Double](x => Step.point(negate(x))))
```
```tut:book:silent
myStepS.flatMap(x => myStepS2).run(position).run(env).eval(rng)
```

Although a simple example with having our state has a `Double`, we can
begin to see its usefulness. Especially when we begin to use it in
for comprehensions to chain multiple `StepSs` together. The last
focus of this chapter will be exploring the companion object.
