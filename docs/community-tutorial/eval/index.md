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