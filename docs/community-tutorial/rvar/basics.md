## The Basics of RVar

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

```tut:book
import cilib._
val rng = RNG.fromTime
```

### Our First RVar

RVar offers a few methods but we won't get into all of the right now. We are going to be looking at the following methods 

```scala
RVar.doubles(n: Int) //Generates a list of size n where each element is a *generator placeholders* of type `Double`.

RVar.ints(n: Int) //Generates a list of size n where each element is a *generator placeholders* of type `Int`.
```

```tut:book
val doubles = RVar.doubles(3)
val ints = RVar.ints(3)
```

### Random Numbers!

Great! We have defined our `Rvars` as well as a `RNG`. 
It's important to know that our `RVar` variables currently **do not** contain any values, only place holders. 
We need to pass our `RNG` to the `RVars` to generate our random values. 
This happens at run time. We can achieve this by using

```tut:book
val doublesResult = doubles.run(rng)
val intsResult = ints.run(rng)

intsResult._2 // Use ._2 to get the actual list of numbers
```
As you can see we have generated random values. 
Since we now understand how `RVar` works, we can move on to using some of the other functions. 

<div class="callout callout-info">
The important point to note is that running the computation again with the same PRNG will give us the exact same numbers. That is, the original state of the PRNG will result in the same obtained results no matter where or when we run it.
</div>