## Summary

In this chapter we learnt what is `RVar`, how it's used and where it's used.
Understanding this makes understanding and using CILib that much easier.

<div class="callout callout-info">
`RVar` is a monadic structure of type `A` that when used with a pseudo-random number generator, such as `RNG`,
results in random a `List` values of type `A`. This is done at run time.
</div>

<div class="callout callout-info">
The `RVar` companion object provides several ways that allow use to create instances of `RVar` (the class).
</div> 

<div class="callout callout-info">
The `Dist` object allows us to use distributions, in a natural way, that result in `RVars`.
</div> 