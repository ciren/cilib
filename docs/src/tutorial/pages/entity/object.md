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