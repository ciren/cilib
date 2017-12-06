```tut:invisible
import cilib._
import spire.math._
import spire.implicits._
```

# Entity

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
refer, collectively, to these algorithm participants as ``Entity`` instances.

An ``Entity`` is a simple structure that contains and manages two very
specific things:

* A ``Position[A]`` within the current search space of the problem
* A "state" that contains all addition data required by the ``Entity``
  which is not managed by the ``Position[A]``.

The resulting ``Entity`` is therefore represented by the following
parameterized data type:

    final case class Entity[S,A](state: S, pos: Position[A])

where ``S`` is the type of the state that the ``Entity`` maintains,
and ``A`` is the type of the dimension element within a ``Position[A]``.

The state value differs between ``Entity`` instances. A Particle requires
a velocity vector and a previous best position vector, whilst an Individual,
requires no additional data other that a ``Position``.

Because the value of ``S`` within the ``Entity`` can be anything, it is
not possible to have predefined functions that allow extraction of data
the ``S`` parameter type. In order to enable this, optics are applied to
the instances.

Within the definitions of CIlib, an Individual is nothing more than an
``Entity[Unit,A]`` for some type ``A``. As the type states, there is no
state value for the ``Entity``, and it is defined to be ``Unit`` - a type that
exists with a single value (expressed as ``()``), which is uninteresting.

Some functions use `Entity` instances, but
constrain the usage based on the shape of the `S` type parameter within the
`Entity[S,A]`. As mentioned within the discussion of
[lenses and optics](), a typeclass `HasMemory` is defined to allow a state
to contain information about some kind of memory for an `Entity`. A data
structure that provides this memory for a Particle is `Mem[A]`, and is simply
the case class:

    case class Mem[A](b: Position[A], v: Position[A])

There already exists an instance of `HasMemory` defined for the `Mem`
data structure. Let's have a look at some usage:

```tut
// Lets create a function that expects the provided Entity to have
// a memory within it's state parameter
def foo[S](x: Entity[S,Double])(implicit mem: HasMemory[S,Double]) =
  mem._memory.get(x.state)

// Now, lets create some Entity instances
val interval = Interval(-5.12,5.12)^3
val individual = Position.createPosition(interval).map(p => Entity((), p))
val particle = Position.createPosition(interval).map(p => Entity(Mem(p, p.zeroed), p))
```

Take note of the value held by the state parameter
in the resulting entity instances above.
We would need to run the `RVar` computation to get the individual and particle
values, or we can simply `map` the function `foo` into the `RVar`, changing
the result of the computation to a `RVar` which when executed will return the memory
of the `Entity`.

```tut
particle.map(p => foo(p)) // This works as expected: particles have a memory
```

Because `individual` does not have a memory defined, the following will fail.
This failure is not only expected but required to ensure that incorrect usages
are disallowed as soon as possible.

```tut:fail
individual.map(i => foo(i))
```

There are several optics predefined that allow for the zooming in of values in
the `Position` as well as the state of an `Entity`. Of course a user could define
their own optics, and it is recommended when custom state types are used,
in order to work with the `Entity` data.
