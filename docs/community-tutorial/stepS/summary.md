## Summary

Again. Not that scary.
It really depends on what you decide to use as your state.

<div class="callout callout-info">
A *state-ful* computation is something that takes some state and returns a value along with some new state.
`StepS` represents a `Step` with a step.
When we `run` a `StepS` we need to supply an initial state value.
The type of the initial state value is determined by the second type parameter of the `StepS`.
</div>

In the next chapter we will gain some practical experience with `Step` as well as a bit of `StepS`