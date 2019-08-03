## Lenses Object

CILib offers a `Lense` object that contains a few optics that we can readily use.
These optics are not just for `Entities` but also some CILib types we have seen before (yaaay!)
With these optics, we are condensing some of the functions we created earlier in to a single line while at the same time adding more functionality (double yaaay!)

All optics within the `Lense` object are prefixed with an underscore to signify that they are indeed an optic.

```scala
_state[S,A]

_position[S,A]

_vector[A:scalaz.Equal]

_solutionPrism[A]: Prism[Position[A],Solution[A]]

_objectiveLens[A]: Lens[Solution[A],Objective[A]]

_singleObjective[A]: Prism[Objective[A],Single[A]]

_multiObjective[A]: Prism[Objective[A],Multi[A]]

_singleFit[A]: Lens[Single[A],Fit]

_singleFitness[A]: Optional[Position[A], Fit]

_feasible: Prism[Fit,Double]
```

### _state

Will provide a `Lens` that we may use to *zoom* in on the state of an `Entity`.

```tut:book:invisible
import cilib.{Lenses, _}
import spire.implicits.{eu => _, _}

import scalaz._
import Scalaz._
import spire.math._
```
```tut:book:silent
val rng = RNG.init(12)
val interval = Interval(-5.12,5.12)^3
val particle = Position.createPosition(interval).map(p => Entity(Mem(p, p.zeroed), p))
```
```tut:book
val p = particle.eval(rng)
Lenses._state.get(p)
```

### _position

Will provide a `Lens` that we may use to *zoom* in on the position of an `Entity`.

```tut:book
Lenses._position.get(p)
```

### _vector

Returns the actual position within a `Position` instance.
As of now you have to declare the type being used in the lense.

```tut:book:silent
val x = cilib.Point[Int](NonEmptyList(2, 4), NonEmptyList(Interval(-5.12, 5.12)))
```
```tut:book
Lenses._vector[Int].get(x)
```

This lense will not work for `Doubles`.

### _solutionPrism

Will provide a `Prism`.
If the `Position` is a `Solution` is will be returned in `Some`.
Else it's a `Point` and `None` will be returned.

```tut:book:silent
val rng = RNG.init(12)
val interval = Interval(-5.12,5.12)^3
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
val pos = Position.eval(e, Position.createPosition(interval).eval(rng)).eval(rng)
```
```tut:book
val solution = Lenses._solutionPrism.getOption(pos).get
```

### _objectiveLens

Provides a `Lens` that *zooms* in on the `Objective` of a `Solution`

```tut:book:silent
val rng = RNG.init(12)
val interval = Interval(-5.12,5.12)^3
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
val pos = Position.eval(e, Position.createPosition(interval).eval(rng)).eval(rng)
val solution = Lenses._solutionPrism.getOption(pos).get
```
```tut:book
val objective = Lenses._objectiveLens.get(solution)
```

### _singleObjective

Provides a `Prism`.
If the `Object` is a `Single` is will be returned in `Some`.
Else it's a `Multi` and `None` will be returned.

```tut:book:silent
val rng = RNG.init(12)
val interval = Interval(-5.12,5.12)^3
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
val pos = Position.eval(e, Position.createPosition(interval).eval(rng)).eval(rng)
val solution = Lenses._solutionPrism.getOption(pos).get
val objective = Lenses._objectiveLens.get(solution)
```
```tut:book
val single = Lenses._singleObjective.getOption(objective).get
```

### _multiObjective

Works like `_singleObjective` but in favour of the `Multi` type.


### _singleFit

Provides a `Lens` that *zooms* in on the `Fit` of a `Objective`

```tut:book:silent
val rng = RNG.init(12)
val interval = Interval(-5.12,5.12)^3
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
val pos = Position.eval(e, Position.createPosition(interval).eval(rng)).eval(rng)
val solution = Lenses._solutionPrism.getOption(pos).get
val objective = Lenses._objectiveLens.get(solution)
val single = Lenses._singleObjective.getOption(objective).get
```
```tut:book
val fit = Lenses._singleFit.get(single)
```

### _singleFitness

Will provide a `Prism`.
If the `Position` is a `Solution` its fitness (`Fit`) will be returned in `Some`.
Else it's a `Point` and `None` will be returned.

```tut:book:silent
val rng = RNG.init(12)
val interval = Interval(-5.12,5.12)^3
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
val pos = Position.eval(e, Position.createPosition(interval).eval(rng)).eval(rng)
```
```tut:book
Lenses._singleFitness.getOption(pos)
```

### _feasible

Will provide a `Prism`.
If the `Fit` is a `Feasible` its fitness will be returned in `Some`.
Else it's a `Infeasible` and `None` will be returned.

```tut:book:silent
val rng = RNG.init(12)
val interval = Interval(-5.12,5.12)^3
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
val pos = Position.eval(e, Position.createPosition(interval).eval(rng)).eval(rng)
val solution = Lenses._solutionPrism.getOption(pos).get
val objective = Lenses._objectiveLens.get(solution)
val single = Lenses._singleObjective.getOption(objective).get
val fit = Lenses._singleFit.get(single)
```
```tut:book
Lenses._feasible.getOption(fit)
```