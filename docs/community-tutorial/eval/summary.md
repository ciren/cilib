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