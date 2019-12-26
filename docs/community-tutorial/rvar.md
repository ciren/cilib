---
id: rvar
title: RVar
---

`RVar` (random value) is the backbone data structure of CILib.
The monad instance for `RVar` allows for composition, but more importantly enables tracking the effect of applying randomness to the computed value (which the `RVar` represents).
Tracking the effect of randomness is very important, particularly within Computational Intelligence algorithms, in order to allow for the duplication of computational process.
In other words, even if a computation uses randomness, given the same inputs, the same output will be obtained.
This `purity` allows allows a stochastic process (one which involves randomness) to be represented as a purely deterministic computation.

> ##### RVar
>
> An instance of `RVar` represents a computation that, when executed, results in a value with randomness applied.

## The Basics of RVar

FIXME: Stopped editing here -> continue from here


Let’s get to know RVar by going over an example.
In general, we can say that there are three parts to using RVar:

1. The number generator
2. The random variable
3. The result

Our example will look at each of these steps in detail so we can really understand *what* **RVar** is.

### The Number Generator

In order to have random numbers we need a random number generator.
You are more than welcome to define your own pseudo-random number generator (PRNG).
However, CILib offers a random number generator object called `RNG` that is suitable for scientific work.
`RNG` utilizes a [linear congruential generator][link-lcg] method as well as a
[complementary-multiply-with-carry (CMWC)][link-cmwc] method to generate its random numbers.
`RNG` does just that, generate random numbers.
All we need to do is supply a seed value.
It is always recommended to record the seed value, so that others may reproduce results, especially if the results are to be published.
`RNG` offers 4 methods for us to use.

```scala
RNG.fromTime //Generates a `RNG` with a seed value based on the current time.

RNG.init(seed: Long) //We can supply the seed value to generate a `RNG`.

RNG.initN(n: Int, seed: Long) //Given a seed value we can generate a list of n `RNG`.

RNG.split((r: RNG) //Generates a tuple of two `RNGs` based on the original's, r's, seed value.
```

Now that we know what `RNG` is all about, let's create an instance for our example.

```scala mdoc
import cilib._

val rng = RNG.fromTime
```

### Our First RVar

RVar offers a few methods but we won't get into all of the right now. We are going to be looking at the following methods

```scala
RVar.doubles(n: Int) //Generates a list of size n where each element is a *generator placeholders* of type `Double`.

RVar.ints(n: Int) //Generates a list of size n where each element is a *generator placeholders* of type `Int`.
```

```scala mdoc
val doubles = RVar.doubles(3)
val ints = RVar.ints(3)
```

### Random Numbers!

Great! We have defined our `Rvars` as well as a `RNG`.
It's important to know that our `RVar` variables currently **do not** contain any values, only place holders.
We need to pass our `RNG` to the `RVars` to generate our random values.
This happens at run time. We can achieve this by using

```scala mdoc
val doublesResult = doubles.run(rng)
val intsResult = ints.run(rng)

intsResult._2 // Use ._2 to get the actual list of numbers
```
As you can see we have generated random values.
Since we now understand how `RVar` works, we can move on to using some of the other functions.

:::note
The important point to note is that running the computation again with the same PRNG will give us the exact same numbers.
That is, the original state of the PRNG will result in the same obtained results no matter where or when we run it.
:::

## RVar Class

Now that we have some introductory knowledge as to how `RVar`
works we should distinguish between the RVar class and its companion object.
The easiest way to do this is to look at the the methods that each offer.

In this section, as is the theme of this book,
I will cover each method to help you understand functionally the `RVar` class has to offer.
This will also serve as documentation to the class. At a quick glance we will be covering

```scala
run(initial: RNG): (RNG, A)

exec(s: RNG): RNG

eval(s: RNG): A

map[B](f: A => B): RVar[B]

flatMap[B](f: A => RVar[B]): RVar[B]
```

However, it's important to note that we are only able to instantiate `RVars` through the companion object.
Alright, let's get started.


### run

We have seen `run` before in our introductory example.
It returns the a `tuple` containing the `RNG` used as well as the resulting random value/s of type A.

