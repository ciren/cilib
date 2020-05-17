---
id: step
title: Step
---

Content currently under development

<!--
What is this mysterious data type called `Step`? Well, it actually
represents a *step* in an algorithm. Nothing mysterious at all. How
does this happen? `Step` is a monad transformer and because monad
transformers are monads themselves, we can freely compose different
Step instances to create a larger computation.

In the last chapter we learnt how to create `Entities`, but what about
evaluating them? This wil be our first introduction to `Step`.

```scala :invisible
import cilib._
import spire.implicits._
import spire.math._
import scalaz._
import Scalaz._
```
```scala :silent
val rng = RNG.init(12)
val bounds = Interval(-5.12,5.12)^2

val env = Environment(
        cmp = Comparison.dominance(Min),
        eval = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
    )

val particle = Position.createPosition(bounds).map(p => Entity(Mem(p, p.zeroed), p)).eval(rng)
def explore (position: Position[Double]): Position[Double] = position.map(x => x * 0.73)
```
```scala
val myStep = Step.eval(explore)(particle)
```

This `Step` represents a step in some algorithm where the particle's
position is updated and evaluated to return a new `Entity`.

Understanding the `Step` concept is important as we will begin to
start making more complex *steps* that which may be used in an
algorithm.


## Environment

An environment is simply a *container* for the specifications of our
problem. And as we can see from the class definition, it uses types
we are very familiar with.

```
final case class Environment[A](
    cmp: Comparison,
    eval: RVar[NonEmptyList[A] => Objective[A]])
```

An example of creating an `Environment` would be the following...

```scala :invisible
import cilib._
import scalaz._
import Scalaz._
import spire.math.Interval
import spire.implicits.{eu => _, _}

```
```scala
val env = Environment(
    cmp = Comparison.dominance(Min),
    eval =  Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
)
```


## Step Class

`Step` is has a very simple class definition. It takes a single
parameter, that being a function of type `Environment[A] => RVar[B]`.
We see this member function used we we call `run` of a `Step`.

```
final case class Step[A,B] private (run: Environment[A] => RVar[B])
```

Not complicated at all. And because of its monadic nature we have
the following functions at our disposal.

```scala
map[C](f: B => C): Step[A,C]

flatMap[C](f: B => Step[A,C]): Step[A,C]
```

We will be using the `Step` we created at the beginning of this
chapter, `myStep`.

### map

Here we changing the context of the `Step`.

```scala :invisible
import cilib._
import spire.implicits._
import spire.math._
import scalaz._
import Scalaz._

val bounds = Interval(-5.12,5.12)^2

val env = Environment(
    cmp = Comparison.dominance(Min),
    eval =  Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
)

val rng = RNG.init(12)
val particle = Position.createPosition(bounds).map(p => Entity(Mem(p, p.zeroed), p)).eval(rng)
def explore (position: Position[Double]): Position[Double] = position.map(x => x * 0.73)
val myStep = Step.eval(explore)(particle)
```
```scala
myStep.map(entity => Lenses._position.get(entity)).run(env).eval(rng)
```

In this example we changed it from

- a step in where the particle's position is updated and evaluated to
  return a new `Entity`
- to a step in where the particle's position is updated and evaluated
  to return the resulting `Position`.

### flatMap

Here we changing the context. In this example, We are passing an
`Entity[A] => Step[A, C]`, thus producing a new `Step[A, C]` which
will differ from our original `Step[A, B]`. What will happen here is
that we adding an extra *step*.


```scala :silent
val rng = RNG.init(12)
def explore (position: Position[Double]): Position[Double] = position.map(x => x * 0.73)
val particle = Position.createPosition(bounds).map(p => Entity(Mem(p, p.zeroed), p)).eval(rng)
val myStep = Step.eval(explore)(particle)

def negate (position: Position[Double]): Position[Double] = position.map(x => x * -1)
```
```scala
myStep.flatMap(entity => Step.eval(negate)(particle)).run(env).eval(rng)
```

So now our `Step` represents a step in an algorithm where a position
is multiplied by 0.73 and then negated. This step might not serve any
real world purpose but it demonstrates how we may chain `Steps`
together to form an algorithm. This is easily achieved by using for
comprehensions. For example, take a look at the following method.

```scala
def algorithm(entity: Entity[Mem[Double], Double]) = (for {
    step1 <- Step.eval(explore)(entity)
    step2 <- Step.eval(negate)(entity)
} yield step2).map(entity => Lenses._position.get(entity))

algorithm(particle).run(env).eval(rng)
```


