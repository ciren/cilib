Entity
======

Within swarm intelligence, evolutionary computation and other similar algorithms, there
is always a metaphor that the algorithm is based on. Using this metaphor, the participants
withing the algorithm are also appropriately named. For example, within a
:abbr:`PSO (Particle Swarm Optimization)`, the participants are referred to as Particles,
with Individuals being used in both :abbr:`DE (Differential Evolution)` and
:abbr:`GA (Genetic Algorithms)`. Many other examples can easily be identified in
available literature.

It is not practical to have several representations for something that is essentially
the same in these individual algorithms. Based on the experimentation within CIlib,
it was identified that a common structure could be used to represent the participants
for these metaphor-based population based algorithms, which we refer to as an ``Entity``.

An ``Entity`` is a simple stucture that contains and manages two very specific things:

* A ``Position`` within the current search space of the problem in question
* A "state" that contains all addition data required by the ``Entity`` that is not
  the position

As a result, ``Entity`` is represented by the following parameterized data type

.. code-block:: scala

   final case class Entity[S,A](s: S, x: Position[A])

where ``S`` is the type of the state that the ``Entity`` maintains, and ``A`` is the type
of a dimension within a ``Position``.

The state value differs between ``Entity`` instances, where a Particle may need to manage a
velocity vector and a previous best position vector, an Individual, by contrast, need
manage no additional data.

Because the value of ``S`` can be anything, it is not possible to have pre-defined functions
that allow extraction of data from whatever type ``S`` may be. In order to enable this,
a structure ``Lens`` was used. A ``Lens`` can be seen as a getter and setter function
pair. Within CIlib, several common lenses are availble to extract information from the state
of the ``Entity``. `Monocle <http://julien-truffaut.github.io/Monocle/>`_ is used to provide
the lens functionality. Lenses are strucutures that can also be composed together and are
collectively called "optics". Please refer to the documentation of Monocle to learn more about
lenses and optics.
