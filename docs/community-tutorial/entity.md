# Entity

By now you should have enough some experience with `Entity`.
If not, refer to the "A Quick Look at Entity" section in the `Lenses` chapter.
Thus we can say the following about `Entity`.

<div class="callout callout-info">
An `Entity` is a simple structure that contains and manages two very
specific things:

- A `Position[A]` within the current search space of the problem
- A "state" that contains all addition data required by the `Entity`
  which is not managed by the `Position[A]`.
</div>

An `Enity` is used to represent a singular *thing* exploring the search space.
This *thing* changes from algorithm to algorithm.
[Gary][Link-Gary] gives a clear explanation in the following segment as to why CILib uses `Entity`.

<div class="callout callout-danger">
Within swarm intelligence, evolutionary computation and other, similar
algorithms, there is always a metaphor that the algorithm is based on.
Using this metaphor, the participants within the algorithm are also
appropriately named. For example, within a Particle Swarm Optimization (PSO),
the participants are referred to as Particles, with Individuals being
used in both Differential Evolution (DE) and Genetic Algorithms (GA).
Many other examples can easily be identified in available literature.

It is not practical to have several representations for a very similar
concept used within these algorithms. Based on experimentation within CIlib,
a common structure was identified that could be used to represent the
participants for these metaphor-based population based algorithms. We
refer, collectively, to these algorithm participants as `Entity` instances.
</div>

## Exploring The Entity Class

`Entity` has the following constructor:

- `Entity[S,A](state: S, pos: Position[A])`

Where...

- `S` is the type of the state that the `Entity` maintains.
- `A` is the type of the dimension element within a `Position[A]`.

### state

A we discussed before, the state of an `Entity` can be anything.
The result of this that there aren't any predefined functions to extract the state information from the `Entity`.
But! We just learnt about `Optics` in the last chapter.
Luckily CILib does have predefined `Optics` for where `S` is of type `Mem`.
However, if were to choose your own custom type for the state parameter, you can (and it's recommend) define your own `Optics` for the type.

### Individual

Now `Entity` is great for PSO algorithms because it is able to hold a state.
For genetic algorithms, however, you do not need a state.
Thus we could pass a `Unit` for the state to create an individual.

```tut:book:invisible
import cilib._
import spire.implicits.{eu => _, _}
import spire.math._
```
```tut:book:silent
val interval = Interval(-5.12,5.12)^3
val individual = Position.createPosition(interval).map(p => Entity((), p))
```

This is actually a type within the `GA` package called `Individual`.
We will look at it more when we get to the second part of the book.

## Entity Companion Object

By now we understand what is `Entity`.
And the companion object offers us a few methods.

```scala
implicit def entityEqual[S,A:scalaz.Equal]: scalaz.Equal[Entity[S,A]]

implicit def entityFitness[S,A]: Fitness[Entity[S,?],A]
```

### entityEqual

This `implicit` will determine if two `Entities` are equal.
This is useful for example in a DE where you would avoid selecting duplicate `Entities` when producing a trial vector.

### entityFitness

Used in places where retrieving the fitness of an `Entity`.
For example, in a tournament selection.


## Summary

We explored every aspect of `Entity`.
And to reiterate

<div class="callout callout-info">
An `Entity` is a simple structure that contains and manages two very
specific things:

- A `Position[A]` within the current search space of the problem
- A "state" that contains all addition data required by the `Entity`
  which is not managed by the `Position[A]`.
</div>

But wait! We were introduced to two things we had not seen before.
That being

- `Step`
- `Environment`

Now, if you are asking "What are those?"
Fear not as we find out in the next chapter of "A Guide to CILib"!
