---
id: rvar
title: RVar
hide_title: true
---

# RVar

```scala mdoc:invisible
import cilib._
```

`RVar` (the contraction of *random variable*) is the backbone data structure of CILib.

`RVar` allows for the sequenced composition of actions involving randomness, but more importantly tracks the effect of applying randomness to the computed value (which the `RVar` represents).
Tracking the effect of randomness is important, particularly within Computational Intelligence algorithms, in order to allow for the duplication of the computational process.
In other words, even if a computation uses randomness in order to calculate a value, the resulting value will always be the same provided that the provided inputs are unchanged.
This **purity** (the same inputs producing the same output) allows allows a stochastic process (one which involves randomness) to be represented as a _purely deterministic_ and _declarative_ computation.
The stochastic nature of the data structure is, however, only relevant when an `RVar` instance is evaluated to determine the computed value.

> ##### RVar
>
> An instance of `RVar` represents a computation that when evaluated, produces a value with randomness applied.

## The Basics of RVar

Instances of `RVar` cannot be created without making use of the predefined functions, homed within the `RVar` companion object.
These functions allow for the creation of basic types with applied randomness and include:

- Integers (`Ints` and `Long`)
- Floating point numbers (`Float` and `Double`)
- Booleans (`True`/`False` values)
- Constant values where the randomness is not necessary - useful when a constant value needs to be used within a calculation of `RVar`s.

Other values are built up using these base instances quite easily.
Additionally, functions that operate on the types mentioned above are available.
These operations are common operations which would be tedious for the user to maintain and have been included as part of the library.
It is recommended to look at the documentation for `RVar` to become familiar with the available combinator functions.

## Some example usage

To make the statements in the previous section a little more concrete, let's examine some usage examples.
Assume that you need to create a list of random values.
Now, because we are using a statically typed language, we cannot mix and match different `RVar` instances without actually losing information about the list contents.

Losing information implies that the type which may represent all the types within the list becomes more and more general.
The more general the type becomes, less of the known specific information for the underlying type remains.
For example, in Java a list containing several different types within it will be represented as a `List<Object>`.
The contents of the list are known to be of type `Object`, but not much can be achieved with this knowledge.
`java.lang.Object` does not allow for many methods which means that mon many relevant operations are possible.
Such situations tend to force the use of reflection, which produces fragile and unverifiable programs.

For this reason, the `RVar` companion object allows us to create lists of values using the already defined instances:

```scala mdoc
val listOfInts = RVar.ints(12)
```

So `listOfInts` is a `List[Int]` of 12 elements that are all randomly generated but contained within a `RVar` computation.
It is important to observe that the value of `listOfInt` is _not actually_ 12 random integers, but is rather a computation which when evaluated will produce a list of randomised integers.
Instances of `RVar` may be combined to produce new `RVar` values though the predefined combinator functions and the _for-comprehension_ of Scala.
[//]: # ( The combination of `RVar` values into new values uses the available **Functor, Applicative and Monad** instances for `RVar`.)

Let's now take `listOfInts` and do something with the internal `List[Int]`.

```scala mdoc
val doubledListOfInts =
  for {
    list <- listOfInts
  } yield list.map(x => x * 2)
```

The value `doubledListOfInts` extracted the list contained within the `listOfInts` and then yielded a result which multiplied each of the elements in the list by 2.
The same operation may also be achieved using the `map` method on `RVar`:

```scala mdoc
val doubledListOfIntsAlternative =
  listOfInts.map(_.map(x => x * 2))
```

The result of `doubledListOfInts` and `doubledListOfIntsAlternative` is a `RVar` value, nothing else.
The actual value of the list is _still not known_ when the values are transformed.
Essentially, the `RVar` computation became a richer computation which would first generate the randomised values and then apply a transformation on these values to ultimately produce the final result.
That's quite convenient.
We can now create new `RVar`s using existing `RVar` values and use any method to create these values that is the most comfortable.

Now that we can create these `RVar` instances, we would need to evaluate them in order to produce the presented random value.


## Evaluating RVar instances

It should be stressed that the internal value of `RVar` should not be a concern to the user.
Even though we are playing with these instances now, it should be noted that the library will produce values that should only be evaluated at the "end of the world".
The "end of the world" means that the `RVar`s are evaluated at the entry point to your program (often in the `main` function).
Up until that evaluation point, the `RVar` instances describe the computation including randomness and are often referred to as sub-programs of the main program.

Now, to evaulate a `RVar` instance, the `RVar` needs to be _evaluated_.
This evaluation requires a `RNG` value.
`RNG` is an instance of a pseudo-random number generator (PRNG).
The CMWC generator is recommended due to the good performance properties and overall power of the generator itself.
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

The value of `result` is interesting.
It is a tuple of two values, a `RNG` as well as a `List[Int]` of evaluated randomised integers.
The `RNG` part of the result is the new state of the `RNG`, after the complete evaluation of the `RVar` computation.
By returning the updated `RNG` state, the state may be threaded into a subsequent `RVar` computation.

Provided the same `RNG` state is used and the same sequence of `RVar` instances are evaluated, identical solution is produced.
It might seem that such threading could be error prone.
The `RNG` threading **is** error prone and also the reason why `RVar` is responsible for such threading, instead of the user.
`RVar` is also a stack-safe computation, meaning that the composition of `RVar` instances will not result in a stack-overflow error which is common in highly recursive logic.
