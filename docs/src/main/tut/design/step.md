```tut:invisible
import cilib._
import cilib.pso._
import cilib.pso.PSO._
import scalaz._
import Scalaz._
import spire.algebra._
import spire.implicits._
```

# Step

Apart from tracking the effect of randomness, a computational intelligence
algorithm requires some additional information:

* The optimization scheme (`Opt`) required, either minimization or maximization
* The fitness function evaluation instance, `Eval`, which calculates the quality of
  the candidate solution

The result is a function, which uses a predefined set of values to generate
a `RVar` computation to which randomness still needs to be applied. The function
is therefore of the shape

    (Opt,Eval[A]) => RVar[B]

The predefined set of values required for input is also referred to as the
"environment". Given that the environment is decided once, at the beginning of
the execution process, we can factor out this common parameter using a Kleisli
arrow, on this case the `ReaderT` monad transformer is applicable, resulting
in the final type of:

    ReaderT[RVar, (Opt,Eval[A]), B]

Because monad transformers are monads themselves, we can freely compose different
`Step` instances to create a larger computation. The larger computation, being
termed the "algorithm". Due to the sequencing action of monads, `Step` instances
are analogous to steps in algorithm pseudo-code.

Now that the intention of `Step` is clearer, lets have a look at some usage. Keep
in mind that even though the final "shape" of the `Step` is defined, this does not
limit the manner in which we can create `Step` instances.
Within a PSO, each particle has several actions applied to it, in order to create
a new particle that occupies a different location within the problem search space.

The canonical PSO algorithm iteratively alters the position (and state data) of each
particle to create a new particle for each within the collection. The new collection
essentially replaces the original collection and this process repeats until some
kind of stopping condition is met. Stopping condition logic is intentionally not
included within the `Step`. The type for a PSO is then

    List[Particle[S,A]] => Particle[S,A] => Step[RVar,(Opt,Eval[A]),Particle[A]]

and the algorithm definition is:

```tut
def pso[S](w: Double, c1: Double, c2: Double,
  cognitive: Guide[S,Double], social: Guide[S,Double]
)(implicit M: HasMemory[S,Double], V: HasVelocity[S,Double]
): NonEmptyList[Particle[S,Double]] => Particle[S,Double] => Step[Double,Particle[S,Double]]  =
  collection => x => for {
    cog     <- cognitive(collection, x)
    soc     <- social(collection, x)
    vel     <- stdVelocity(x, soc, cog, w, c1, c2)
    p       <- stdPosition(x, vel)
    p2      <- evalParticle(p)
    p3      <- updateVelocity(p2, vel)
    updated <- updatePBest(p3)
  } yield updated
```

That sure looks complex! Sure, there are a few things going on there, but it certainly
is not complex - the syntax just makes the intention a little hidden due to the verbosity.
The `pso` function defines that there are some parameters that it requires
for the `stdVelocity` function, namely `w`, `c1`, `c2`. Next it requires two individual
`Guide` instances which are the particle attactors for the velocity update equation. In
the next parameter group is an implicit parameter that is constraining the types of
data elements `pso` can work with. In this case, the `pso` requires that all `Particle`
instances (which is just an alias for the type `Entity[S,Position[Double]]`)
must have a `HasMemory` and `HasVelocity` instance available for the compiler to provide.
As mentioned previously, in the section about lenses and optics, these classy lens instances
prevent `Entity` types that do not have a memory nor a velocity present in their
state parameter type. Again, we don't know what the value of `S` is at the definition
level, it will be made concrete when we actually use the `pso` function.

Every function within the for-comprehension is a function that yields a `Step` instance
of the type `Step[Double,Particle[S,Double]]` (in this usage example). The individual
`Step`s are then composed into a larger composition that ultimately results in the
creation of a new piece of data: the particle which replaces the particle identified
by the parameter `x`.

# Iteration

Algorithms can be executed either synchronously or asynchronously. Given an
algorithm definition like the definition of `pso` above, it may be passed into
an iteration function which converts the signature of `pso` function into the
shape:

    List[Particle[S,A]] => RVar[A,Particle[S,A]]

It is important to note that the manner of execution constrains the amount
of parallelism that may be applied to the iteration process. The synchronous
scheme can be completely parallelized as there is no reliance on the currently
building collection. The asynchronous process on the other hand builds the new
collection from a combination of the current and new collections. As a result
of the manner in which the asynchronous process executes, parallelism is not
possible due to the shared state that is managed during the execution process.
