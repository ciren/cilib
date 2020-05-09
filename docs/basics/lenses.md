---
id: lenses
title: Lenses
---

<!--
Great! You just learnt about creating positions in a search space.
But you might be asking yourself "How can we modify our set positions to explore the search space?"
And this is exactly what we intend to learn in the coming chapter.
Well, partly.
This chapter is going to focus on `Lenses`, which are super awesome getters, setters and more!
CILib makes use of the two optics from the [`monocle`][Monocle-link] library.

- [`Lens`][lens-link]
- [`Prism`][prism-link]

I deeply encourage you to check out [`monocle`][Monocle-link] if you are not familiar with any of the concepts I just mentioned.
The use of these optics is beneficial as they allow us to create a new *mutated* instance while still preserving the original.
Or they may shed light on an instance by *zooming* in and getting/returning data.

<div class="callout callout-danger">

Lenses provide an API that is first and foremost, composition and lawful.
This means that the various optics are well behaved and rules exist that
govern their usage. Furthermore, different optics may be composed together
to create new optics that are the combination of the original optics. This
is obviously only possible if the provided types correctly line up.

Building on the usage of optics in general, we use a mechanism known as
"classy lens" in Haskell. This mechanism prevents invalid usage, by letting
the compiler fail based on the types being used. In the case of ``Entity``,
the compiler would look up instances, using its implicit resolution rules,
to obtain evidence for a typeclass with a given set of types, at compile time.

This provides an additional level of surety, that the data being passed to
a function that requires evidence in order to extract some other piece of
information for a given type. The scala compiler provides the evidence
through the use of its implicit lookup mechanics.

</div>

Now that you understand the motivation for `Lenses` we can start to look at what CILib offers us.
If you are still a bit unclear about optics then hopefully the following sections will clear that up as we go through some examples.
Just one last thing.
You might have picked up that [Gary][link-gary] made reference to something called `Entity`.
Fear not as this will be explained more in detail soon in this chapter and the next chapter, which is all about `Entity`.


## Immutable data

To understand a bit more of the motivation behind lenses here is an excerpt from the [CILib docs][cilib-docs].

<div class="callout callout-danger">

Immutable data is fantastic because it allows anyone to read the contents
of the data and provides the security that the data will not change. In some
cases, however, we would like to "change" some data value. Using immutable
data means that we cannot change the value within a structure, instead we
need to create an updated view of the data with the changes applied. This
updated view creates new data, where the old data is still present and
unchanged. It's recommended that the reader become familiar with
persistent data structures and how they operate. Due to how persistent
data structures update, by only changing the smallest number of references,
the needed speed and efficiency is achieved.

Scala tries to help with respect to immutable data, by providing a convenience
method on all `case class`es called `copy`. In situations where there is a
nesting of case classes, potentially several levels, the updating of a value
on the lower levels results in a bubbling-up process whereby each previous
layer needs to update the reference to the new data in the lower layer.
Although this is not difficult to do, the result is very verbose and
extremely cumbersome for the user. It would be nice if this "zooming"
update process was abstracted behind a data structure that would hide and
automate the tedious process.

</div>


## Mem

`Mem` is a data type that represents a particle's memory.
It contains only two pieces information, both which are valuable.
This information being:

- A particle's best `Position`
- A particle's velocity in the form of a `Position`

The `Mem` class is represented with the following definition/constructor:

- `Mem[A](b: Position[A], v: Position[A])`

`Mem` ins't a complex data type at all but it is very important to CILib.
We will now see why.

## A Quick Look at Entity

An instance of `Entity` represents a particle with in a search space.
It has the following constructor:

- `Entity[S,A](state: S, pos: Position[A])`

The `state` member value represents the state of a particle.
This in our case is `Mem`, but it would also be possible for you to define your own state if you choose to.
And `pos` a `Position` type to represent the particle's current position.

Now, if you are familiar with the optics laws form [`monocle`][Monocle-link] you might have already picked up on how `Entities` and `Lenses` relate.
Since `Entity` is a product of other classes we would want to be able to *zoom* in on those data types.
For example, if we wanted to get a particle's best position we would have to *zoom* in on its `Mem` value.
This is where `Lenses` come in.

