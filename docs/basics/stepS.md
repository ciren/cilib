---
id: step-with-state
title: StepS
---

Content currently under development

<!--
At this point, we now understand what `Step` is.
`StepS` is very similar but now we are creating a step with a state.

What is a state?
Lets first at look functions to answer this.
A *state-ful* computation is something that takes some state and returns a value along with some new state.
In a way we could look at `RVar` as state-ful computation as we supply at state, the `RNG`, and when it is run a value is returned along with a new state.

```
s -> (a, s)
```

Thus, `StepS` represents a `Step` with a step.
Now since we haven't come across any `StepS` instances before this, we will first look at what a `StepS` is made up off.


## StepS Class

`final case class StepS[A,S,B](run: StateT[Step[A,*],S,B])`

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

```scala
import cilib._
import scalaz._
import Scalaz._
import spire.implicits._
import spire.math.Interval
import _root_.eu.timepit.refined.auto._
```
```scala
val bounds = Interval(-5.12,5.12)^2

val env = Environment(
    cmp = Comparison.dominance(Min),
    eval = Eval.unconstrained[NonEmptyList,Double](p => Feasible(_.map(x => x * x).suml)).eval
)
val rng = RNG.init(12)
```
```scala
// Our Position
val position = Position.createPosition(bounds).eval(rng)
```

And then here is our function that we will be using to get an updated
`Position` and state.

```scala
def explore (position: Position[Double], factor: Double): (Position[Double], Double) =
    (position.map(x => x * factor), 0.73 * factor)
```

Now, putting it all together to make a `StepS`. Pay close attention
to the resulting types.

```scala
val myStepS = StepS(StateT[Step[Double, *], Position[Double], Double](x => Step.point(explore(x, 0.96))))
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

```scala
val particle = Position.createPosition(bounds).map(p => Entity((), p)).eval(rng)
myStepS.zoom(Lenses._position[Unit, Double]).run(particle).run(env).eval(rng)
```

### map

Using the `map` method we are able to modify the state value.

```scala
myStepS.map(x => 4.0 * x).run(position).run(env).eval(rng)
```

### flatMap

Similarly, using the `flatMap` method we are able to modify the state
as well as the value at hand by chaining together `StepS`s.

```scala :silent
def negate (position: Position[Double]): (Position[Double], Double) = (position.map(x => x * -1), -1.0)
val myStepS2 = StepS(StateT[Step[Double, *], Position[Double], Double](x => Step.point(negate(x))))
```
```scala :silent
myStepS.flatMap(x => myStepS2).run(position).run(env).eval(rng)
```

Although a simple example with having our state has a `Double`, we can
begin to see its usefulness. Especially when we begin to use it in
for comprehensions to chain multiple `StepSs` together. The last
focus of this chapter will be exploring the companion object.


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
implicit def stepSMonad[A,S]: Monad[StepS[A,S,*]]

implicit def stepSMonadState[A,S]: MonadState[StepS[A,S,*], S]
```

### lensIso

This method allows us to transform a `scalaz` lense into a `monocle`
lenses that we may use.

```scala
StepS.lensIso.get(scalaz.Lens.firstLens[Unit, Double])
```

### apply

`apply` we have seen before in the previous section, specifically "Our
First StepS" where you can find a thorough example.

### pointR

Creating a `StepS` based on an `RVar` computation. It is important to
remember that the initial value for `run` is a state value, the second
type parameter. In this case `Double`.

```scala
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


## Summary

Again. Not that scary.
It really depends on what you decide to use as your state.

<div class="callout callout-info">
A *state-ful* computation is something that takes some state and returns a value along with some new state.
`StepS` represents a `Step` with a step.
When we `run` a `StepS` we need to supply an initial state value.
The type of the initial state value is determined by the second type parameter of the `StepS`.
</div>

In the next chapter we will gain some practical experience with `Step` as well as a bit of `StepS`
-->
