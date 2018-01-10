# Immutable data

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

# Lenses

A ``Lens`` can, in the most trivial of ways, be seen as a simple getter
and setter function pair. Within CIlib, several common lenses are available
in order to extract information from the state of the ``Entity``, or
anything else for which a ``Lens`` has been defined. Because lenses
allow the user to "zoom" onto data, potentially nested under many levels
in another data structure, they are convenient as they allow for correctly
updating the "zoomed" location in addition to extracting the value.

Trying to not re-invent the wheel, the
[Monocle](http://julien-truffaut.github.io/Monocle/>) library
provides the lens functionality. Lenses are structures that can also be
composed together and are collectively called "optics". Please refer to
Monocle's documentation to learn more about lenses and optics. CIlib
specific lens questions can be directed to the CIlib gitter room or IRC.

Lenses provide an API that is first and foremost, composition and lawful.
This means that the various optics are well behaved and rules exist that
govern their usage. Furthermore, different optics may be composed together
to create new optics that are the combination of the original optics. This
is obviously only possible if the provided types correctly line up.

Building on the usage of optics in general, we use a mechanism known as
"classy lens" in Haskell. This mechanism prevents invalid usage, by letting
the compiler fail based on the types being used. In the case of ``Entity``,
the compiler would look up instances, using it's implicit resolution rules,
to obtain evidence for a typeclass with a given set of types, at compile time.

This provides an additional level of surety, that the data being passed to
a function that requires evidence in order to extract some other piece of
information for a given type. The scala compiler provides the evidence
through the use of its implicit lookup mechanics.
This may seem quite like a mouthful, but let's have a look a few examples
that will hopefully make the usage clearer.

Based on the normally accepted usage of "classy lenses", the typeclasses
that expose the lenses are generally prefixed with ``Has``. An example of
this would be the ``HasMemory`` typeclass. The typeclass definition is simply:

    trait HasMemory[S,A] {
      def _memory: Lens[S, Position[A]]
    }

``HasMemory`` defines a lens member, with the name ``_memory`` that can from
a given ``S`` provide a ``Position[A]``. There is absolutely no mention of
what ``S`` is, or should be, but the typeclass allows for a set of instances
that can actually provide this result. Within the context of the GA, Individuals
are entities that _do not_ maintain a memory; whereas within the PSO, Particles
do maintain a memory of their previous best position (where ``Position[A]`` is
a candidate solution of the problem space). The base module within the library
already provides several optics for the user that operate on `Position[A]`
instances, allowing for the extraction of the candidate solution, the fitness
and the constraint violations. Other optics for other structures are also available.

The usage of these classy lenses will become even clearer when we look into what
the generalization of Individual and Particle, the *Entity*.