:::note
Invoking `run`, or any method that uses `run`, on the same `RVar` with the same `RNG` it will always produce the same result.
:::

```scala mdoc
val doubleResult = doubles.run(rng)._2
val doubleResult2 = doubles.run(rng)._2
doubleResult == doubleResult2
```

### exec

`exec` performs `run` and only returns the `RNG` used.

<div class="callout callout-warning">
Note that the `RNG` returned is not our original, but rather a new `RNG` that represents the next state of the original after being used.
If we were to use the returned `RNG` on the same `RVar` it would result in new random numbers.

```scala mdoc
val (newRNG, x) = doubles.run(rng)
doubles.run(newRNG)
newRNG == rng
```
</div>

### eval

`eval` performs `run` and only returns the resulting random value/s of type A.

### map

When we use `map`, we are simply changing the values in the context (RVar).
An example of such a use would be to multiply each number by a factor.

```scala mdoc
doubles.eval(rng)
doubles.map(x => x map(_ * 0.2)).eval(rng)
```

In this case, `x` is our `List[Double]` and we are saying for every `Double` in the list, multiply it by 0.2.

### flatMap

When we use `flatMap`, we are changing the values and the context.
Lets say we had a `RVar` of type `List[Double]` and we wanted a new `RVar` that contained a `List` with elements
from the original as well as each element multiplied by 0.2.

```scala mdoc
doubles.eval(rng)
doubles.flatMap(x => RVar.point(x.flatMap(el => List(el, el * 0.2)))).eval(rng)
```

We now have our original values along side our newly mutated values.

## RVar Companion Object

We now know how to use instances of `RVar` but now we are going to focus on creating them.
This is the purpose of the companion object.

The `RVar` Companion Object has several methods that allow us to create instances of `RVar` (the class).

```scala
apply[A](f: RNG => (RNG, A)): RVar[A]

point[A](a: => A): RVar[A]

next[A](implicit e: Generator[A]): RVar[A]

ints(n: Int): RVar[List[Int]]

doubles(n: Int): RVar[List[Double]]

choose[A](xs: NonEmptyList[A]): RVar[Option[A]]

choices[A](n: Int, xs: NonEmptyList[A]): OptionT[RVar, List[A]]

sample[A](n: Int, xs: NonEmptyList[A]): OptionT[RVar, List[A]]

shuffle[A](xs: NonEmptyList[A]): RVar[NonEmptyList[A]]
```

We will be testing all these methods using `eval` with a `RNG` on the instances of `RVar` that they create.

### apply

Given a `RNG` and type `A` apply will return an `RVar` of that type.

```scala mdoc
RVar(rng => (rng, 2)).eval(rng)
```

### point

`point` performs `apply`. The benefit is that we need not worry about supplying a `RNG`.

```scala mdoc
RVar.point(4).eval(rng)
```

### next

This is a really cool method as it allows us to access the next number from the PRNG stream.
Furthermore, the monad instance for RVar allows for cleaner syntax through the use of a for-comprehension.

```scala mdoc
val composition =
  for {
    a <- RVar.next[Int] // Get a single Int
    b <- RVar.next[Double] // Get a single Double, using the next state of the PRNG
    c <- RVar.next[Boolean] // Get a Boolean, again passing the PRNG state
  } yield if (c) a*b else b

composition.run(rng)
```

### ints and doubles

We have already seen `ints` and `doubles` used before.

```scala
RVar.doubles(n: Int) //Generates a list of size n where each element is a *generator placeholders* of type `Double`.

RVar.ints(n: Int) //Generates a list of size n where each element is a *generator placeholders* of type `Int`.
```

### choices and sample

These two methods perform the exact same function and will produce the same result.

```scala mdoc:invisible
import scalaz._
import _root_.eu.timepit.refined.auto._
```
```scala mdoc
RVar.choices(4, NonEmptyList(4, 3, 2, 56, 78)).eval(rng)
RVar.sample(4, NonEmptyList(4, 3, 2, 56, 78)).eval(rng)
```

It makes use of an `OptionT`.
An example of use with these functions would be selecting entities for a crossover method,
perhaps in a GA or DE.

### shuffle

Shuffle does exactly what you would expect it to do, shuffle a list.

