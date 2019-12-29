---
id: rvar
title: RVar
---

```scala mdoc:invisible
import cilib._
```

`RVar` (random value) is the backbone data structure of CILib.
The monad instance for `RVar` allows for sequenced composition, but more importantly enables tracking the effect of applying randomness to the computed value (which the `RVar` represents).
Tracking the effect of randomness is very important, particularly within Computational Intelligence algorithms, in order to allow for the duplication of computational process.
In other words, even if a computation uses randomness, given the same inputs, the same output will be obtained.
This **purity** (the same inputs producing the same output) allows allows a stochastic process (one which involves randomness) to be represented as a _purely deterministic_ computation.
The stochastic nature of the data structure is only relevant when an `RVar` instance is evaluated into a raw value.

> ##### RVar
>
> An instance of `RVar` represents a computation that, when finally evaluated, produces a value with randomness applied.

## The Basics of RVar

Instances of `RVar` cannot be created without using the predefined functions within the `RVar` companion object.
These functions allow for the creation of basic types that have randomness applied and include:

- Integers (`Ints` and `Long`)
- Floating point numbers (`Float` and `Double`)
- Booleans (`True`/`False` values)
- Constant values where the randomness is simply not applied - this is useful when a constant value needs to be used within a calculation of `RVar`s.

Other values are built up using these base instances quite easily.
Additionally, functions that operate on the types mentioned above are available.
These operations are very common operations that would be tedious for the user to maintain and have been included as part of the library.
It is recommended to look at the [documentation for `RVar`]() to get a feeling for the available combinator functions.

## Some example usage

To make the statements in the previous section a little more concrete, let's examine some examples.
Assume that you need to create a list of random values.
Now, because we are using a statically-typed language, we cannot mix and match different `RVar` instances without actually losing information about the contents of a list.
For this reason, the `RVar` companion object allows us to create lists of values using the already defined instances:

```scala mdoc
val listOfInts = RVar.ints(12)
```

So `listOfInts` is a `List[Int]` of 12 elements, wrapped within a `RVar`, that are all randomly generated.
Importantly, observe that the value of `listOfInt` is _not_ 12 random integers, but is simply an instance of `RVar`.
Instances of `RVar` may be combined to produce new `RVar` values.
The combination of `RVar` values into new values uses the available **Functor, Applicative and Monad** instances for `RVar`.
You need not concern yourself with that too much, but know that these structures are at work when you combine `RVar` values using any of the provided combinators or the Scala _for-comprehension_ feature.

Let's now take `listOfInts` and do something with the internal `List[Int]`.

```scala mdoc
val doubledListOfInts =
  for {
    list <- listOfInts
  } yield list.map(x => x * 2)
```

Look at that. Interesting. The value `doubledListOfInts` took all list within the `listOfInts` and then multiplied each of the elements in the list by 2.
The same operation may also be achieved using the `map` method on `RVar`:

```scala mdoc
val doubledListOfIntsAlternative =
  listOfInts.map(_.map(x => x * 2))
```

The result of `doubledListOfInts` and `doubledListOfIntsAlternative` is just a `RVar` value, nothing else.
That's quite convenient.
We can now create new `RVar`s using existing `RVar` values and use any method to create these values that is the most comfortable.

Now that we can create these `RVar` instances, we would need to evaluate them in order to produce the presented random value.


## Evaluating RVar instances

It should be stressed that the internal value of `RVar` should not be a concern to the user.
Even though we are playing with these instances now, it should be noted that the library will produce values that should only be evaluated at the "end of the world".
The "end of the world" means that the `RVar`s are evaluated at the entry point to your program (often in the `main` function).
Up until that evaluation point, the `RVar` instances are simply describing the computation process with randomness and are often referred to as sub-programs of the main program.

Now, to evaulate a `RVar` instance, the `RVar` needs to be _executed_.
This execution requires a `RNG` value.
`RNG` is simply an instance of a pseudo-random number generator, and we suggest the use of the CMWC generator due to the good performance properties and overall power of the generator itself.
`RNG` creation is similar to that of `RVar`, in that the `RNG` companion object's functions should be used to create the values.
Pseudo-random number generators require a starting point, or a _seed_ value.
This is the single value that initializes the internal state of the generator, and is the single value required to produce the same result from the same `RVar` program.
The ability to reproduce values, even though randomness is involved, is the most crucial feature of the library.
Reproduction allows others to obtain the same results for a given experiment / algorithm execution, which in turn allows for simplified error correction and validation of results.

Let's create a `RNG` value and evaluate one of the previously created `RVar` instances:

```scala mdoc
// Our seed is 1234
val rng = RNG.init(1234)

val result = doubledListOfInts.run(rng)
```

The value of the `result` is interesting.
It is a tuple of two values, a `RNG` as well as a `List[Int]` of evaluated randomised integers.
The `RNG` part of the result is the state of the new state of the `RNG` after the evaluation of the `RVar`.
By returning the updated state, it allows for the threading of the `RNG` through another `RVar` computation.
It might seem that such threading could be error prone, and that is totally correct and also the reason why the `Monad` instance for `RVar` is responsible for such threading, instead of the user.

`RVar` is a specialized **state monad**, with the `RNG` being the state, and therefore correctly handles the state updates internally.