It also might be beneficial to view `Entity` as a collection of information for a particle. It contains a particle's:

- Best position
- Velocity
- Current position

## Classy Lenses

<div class="callout callout-danger">

Based on the normally accepted usage of "classy lenses", the type classes
that expose the lenses are generally prefixed with `Has`.

</div>

So CILib has some classes that expose the lense for us to use.
The reason for this is so that we can define our own functions with the lense.
These *classy lenses* are passed as implicit parameters.
We may make muse of:

- HasMemory
- HasVelocity

The following code is used to create an `Entity` which we will use to explore the class lenses.
Take note of the evaluated `Entity` and its contents.

```scala :invisible
import cilib._
import spire.implicits.{eu => _, _}
import spire.math._
```
```scala :silent

val interval = Interval(-5.12,5.12)^3

val particle = Position.createPosition(interval).map(p => Entity(Mem(p, p.zeroed), p))

val rng = RNG.init(12)
```
```scala
particle.eval(rng)
```

### HasMemory

`HasMemory` contains a `Lens` method that is exposed for use.
By importing CILib we are including the implicits that are defined `HasMemory`.
This allows us to use it in a function were given an `Entity` we may retrieve its best `Position`.

```scala :silent
def foo[S](x: Entity[S,Double])(implicit mem: HasMemory[S,Double]) = mem._memory.get(x.state)
```
```scala
particle.map(p => foo(p)).eval(rng)
```
`_memory` is the `Lens` we are using.

### HasVelocity

`HasVelocity` is works the exact same as `HasMemory`.
The only difference is that it returns the velocity of an `Entity's` state.

```scala :silent
def foo[S](x: Entity[S,Double])(implicit mem: HasVelocity[S,Double]) = mem._velocity.get(x.state)
```
```scala
particle.map(p => foo(p)).eval(rng)
```

### HasCharge and HasPBestStagnation

**Todo**

### Conclusion

Hopefully the `HasMemory` and `HasVelocity` examples cleared up any confusion you may have had about optics.
We created an `Entity`, called `particle`, and we saw its contents when we evaluated it.
We then used a `Lens` to retrieve a part of the contents.
We could have even used set, or any other method defined in the `Lens` type, to return a new modified result.


## Lenses Object

CILib offers a `Lense` object that contains a few optics that we can readily use.
These optics are not just for `Entities` but also some CILib types we have seen before (yaaay!)
With these optics, we are condensing some of the functions we created earlier in to a single line while at the same time adding more functionality (double yaaay!)

All optics within the `Lense` object are prefixed with an underscore to signify that they are indeed an optic.

```scala
_state[S,A]

_position[S,A]

_vector[A:scalaz.Equal]

_solutionPrism[A]: Prism[Position[A],Solution[A]]

_objectiveLens[A]: Lens[Solution[A],Objective[A]]

_singleObjective[A]: Prism[Objective[A],Single[A]]

_multiObjective[A]: Prism[Objective[A],Multi[A]]

_singleFit[A]: Lens[Single[A],Fit]

_singleFitness[A]: Optional[Position[A], Fit]

_feasible: Prism[Fit,Double]
```

### _state

Will provide a `Lens` that we may use to *zoom* in on the state of an `Entity`.

```scala :invisible
import cilib.{Lenses, _}
import spire.implicits.{eu => _, _}

import scalaz._
import Scalaz._
import spire.math._
```
```scala :silent
val rng = RNG.init(12)
val interval = Interval(-5.12,5.12)^3
val particle = Position.createPosition(interval).map(p => Entity(Mem(p, p.zeroed), p))
```
```scala
val p = particle.eval(rng)
Lenses._state.get(p)
```

### _position

Will provide a `Lens` that we may use to *zoom* in on the position of an `Entity`.

```scala
Lenses._position.get(p)
```

### _vector

Returns the actual position within a `Position` instance.
As of now you have to declare the type being used in the lense.

