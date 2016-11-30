Lenses
======

A ``Lens`` can, in the most trivial of ways, be seen as a getter and setter function pair.
Within CIlib, several common lenses are availble to the user in order to extract information from the state
of the ``Entity``, or anything else for which a ``Lens`` has been defined. Because lenses allow the user
to zoom onto data, potentially nested under many levels in another data structure, they are convienient
as they allow for correctly updating the "zoomed" location. When we use "correctly", it is because with
immutable data structures, updates to values deep within a data structure need to correctly "bubble out", such
that the entire stucture is correctly updated to ensure that the data structure remains immutable and consistent.

In order to not re-invent the wheel, the `Monocle <http://julien-truffaut.github.io/Monocle/>`_ library
provides the lens functionality. Lenses are strucutures that can also be composed together and are
collectively called "optics". Please refer to Monocle's documentation to learn more about
lenses and optics. CIlib specific lens questions can be directed to the different CIlib support channels.

Lenses privide an API that is first and foremost, composition and lawful. This means that the various
optics are well behaved and rules exist that govern their usage. Furthermore, different optics may be
composed together to create new optics that are the combination of the original optics. This is obviously
only possible if the provided types correctly line up.

Building on the usage of optics in general, we use a mechanism known as "classy lens" in Haskell. This
mechanism prevents invalid usage, by letting the compiler fail based on the types being used.
In the case of ``Entity``, the compiler would look up instances, using it's implicit resolution rules, to obtain
evidence for a typeclass with a given set of types, at compile time.

This provides an additional level of surity that the data being passed to a function that expects a parameter
that has the evidence to extract some other piece of information for a given type.
This may seem quite like a mouth full, but let's have a look a few examples that will hopefully make the usage clear.

Based on the normally accepted usage of "classy lenses", the typeclasses that expose lenses are generally
prefixed with ``Has``. A simple example of this would be the ``HasMemory`` typeclass.
The typeclass definition is simply: ::

  trait HasMemory[S,A] {
    def _memory: Lens[S, Position[A]]
  }

``HasMemory`` defines a lens, with the name ``_memory`` that can, from a given ``S`` provide a ``Position[A]``.
There is absolutely no mention of what ``S`` is, or should be, but the typeclass allows for a set of
instances that can actually provide this result. For example, in the context of the GA, Individuals are
entities that *do not* maintain a memory; whereas in the PSO, Particles do maintain a memory of their
previous best position (where ``Position[A]`` is a candidate solution of the problem space).

Within the definitions of CIlib, an Individual is nothing more than a ``Entity[Unit,A]`` for some
type ``A``. As the type states, there is no state value for the ``Entity``, and it is defined to
be ``Unit`` - a type that exists with a single value (expressed as ``()``), which is uninteresting.

Using this definition, the following function would accept Pariticle entities, but would fail
to compile (due to missing evidence) if an Individual entity is passed to it: ::

  def foo[S](xs: List[Entity[S,Double]])(implicit mem: HasMemory[S,Double]) = ...

If the function ``foo`` did not have the implicit constraint defined, both a list of particle or individual
entities would be accepted. Within the ``core`` module, several basic optics (focusing on ``Entity``)
are available for use.
