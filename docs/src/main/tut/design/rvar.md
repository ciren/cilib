```tut:invisible
import cilib._
import scalaz._
import Scalaz._

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.auto._
```

# RVar

An instance of `RVar` represents a computation that, when executed,
results in a value with randomness applied. `RVar` is one of the most
important structures in CIlib and is therefore discussed first in order to
understand how the data structure works.

`RVar` has a [monad]() instance and therefore allows a for a large amount
of composition, but more importantly allows for the tracking of randomness
within the `RVar` computation. This tracking is of the utmost importance
within computational intelligence algorithms, as randomness needs to be
controlled in a manner that facilitates repetition. In other words,
even if a computation uses randomness, given the same inputs, the same
results are expected even with randomness applied.

Due to the monadic nature of the data structure, the data structure may be
transformed by functions such as `map`, `flatMap`, etc

There are several predefined combinators that allow the user to use and
create `RVar` computations. These include functions for randomness applied
to primitive types (such and `Int` and `Double`) to more complex types that
build on the primitives, or even for user defined types.

The simplest would be to look at some examples of `RVar` usage. It is quite
common to request several random numbers. `RVar` provides several functions,
with `ints` and `doubles` being the most common for random variable
creation:

```tut
val ints = RVar.ints(5)
val doubles = RVar.doubles(5)
```

Both functions result in a `RVar` that, when provided with a pseudo-random
number generator (PRNG), will result in a list of values.

The user if free to define a PRNG for themselves, but CIlib provides a default
PRNG that is suitable for scientific work. The CMWC generator may be
initialized by either providing a seed value for the pseudo-random number
stream, or it may be taken from the current time of the computer. It is always
recommended to record the seed value, so that others may reproduce results,
especially if the results are to be published.

Let's create a `RNG` instance using both methods:

```tut
val rng = RNG.init(1234L)
val fromTimeYOLO = RNG.fromTime
```

Now, let's run both `doubles` and `ints` with the generator:

```tut
val r1 = ints.run(rng)
doubles.run(rng)
val r2 = ints.run(rng)

r1._2 == r2._2
```

The result is a tuple of the state of the PRNG after being used in the
computation, together with the result of the computation itself. The
important point to note is that running the computation again, with the
same PRNG, that is the original state of the PRNG _will_ result in the same
obtained results. Unlike the normal PRNG within the JVM platform, obtaining
some random value from the source does not implicitly mutate the PRNG. In
order to keep selecting from the PRNG stream, the next state of the PRNG
should be passed into subsequent computations, when needed:

```tut
val (rng2, x) = ints.run(rng)

val (rng3, y) = ints.run(rng2)

x != y
```

This manual state passing for the PRNG is very cumbersome and as a result,
the monad instance of `RVar` provides this exact functionality to the user,
thereby preventing accidental errors due to incorrect usage of PRNG state.
Furthermore, the monad instance for `RVar` allows for cleaner syntax through
the use of a `for`-comprehension as provided by Scala:

```tut
val composition = for {
  a <- RVar.next[Int] // Get a single Int
  b <- RVar.next[Double] // Get a single Double, using the next state of the PRNG
  c <- RVar.next[Boolean] // Get a Boolean, again passing the PRNG state
} yield if (c) a*b else b

composition.run(rng)
```

From this definition of how randomness is managed, we can derive several
useful algorithms which operate within the `RVar` computation. Please refer
to the [scaladoc](http://cirg-up.github.io/cilib/api/cilib/RVar$.html) for
more combinators, but some of the more commonly used are illustrated below:

```tut
val sampleList = NonEmptyList(6,4,5,2,1,3)

RVar.shuffle(sampleList).run(rng)
RVar.sample(3, sampleList).run(rng)
```

Building on `RVar`, we can easily define probability distributions from
which, randomness may be sampled. The provided distributions, where
standard distributions are also defined, include:

* Uniform
* Gaussian / Normal
* Cauchy
* Gamma
* Exponential
* etc

The interface for the distributions is simply a resulting `RVar`

```tut
// Use a derived function from monad to repeat an action 'n' times
Dist.stdNormal.replicateM(5).run(rng)
```
