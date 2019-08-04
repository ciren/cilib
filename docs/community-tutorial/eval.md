# Eval

In the last chapter we saw how to test if a set of numbers satisfies a `Constraint` or a violates `Constraints`.
We now are going to look at how to unconstrained or constrained evaluations in CILib.
`Eval` useful in the sense that it provides a platform to determine whether a set of values are a feasible or
infeasible solution and how to handle them respectively.

```scala
unconstrained[F[_]: Input, A](f: F[A] => Double): Eval[A]

constrained[F[_]: Input, A](f: F[A] => Double, cs: List[Constraint[A, Double]]): Eval[A]
```

The above a methods belong to the `Eval` object with each creating their own version of an `Eval` instance.
Although they produce slightly different `Evals`, let's look at what `Eval` has to offer.

```scala
eval(a: NonEmptyList[A]): RVar[Objective[A]]

constrainBy(cs: List[Constraint[A,Double]]): Eval[A]

unconstrain: Eval[A]
```

The methods for constructing `Evals` also share some similarities. First, the type parameters, `[F[_]: Input, A]`.
`[F[_]: Input` simply means that we need a monad, in this case a `NonEmptyList`, of type `A`.
Secondly, in both methods, we pass a function that takes our monad and produces a `Double` (`f: F[A] => Double`).


## Unconstrained

<div class="callout callout-info">
An unconstrained evaluation applies a function that takes a monad and produces a double in a contained environment.
Every solution is feasible since there are not constraints.
</div>

```tut:book:invisible
import cilib._
import scalaz._
import Scalaz._
```
```tut:book
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml)
```

Here, we are creating an unconstrained evaluation instance that produces the sum of a non-empty list where each element is squared.
With our newly created instance we can use the following methods.

### eval

Will return a function that produces an `Objective`, either `Single` or `Multi` with a `Feasible` solution as there is no constraints.
This function is wrapped in an `RVar`. So in oder to *extract* the function from within the `RVar` we can use `run`.
Finally, we may use our function with other given list.

```tut:book:silent
val l = NonEmptyList(20.0, 4.0, 5.0)
```
```tut:book
e.eval
e.eval.run(RNG.fromTime)
e.eval.run(RNG.fromTime)._2
e.eval.run(RNG.fromTime)._2.apply(l)
```

### constrainBy

Will return a new instance of a constrained `Eval` based on the parameter provided and the existing `Eval's` *evaluation* function.

### unconstrain

This will return itself.

## Constrained

<div class="callout callout-info">
An unconstrained evaluation applies a function that takes a monad and produces a double in a contained environment.
The feasibility of every solution is determined by the constraints.
</div>

```tut:book:invisible
import cilib._
import scalaz._
import Scalaz._
```
```tut:book:silent
val lesThanCons = LessThan(ConstraintFunction((l: NonEmptyList[Double]) => l.suml), 12.0)
```
```tut:book
val e = Eval.constrained[NonEmptyList,Double](_.map(x => x * x).suml, List(lesThanCons))
```
### eval

Performs the same function as an unconstrained.
However, our result may either be feasible or infeasible.
If it is infeasible, you will notice that the result contains a violation count as well a `List` of the violated `Constraints`.

```tut:book:silent
var l = NonEmptyList(20.0, 4.0, 5.0)
```
```tut:book
e.eval.run(RNG.fromTime)._2.apply(l)
```
```tut:book:silent
l = NonEmptyList(1.0, 4.0, 5.0)
```
```tut:book
e.eval.run(RNG.fromTime)._2.apply(l)
```

### constrainBy

Will return a new instance of a constrained `Eval` based on the parameter provided and the existing `Eval's` *evaluation* function.
Note that the existing `Constraints` are not carried over, they are replaced by the parameter.

### unconstrain

Will return a new instance of a unconstrained `Eval` based on the existing `Eval's` *evaluation* function.


## How Would We Use Eval?

Okay, so we know what `Eval` *is*, but what can we do with it?
Say there exists a problem called problem X.
It requires 4 numbers.
Where all numbers need to be in the interval of 0 and 50.
However, there are some constraints.

1. The third number needs to be greater than 20.
2. Our fitness function can't yield a result greater than 5.

Our fitness function will add the first three numbers, and divide the result by the fourth.
Pretty simple. So far we have..

```tut:book:invisible
import cilib._
import scalaz._
import Scalaz._
```
```tut:book:silent
def fitness (values: NonEmptyList[Double]) : Double = {
    var result = values.index(0).getOrElse(0.0)
    result += values.index(1).getOrElse(0.0)
    result += values.index(2).getOrElse(0.0)
    result /= values.index(3).getOrElse(0.0)
    result
}
val cons1 = GreaterThan(ConstraintFunction((l: NonEmptyList[Double]) => l.index(3).getOrElse(0.0)), 20.0)
val cons2 = LessThanEqual(ConstraintFunction((l: NonEmptyList[Double]) => fitness(l)), 5.0)

val e = Eval.constrained[NonEmptyList,Double](fitness(_), List( cons1, cons2))
```

Cool. We know that when we use an `Eval` it will return an `Objective`, *the objective of a function*.
Since we know the `Objective` is going to be `Single` we should focus our attention there.
A `Single` will contain a `Fit` type, either `Feasible` or `Infeasible`, as well as a `List` of
any `Constraints` that were violated.

Because of all the class types being used we are able to use pattern matching in order to control our
program logic.

```tut:book:silent
def getSolution (fit: Fit) : Double = {
    fit match {
        case Feasible(v) => v
        case Infeasible(_, _) => -1.0
        case Adjusted (_, _) => -1.0
    }
}

def evalObjective (objective: Objective[Double]) : Double = {
    objective match {
        case Single(f, _) => getSolution(f)
        case Multi(_) => -1.0
    }
}
```

As you can see, we are using -1.0 to indicate an infeasible solution.

Now we can put everything together and test our code.

```tut:book:invisible
import spire.implicits._
import spire.math.Interval
```
```tut:book:silent
val rng = RNG.init(12L)
val interval = Interval(0.0, 50.0)
val l = Dist.uniform(interval).replicateM(4).eval(rng).toNel.getOrElse(NonEmptyList(0.0))
```
```tut:book
evalObjective(e.eval.run(rng)._2(l))
```
`.toNel.getOrElse(NonEmptyList(0.0))` is converting the returned `List[Double]` from our `RVar` evaluation to `NonEmptyList[Double]`.


## Summary

In this chapter we got to explore the useful concept of `Eval` and how we would use it in a very basic example.
Seeing as there are no exercises for this chapter, I encourage you to modify the example or come up with your own use of `Eval`.
This is so that you can solidify your understanding of `Eval` as will be making use of it in the coming chapters.

<div class="callout callout-info">
`Eval` is a type that allows us to evaluate a `NonEmptyList` of numbers using a
given function, while optionally applying a given `List` of `Constraints`.

To do this we use the `eval` method. We could either get a `Single` or `Multi Objective` as a result.
Where `Single` could contain a `Feasible` or `Infeasible` solution with a `List` of `Constraints` violated
(this list will be empty if it's a `Feasible` solution).
`Multi` will contain a `List` of `Single Objectives`.

Because of the types returned we are able to use pattern matching to account for every situation and control
what the flow of the program logic.
</div>

In the following chapter we will look at the concept that we briefly touched upon, `Fit`.
