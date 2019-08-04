# GA

The genetic algorithm library offers two things:

* A specialized `Entity` type
* A ga method

For a clear and thorough walk through of the everything GA refer to the "Runner and Our First Algorithm" chapter, where we build a GA.

To include this library in your project you can add the following to your list of library dependencies

`"net.cilib" %% "cilib-ga" % "@CILIB_VERSION@"`

## Individual

`Individual` is a type of `Entity`.
While `Entity` has the type parameters `[S, A]`, `Individual` plugs in `Double` to `A`.
Thus giving us the definition:

`type Individual[S] = Entity[S, Double]`

We could even take it a step further, like we did in "Creating a GA", a set the `S` (state) of the Individual to `Unit`.

`type Ind = Individual[Unit]`

## GA Method

The ga method represents a template genetic algorithm that we are able to customize based on the parameters passed.

### Parameters

```scala
p_c: Double //Crossover rate
parentSelection: NonEmptyList[Individual[S]] => RVar[List[Individual[S]]] //Function used to select the parents
crossover: List[Individual[S]] => RVar[List[Individual[S]]] //Function used to produce off spring
mutation: List[Individual[S]] => RVar[List[Individual[S]]] //Function used to mutate the offspring
```

### Return Type

`NonEmptyList[Individual[S]] => Individual[S] => Step[Double,List[Individual[S]]]`

It's worth mentioning that all algorithms will have the same return type.
This is so that they may be plugged into an iterator and subsequently an `Runner`.
But for documentation purposes it will be stated at every algorithm.
