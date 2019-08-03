## Distributions

A big part of `RVar` is the `Dist` object which offers some of the popular distributions for us to use in our projects.
These come in the form of functions and methods, all of which return an instance of `RVar`.

### Functions

```scala
stdUniform: RVar[Double]

stdNormal: RVar[Double]

stdCauchy: RVar[Double]

stdExponential: RVar[Double]

stdGamma: RVar[Double]

stdLaplace: RVar[Double]

stdLognormal: RVaDoubler[]
```

### Methods

```scala
uniformInt(i: Interval[Int]): RVar[Int]

uniform(i: Interval[Double]): RVar[Double]

cauchy(l: Double, s: Double): RVar[Double]

gamma(k: Double, theta: Double): RVar[Double]

exponential(l: Option[Positive[Double]]): RVar[Double]

laplace(b0: Double, b1: Double): RVar[Double]

lognormal(mean: Double, dev: Double): RVar[Double]

dirichlet(alphas: List[Double]): RVar[List[Double]]

gaussian(mean: Double, dev: Double): RVar[Double]

levy(l: Double, s: Double): RVar[Double]
```

### Distribution Examples

As you can see there is quite a lot for us to choose from.
I wont be able to cover them all, but let's look at a few.

```tut:book:invisible
import cilib._
val rng = RNG.fromTime
```
```tut:book
Dist.stdNormal.run(rng)
```

Using the functions is pretty straight forward, as are the methods, however, some methods make use of `Interval[A]` which you might be be unfamiliar with. 
If that's the case, don't to worry, here is how you would do it. 

```tut:book:silent
import spire.implicits._
import spire.math.Interval
```
```tut:book
val interval = Interval(-1.25, 2.75)
Dist.uniform(interval).run(rng)
```

But what if we needed a list of the random numbers from the distribution? 
We can use `replicateM(n: Int)` to repeat the action n amount of times.

```tut:book:invisible
import scalaz._
import Scalaz._
```
```tut:book
Dist.stdNormal.replicateM(5).run(rng)
```

Although these examples were somewhat simple, they were just to demonstrate how they may be used.