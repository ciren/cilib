## Summary

We now know how to implement constraints on our lists.
In the next chapter we will see how `Constraints` are used further in CILib.

<div class="callout callout-info">
All `Constraint` classes make use of two parameters

- The constraint function that will compute a result
- An expected or appropriate value to compare against the result

This defines a constraint context that can used on `NonEmptyLists` through the companion object.
</div>