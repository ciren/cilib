## Min and Max Optimization

Often we need to choose either the minimum or maximin solution out of our set of possible solutions.
For this, we have `Min` and `Max` which extends `Opt`.
The operate as one would expect.
They do offer a function, `objectiveOrder[A]: Order[Objective[A]]`, which when supplied with two
`Objectives` results in an `Order`.