# RVar

`RVar` (random value) is the backbone of CILib. 
Being a monad instance it allows for a large amount of composition, but more importantly it allows for the tracking of randomness within the `RVar` computation.
This tracking is of the utmost importance within computational intelligence algorithms, as randomness needs to be controlled in a manner that facilitates repetition.
In other words, even if a computation uses randomness, given the same inputs, the same results are expected.

<div class="callout callout-info">
An instance of `RVar` represents a computation that, when executed, results in a value with randomness applied.
</div>

Let's first try to understand what **RVar** is and then we can use it in practical examples.