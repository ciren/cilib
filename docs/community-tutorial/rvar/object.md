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

```tut:book:invisible
import cilib._
val rng = RNG.fromTime
```
```tut:book
RVar(rng => (rng, 2)).eval(rng)
```

### point

`point` performs `apply`. The benefit is that we need not worry about supplying a `RNG`.

```tut:book
RVar.point(4).eval(rng)
```

### next

This is a really cool method as it allows us to access the next number from the PRNG stream.
Furthermore, the monad instance for RVar allows for cleaner syntax through the use of a for-comprehension.

```tut:book
val composition = for {
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

```tut:book:invisible
import scalaz._
```
```tut:book
RVar.choices(4, NonEmptyList(4, 3, 2, 56, 78)).run.eval(rng)
RVar.sample(4, NonEmptyList(4, 3, 2, 56, 78)).run.eval(rng)
```

It makes use of an `OptionT`. 
An example of use with these functions would be selecting entities for a crossover method,
perhaps in a GA or DE.

### shuffle

Shuffle does exactly what you would expect it to do, shuffle a list.

```tut:book
RVar.shuffle(NonEmptyList (73, 5, 2, 3, 19, 28)).eval(rng)
```