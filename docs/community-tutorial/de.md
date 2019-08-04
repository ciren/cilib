# DE

In this chapter we will be exploring the differential evolution package of CILib.
What's great about algorithms in CILib is their flexibility.
The DE show cases this wonderfully.
We'll start off with the standard de algorithm and branch off from there with different methods that we are able to plugin to achieve new DEs.

To include this library in your project you can add the following to your list of library dependencies

`"net.cilib" %% "cilib-de" % "@CILIB_VERSION@"`

## DE Method

The de method represents a template differential evolution algorithm that follows the standard (x, y, z) format for defining DEs.
We are able to produce a custom DE based on the parameters passed.

### Parameters

```scala
p_r: Double //Recombination Probability
p_m: Double //Mutation Probability
targetSelection: NonEmptyList[Individual[S, A]] => Step[A, (Individual[S, A], Position[A])] //Selection Method
y: Int Refined Positive //Number of difference vectors
z: (Double, Position[A]) => RVar[NonEmptyList[Boolean]] //Crossover method
```

### Return Type

`NonEmptyList[Individual[S]] => Individual[S] => Step[Double,List[Individual[S]]]`

## Selection Methods

All selection methods will fit meet the required type set out by the `targetSelection` parameter.
That being, a function that takes a value of type `NonEmptyList[Individual[S, A]]` and returns a value of type `Step[A, (Individual[S, A], Position[A])]`.
The list of available selection methods are as follows:

* Random selection
* Best selection
* Random to best selection
* Current to best selection

With the co-responding method headers below:

```scala
randSelection[S, A](collection: NonEmptyList[Entity[S, A]])

bestSelection[S, A](collection: NonEmptyList[Entity[S, A]])

randToBestSelection[S, A: Numeric](gamma: Double)(collection: NonEmptyList[Entity[S, A]])

currentToBestSelection[S, A: Numeric](p_m: Double)(collection: NonEmptyList[Entity[S, A]])
```

## Crossover Methods

CILib offers two crossover methods.
Those being binary and exponential methods, `bin` and `exp` respectively.

```scala
bin[F[_]: Foldable1, A](p_r: Double, parent: F[A])

exp[F[_]: Foldable1, A](p_r: Double, parent: F[A])
```

Both follow the type requirement of the `de's z` parameter, and thus return `RVar[NonEmptyList[Boolean]]`.

## Predefined DEs

All of the following DEs make use of what we already have seen.
So if we wanted to we could produce any one of these ourselves using the base `de` algorithm.
With that being said (and to avoid repetition) the names of the parameters of the predefined algorithms are the same as in the base `de` algorithm.
The method names are indicators of what methods have been *plugged* into `de`, and follow the (x, y, z) convention.
For example, `best_1_bin` represents a `de` utilizing a best selection method, 1 difference vector and a binary crossover.

### best_1_bin

```scala
best_1_bin[S, A: Numeric: Equal](p_r: Double, p_m: Double)
```

### rand_1_bin

```scala
rand_1_bin[S, A: Numeric: Equal](p_r: Double, p_m: Double)
```

### best_1_exp

```scala
best_1_exp[S, A: Numeric: Equal](p_r: Double, p_m: Double)
```

### best_bin

```scala
best_bin[S, A: Numeric: Equal](p_r: Double, p_m: Double, y: Int Refined Positive)
```

### rand_bin

```scala
rand_bin[S, A: Numeric: Equal](p_r: Double, p_m: Double, y: Int Refined Positive)
```

### best_exp

```scala
best_exp[S, A: Numeric: Equal](p_r: Double, p_m: Double, y: Int Refined Positive)
```

### rand_exp

```scala
rand_exp[S, A: Numeric: Equal](p_r: Double, p_m: Double, y: Int Refined Positive)
```

### randToBest

```scala
def randToBest[S, A: Numeric: Equal](
    p_r: Double,
    p_m: Double,
    gamma: Double,
    y: Int Refined Positive,
    z: (Double, Position[A]) => RVar[NonEmptyList[Boolean]])
```

### currentToBest

```scala
def currentToBest[S, A: Numeric: Equal](
    p_r: Double,
    p_m: Double,
    y: Int Refined Positive,
    z: (Double, Position[A]) => RVar[NonEmptyList[Boolean]])
```
