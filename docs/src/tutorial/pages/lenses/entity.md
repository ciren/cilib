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