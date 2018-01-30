# Objective

Computational intelligence research requires an objective.
Either it's a single objective or a collection of objectives.
CILib offers just this.
`Objective` is a data type that mimics this real world aspect.
We will often see methods in CILib return an `Objective`.
The two classes of `Objective` that we may encounter are

```scala
Single[A](f: Fit, v: List[Constraint[A, Double]]) extends Objective[A]

Multi[A](x: List[Single[A]]) extends Objective[A]
```

## Single

`Single` contains a `Fit` data type, which represents how fit a solution is (don't worry, we will explore this soon enough).
And a `List` of `Constraints`, which we have had experience with.
These `Constraints` are ones that were violated when determining the fitness of a solution.

## Multi

`Multi` is a collection of `Singles`.
We will see this data type come into play when we start exploring CILib's `MOO` section.

## Summary

That's it.
If the purpose of `Objective` is still a bit unclear
then don't worry as we are going to be seeing it used a lot in examples in the coming chapters.
Often we'll see `Objectives` returned and we will need to figure out what we do with the information for each case.