```scala mdoc
RVar.shuffle(NonEmptyList (73, 5, 2, 3, 19, 28)).eval(rng)
```


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

```scala mdoc
Dist.stdNormal.run(rng)
```

Using the functions is pretty straight forward, as are the methods, however, some methods make use of `Interval[A]` which you might be be unfamiliar with.
If that's the case, don't to worry, here is how you would do it.

```scala mdoc:silent
import spire.implicits._
import spire.math.Interval
```
```scala mdoc
val interval = Interval(-1.25, 2.75)
Dist.uniform(interval).run(rng)
```

But what if we needed a list of the random numbers from the distribution?
We can use `replicateM(n: Int)` to repeat the action n amount of times.

```scala mdoc:invisible
import scalaz._
import Scalaz._
```
```scala mdoc
Dist.stdNormal.replicateM(5).run(rng)
```

Although these examples were somewhat simple, they were just to demonstrate how they may be used.


## Exercises

### Question 1
Define a function that accepts a parameter of type `Int`, called n, that returns `RVar[List[Double]]`,
where the `List[Double]` is of size n.

<div class="solution">

```scala mdoc:invisible
import cilib._
```
```scala mdoc:silent
def genList(n: Int): RVar[List[Double]] = RVar.doubles(n)
```
</div>

### Question 2
Modify your function to include two additional parameters of type `Double`,
which represent the lower and upper bounds of numbers we want to generate.

<div class="solution">

```scala mdoc:silent
import scalaz._
import Scalaz._
import spire.implicits._
import spire.math.Interval

def genList(n: Int, lower: Double, upper: Double): RVar[List[Double]] = {
    val interval = Interval(lower, upper)
    Dist.uniform(interval).replicateM(n)
}
```
```scala mdoc
genList(5, 0.2, 0.5).run(rng)
```
</div>

### Question 3
Define a second function that accepts a parameter of type `List[Double]`, called randomList.
The function returns 3 randomly selected values from randomList, along with each selected value multiplied by a factor of 0.15.
The type of the returned value should be `RVar[List[Double]]`.

<div class="solution">

```scala mdoc:silent
def select(randomList: NonEmptyList[Double]): RVar[List[Double]] =
  for {
    sample <- RVar.sample(3, randomList)
  } yield sample match {
    case None => List(0.0)
    case Some(list) => list.map(element => element * 0.15)
  }
```
</div>

### Question 4
Define a third function that accepts a parameter of type `Int`, called iterations, and returns a value of `List[Double]`.
The function should run from 0 to iterations.
At each iteration select the highest value from 5 random numbers and add it to a `List`.
This list should be returned.

<div class="solution">

```scala mdoc:silent
import scala.collection.mutable.ListBuffer
def getMax(iterations: Int): List[Double] = {
    var rng = RNG.fromTime
    var result = ListBuffer[Double]()
    for (count <- 0 to iterations){
        val (newRNG, randomValues) = RVar.doubles(5).run(rng)
        result = result += randomValues.max
        rng = newRNG
    }
    result.toList
}
```
</div>

### Question 5
Write the necessary statements that will produce a `RVar[List[Double]]` where each element is determined by a `Boolean` value.
If the `Boolean` is `true` the element should be in the range of 20 to 75.
And if the `Boolean` is `false` the element should be -1.
The `List ` shoould be of size 5.

<div class="solution">

```scala
val solution = for {
    value <- Dist.uniform(Interval(20.0, 75.0))
    check <- RVar.next[Boolean]
} yield if (check) value else -1.0

solution.replicateM(5)
```
</div>

## Summary

In this chapter we learnt what is `RVar`, how it's used and where it's used.
Understanding this makes understanding and using CILib that much easier.

<div class="callout callout-info">
`RVar` is a monadic structure of type `A` that when used with a pseudo-random number generator, such as `RNG`,
results in random a `List` values of type `A`. This is done at run time.
</div>

<div class="callout callout-info">
The `RVar` companion object provides several ways that allow use to create instances of `RVar` (the class).
</div>

<div class="callout callout-info">
The `Dist` object allows us to use distributions, in a natural way, that result in `RVars`.
</div>
