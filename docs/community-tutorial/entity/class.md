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