```scala :silent
val x = cilib.Point[Int](NonEmptyList(2, 4), NonEmptyList(Interval(-5.12, 5.12)))
```
```scala
Lenses._vector[Int].get(x)
```

This lense will not work for `Doubles`.

### _solutionPrism

Will provide a `Prism`.
If the `Position` is a `Solution` is will be returned in `Some`.
Else it's a `Point` and `None` will be returned.

```scala :silent
val rng = RNG.init(12)
val interval = Interval(-5.12,5.12)^3
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
val pos = Position.eval(e, Position.createPosition(interval).eval(rng)).eval(rng)
```
```scala
val solution = Lenses._solutionPrism.getOption(pos).get
```

### _objectiveLens

Provides a `Lens` that *zooms* in on the `Objective` of a `Solution`

```scala :silent
val rng = RNG.init(12)
val interval = Interval(-5.12,5.12)^3
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
val pos = Position.eval(e, Position.createPosition(interval).eval(rng)).eval(rng)
val solution = Lenses._solutionPrism.getOption(pos).get
```
```scala
val objective = Lenses._objectiveLens.get(solution)
```

### _singleObjective

Provides a `Prism`.
If the `Object` is a `Single` is will be returned in `Some`.
Else it's a `Multi` and `None` will be returned.

```scala :silent
val rng = RNG.init(12)
val interval = Interval(-5.12,5.12)^3
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
val pos = Position.eval(e, Position.createPosition(interval).eval(rng)).eval(rng)
val solution = Lenses._solutionPrism.getOption(pos).get
val objective = Lenses._objectiveLens.get(solution)
```
```scala
val single = Lenses._singleObjective.getOption(objective).get
```

### _multiObjective

Works like `_singleObjective` but in favour of the `Multi` type.


### _singleFit

Provides a `Lens` that *zooms* in on the `Fit` of a `Objective`

```scala :silent
val rng = RNG.init(12)
val interval = Interval(-5.12,5.12)^3
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
val pos = Position.eval(e, Position.createPosition(interval).eval(rng)).eval(rng)
val solution = Lenses._solutionPrism.getOption(pos).get
val objective = Lenses._objectiveLens.get(solution)
val single = Lenses._singleObjective.getOption(objective).get
```
```scala
val fit = Lenses._singleFit.get(single)
```

### _singleFitness

Will provide a `Prism`.
If the `Position` is a `Solution` its fitness (`Fit`) will be returned in `Some`.
Else it's a `Point` and `None` will be returned.

```scala :silent
val rng = RNG.init(12)
val interval = Interval(-5.12,5.12)^3
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
val pos = Position.eval(e, Position.createPosition(interval).eval(rng)).eval(rng)
```
```scala
Lenses._singleFitness.getOption(pos)
```

### _feasible

Will provide a `Prism`.
If the `Fit` is a `Feasible` its fitness will be returned in `Some`.
Else it's a `Infeasible` and `None` will be returned.

```scala :silent
val rng = RNG.init(12)
val interval = Interval(-5.12,5.12)^3
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
val pos = Position.eval(e, Position.createPosition(interval).eval(rng)).eval(rng)
val solution = Lenses._solutionPrism.getOption(pos).get
val objective = Lenses._objectiveLens.get(solution)
val single = Lenses._singleObjective.getOption(objective).get
val fit = Lenses._singleFit.get(single)
```
```scala
Lenses._feasible.getOption(fit)
```


## Summary

How great are optics?
They provide us with an easy interface with extracting information.
But not only a for extracting (getting) we can also use other methods such as `set` or `modifyF` to return new instances.

Documentation for the optics types:

- [`Lens`][lens-link]
- [`Prism`][prism-link]

Moving forward, we can begin to see how optics will come in handy when we begin to explore a search space.
In the next chapter we will test our newly acquired skills as we begin to put everything together.

<div class="callout callout-info">
Optics in CILib use `Lens` and `Prism` that allow us *zoom* in on instances to retrieve information or create new instances.
</div>
-->
