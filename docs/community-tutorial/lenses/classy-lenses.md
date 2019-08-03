## Classy Lenses

<div class="callout callout-danger">

Based on the normally accepted usage of "classy lenses", the type classes
that expose the lenses are generally prefixed with `Has`.

</div>

So CILib has some classes that expose the lense for us to use.
The reason for this is so that we can define our own functions with the lense.
These *classy lenses* are passed as implicit parameters.
We may make muse of:

- HasMemory
- HasVelocity

The following code is used to create an `Entity` which we will use to explore the class lenses.
Take note of the evaluated `Entity` and its contents.

```tut:book:invisible
import cilib._
import spire.implicits.{eu => _, _}
import spire.math._
```
```tut:book:silent

val interval = Interval(-5.12,5.12)^3

val particle = Position.createPosition(interval).map(p => Entity(Mem(p, p.zeroed), p))

val rng = RNG.init(12)
```
```tut:book
particle.eval(rng)
```

### HasMemory

`HasMemory` contains a `Lens` method that is exposed for use.
By importing CILib we are including the implicits that are defined `HasMemory`.
This allows us to use it in a function were given an `Entity` we may retrieve its best `Position`.

```tut:book:silent
def foo[S](x: Entity[S,Double])(implicit mem: HasMemory[S,Double]) = mem._memory.get(x.state)
```
```tut:book
particle.map(p => foo(p)).eval(rng) 
```
`_memory` is the `Lens` we are using.

### HasVelocity

`HasVelocity` is works the exact same as `HasMemory`.
The only difference is that it returns the velocity of an `Entity's` state.

```tut:book:silent
def foo[S](x: Entity[S,Double])(implicit mem: HasVelocity[S,Double]) = mem._velocity.get(x.state)
```
```tut:book
particle.map(p => foo(p)).eval(rng) 
```

### HasCharge and HasPBestStagnation

**Todo**

### Conclusion

Hopefully the `HasMemory` and `HasVelocity` examples cleared up any confusion you may have had about optics.
We created an `Entity`, called `particle`, and we saw its contents when we evaluated it.
We then used a `Lens` to retrieve a part of the contents.
We could have even used set, or any other method defined in the `Lens` type, to return a new modified result.