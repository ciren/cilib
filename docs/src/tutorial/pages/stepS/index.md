# StepS

At this point, we now understand what `Step` is.
`StepS` is very similar but now we are creating a step with a state.

What is a state?
Lets first at look functions to answer this.
A *state-ful* computation is something that takes some state and returns a value along with some new state.
In a way we could look at `RVar` as state-ful computation as we supply at state, the `RNG`, and when it is run a value is returned along with a new state.

```
s -> (a, s)
```

Thus, `StepS` represents a `Step` with a step.
Now since we haven't come across any `StepS` instances before this, we will first look at what a `StepS` is made up off.