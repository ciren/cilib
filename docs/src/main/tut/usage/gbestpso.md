# Global Best PSO (GBestPSO)

The `GBestPSO` is the canonical version of the PSO. It is popular, not
only, because it is the original version of the algorithm (which is cited
often within literature), but is also a simple algorithm to implement.

As with all algorithms modelled as a function, the type of the `GBestPSO`
is simply defined as:

    List[Particle[S,A]] => RVar[List[Particle[S,A]]]

where a collection of entities are transformed from a given set of
entities to a new collection of entities, with randomness applied. This process
is then repeatedly reapplied, until a stopping condition is reached.

We're going to exclude the import statements simply for brevity, but the reader
is encouraged to examine the example algorithm definition in the `examples`
sub-module of the project source.

## Getting things ready

In order to define an experiment, there are a couple of things we need to
get ready first. The most obvious should be that there needs to be some kind
of problem, upon which we will be executing the `GBestPSO`.

As the very first step, we need to get the needed imports in scope:

```tut:silent
import cilib._
import cilib.pso._
import cilib.pso.Defaults._

import scalaz.NonEmptyList
import scalaz.effect._
import scalaz.effect.IO.putStrLn
import scalaz.std.list._
import spire.implicits._
import spire.math.Interval

import cilib.syntax.algorithm._

import scalaz._
import Scalaz._
```

Let's define a simple problem, borrowing the problem definition from the
[benchmarks sister project](http://github.com/cirg-up/benchmarks).

```tut
val spherical = Eval.unconstrained(cilib.benchmarks.Benchmarks.spherical[NonEmptyList, Double]).eval
```

Here we define a value called `spherical`, which is an unconstrained `Eval`
instance, which uses the `spherical` function definiton from the benchmarks
project. We explicitly provide the needed type parameters to keep the compiler
happy, that being that the `Position` is a `NonEmtpyList[Double]`.

Next, we define the `GBestPSO` itself. The `GBestPSO` is defined to use a velocity
update equation that uses the personal best of the current particle and then the
collection's current best particle to determine the new velocity vector for the
current particle within the algorithm.

Let's define the two "particle attractors" which we need in the velocity update
equation. Because these two values will attract or guide the particle in the search
space, we refer to them as `Guide` instances:

```tut
val cognitive = Guide.pbest[Mem[Double],Double]
val social    = Guide.gbest[Mem[Double]]
```

Again, we need to provide some type parameters to keep the compiler happy, but
in this case we need to provide a type called `Mem[Double]`, which is needed to
track the memory of a particle and at the same time, fulfills the function
constraints of the PSO algorithm itself: that the algorithm participants must
cater for a `HasMemory` instance which exists for the `Mem[Double]` type.

Now we can define the algorithm itself, providing some constants that are
known to provide convergent behaviour within the PSO:

```tut
val gbestPSO = gbest(0.729844, 1.496180, 1.496180, cognitive, social)
val iter = Iteration.sync(gbestPSO)
```

Now that the algorithm is defined, lets now define the entity collection that
we need to given the algorithm instance. The collection defines the bounds for
out problem and also defines how the entity instances will be initialized, once
random positions are generated for the given problem space

```tut
val swarm = Position.createCollection(PSO.createParticle(x => Entity(Mem(x, x.zeroed), x)))(Interval(-5.12,5.12)^30, 20)
```

The last two requirements we have is to define _how_ we want to perform the
optimization and then to provide the RNG instance that will use used within
the algorithm. We define these values and then repeatedly run the algorithm
on the entity collection, stopping after 1000 iterations of the algorithm
have been performed

```tut
val opt = Comparison.dominance(Min)
val rng = RNG.fromTime // Seed the RNG with the current time of the computer

val result = Runner.repeat(1000, iter, swarm).run(opt)(spherical)
val positions = result.map(_.map(x => Lenses._position.get(x)))

positions.run(rng)._2
```
