## Comparison

Like the most of CILib, it's easy to deduce what a component does based on its name.
With `Comparison` we are going to be making... comparisons!
This is going to be a great addition to our arsenal of collected CILib components thus far.
Our `Comparison` object offers us several methods

```scala
dominance(o: Opt): Comparison
quality(o: Opt) = dominance(o)
compare[F[_],A](x: F[A], y: F[A])(implicit F: Fitness[F,A]): Comparison => F[A]
fitter[F[_],A](x: F[A], y: F[A])(implicit F: Fitness[F,A]): Comparison => Boolean
```

Through the use of these methods we will be able to make our programs more elaborate and start getting more optimum results.

First things first, `implicits`.
You might have noticed the `(implicit F: Fitness[F,A])` parameter.
Later on you'll see we need not to worry about this as the implicits needed for the types we will
be using are defined within CILib.
However, for this case and if you intend to use your own data types, we will
need to define the implicits ourselves.
We will be using `NonEmptyList`.

```tut:book:invisible
import cilib._
import scalaz._
import Scalaz._
```
```tut:book:silent
val rng = RNG.init(12L)
val cons = cilib.Equal(ConstraintFunction((l: NonEmptyList[Double]) => l.index(0).getOrElse(0.0)), 4.0)
val e = Eval.constrained[NonEmptyList,Double]((l: NonEmptyList[Double]) => l.suml, List(cons))

implicit def i = new Fitness[NonEmptyList, Double] {
    def fitness(l: NonEmptyList[Double]) = Option.apply(e.eval.run(rng)._2(l))
}
```

We want to calculate the sum of the list with the constraint that the first number needs to be 4.
With that out of the way we may begin exploring the `Comparison` methods.

### dominance

`dominance` creates an instance of a `Comparison` type, based on the supplied `Opt`, that we may use.

```tut:book:silent
val test1 = NonEmptyList(4.0, 5.0, 6.0)
val test2 = NonEmptyList(4.0, 2.0, 33.0)

val comparison = Comparison.dominance(Max)
```
```tut:book
comparison.apply(test1, test2)
```

So clearly `test2` is the better option, as we are calculating the sum of the list.
What if `test2's` first number isn't 4?
Well, then `test1` will be the better choice as `test2` will become infeasible.
Ah but what if both test1's and test2's first number is something other that 4?
Well, both lists will become infeasible, and the first parameter will be returned.

### quality

Does the same thing as `dominance`, just a different name.

### compare

With this we can produce a function, that will work in a similar way to `dominance`.
All we need to do is supply a `Comparison` instance.

```tut:book
Comparison.compare(test1, test2)
Comparison.compare(test1, test2).apply(comparison)
```

### fittest

Works in a similar fashion to `compare` but returns a function of `Comparison => Boolean`.
The function determines if the first parameter is fitter than the second.

```tut:book
Comparison.fitter(test2, test1).apply(comparison)
Comparison.fitter(test1, test2).apply(comparison)
```

And thats how we use the `Comparison` object!