## Step Companion Object

The companion object offers several methods that we may use to create
instances of `Steps`.

```scala
point[A,B](b: B): Step[A,B]

pointR[A,B](a: RVar[B]): Step[A,B]

eval[S,A:Numeric](f: Position[A] => Position[A])(entity: Entity[S,A]): Step[A,Entity S,A]

evalP[A](pos: Position[A]): Step[A,Position[A]]

withCompare[A,B](a: Comparison => B): Step[A,B]

withCompareR[A,B](f: Comparison => RVar[B]): Step[A,B]

evalF[A:Numeric](pos: Position[A]): Step[A,Position[A]]
```

We will be using the `Environment` and `RNG` we created at the
beginning of this chapter.

```
val rng = RNG.init(12)
val bounds = Interval(-5.12,5.12)^2
val env = Environment(
    cmp = Comparison.dominance(Min),
    eval = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
)
```

### point

Returns an instance of `Step` based on the given parameter.

```scala :invisible
import cilib._
import spire.implicits._
import spire.math._
import scalaz._
import Scalaz._
val rng = RNG.init(12)
val bounds = Interval(-5.12,5.12)^2
val env = Environment(
    cmp = Comparison.dominance(Min),
    eval = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
)
```
```scala :silent
val particle = Position.createPosition(bounds).map(p => Entity(Mem(p, p.zeroed), p)).eval(rng)
```
```scala
Step.point(particle)
```

### pointR

Creates `Step` contained in `RVar`.

```scala :silent
val particle = Position.createPosition(bounds).map(p => Entity(Mem(p, p.zeroed), p))
```
```scala
Step.pointR(particle).run(env).eval(rng)
```

### eval

`eval` is used for evaluating `Entities`. This function produces a
`Step` which may be `run` using a function, `Environment => RVar[A]`.

```scala
val particle = Position.createPosition(bounds).map(p => Entity(Mem(p, p.zeroed), p)).eval(rng)
def explore (position: Position[Double]): Position[Double] = position.map(x => x * 0.73)

Step.eval(explore)(particle)
```

```scala :silent
Step.eval(explore)(particle).run(env).run(rng)
```

As you can see from the above code, when our `Entity` was evaluated
it's `Point` changed to a `Solution`. However its `state` remained
the same as that is up to us as to how we update it.

### withCompare

An example of use would be comparing the current position of an
`Entity` with its best, and then returning a new `Entity` based on
the comparison.

```scala :silent
import monocle._, Monocle._

val particle = Position.createPosition(bounds).map(p => Entity(Mem(p, p.zeroed), p)).eval(rng)

def updatePBest[S](p: Entity[S,Double])(implicit M: HasMemory[S,Double]): Step[Double, Entity[S,Double]] = {
    val pbestL = M._memory
    Step.withCompare(Comparison.compare(p.pos, (p.state applyLens pbestL).get)).map(x =>
        Entity(p.state applyLens pbestL set x, p.pos))
}
```

```scala
updatePBest(particle).run(env).eval(rng)
```

### withCompareR

An example of use would be determining which un-evaluated
`RVar[Entity]s` is fitter.

```scala :silent
def better[S,A](a: Entity[S,A], b: Entity[S,A]): Step[A,Boolean] =
    Step.withCompareR(comp => RVar.point(Comparison.fitter(a.pos, b.pos).apply(comp)))

val particle1 = Position.createPosition(bounds).map(p => Entity(Mem(p, p.zeroed), p)).eval(rng)
val particle2 = Position.createPosition(bounds).map(p => Entity(Mem(p, p.zeroed), p)).eval(RNG.fromTime)
```

```scala
Betterk(particle1, particle2).run(env).eval(rng)
```

### evalF

`evalF` will take a `Position` an returns a `Step` that represents the
evaluation of the position. This is actually used in the `Step.eval`
method.


## Summary

Hey, that wasn't as scary as we thought it would be.

:::note
`Step` is nothing more than a data structure that hides the details of a
  monad transformer which represents the algorithmic parts which my be applied to a given problem `Environment`.
:::

We also learnt some valuable skills in this chapter such as:

:::note
- We can use for comprehensions to chain together steps of an algorithm.
- How to can compare two `Entities` in a step.
:::

But what if I told you `Step` has a sibling?
*gasp*.
In the next chapter we are going to look at `StepS` which represents a step with a state.
-->
