## Step Companion Object

The companion object offers several methods that we may use to create
instances of `Steps`.

```scala
point[A,B](b: B): Step[A,B]

pointR[A,B](a: RVar[B]): Step[A,B]

eval[S,A:Numeric](f: Position[A] => Position[A])(entity: Entity[S,A]): Step[A,Entity S,A]

evalP[A](pos: Position[A]): Step[A,Position[A]]

withCompare[A,B](a: Comparison => B): Step[A,B]

withCompareR[A,B](f: Comparison => RVar[B]): Step[A,B]

evalF[A:Numeric](pos: Position[A]): Step[A,Position[A]]
```

We will be using the `Environment` and `RNG` we created at the
beginning of this chapter.

```
val rng = RNG.init(12)
val bounds = Interval(-5.12,5.12)^2
val env = Environment(
    cmp = Comparison.dominance(Min),
    eval = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
)
```

### point

Returns an instance of `Step` based on the given parameter.

```tut:book:invisible
import cilib._
import spire.implicits._
import spire.math._
import scalaz._
import Scalaz._
val rng = RNG.init(12)
val bounds = Interval(-5.12,5.12)^2
val env = Environment(
    cmp = Comparison.dominance(Min),
    eval = Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
)
```
```tut:book:silent
val particle = Position.createPosition(bounds).map(p => Entity(Mem(p, p.zeroed), p)).eval(rng)
```
```tut:book
Step.point(particle)
```

### pointR

Creates `Step` contained in `RVar`.

```tut:book:silent
val particle = Position.createPosition(bounds).map(p => Entity(Mem(p, p.zeroed), p))
```
```tut:book
Step.pointR(particle).run(env).eval(rng)
```

### eval

`eval` is used for evaluating `Entities`. This function produces a
`Step` which may be `run` using a function, `Environment => RVar[A]`.

```tut:book
val particle = Position.createPosition(bounds).map(p => Entity(Mem(p, p.zeroed), p)).eval(rng)
def explore (position: Position[Double]): Position[Double] = position.map(x => x * 0.73)

Step.eval(explore)(particle)
```

```tut:book:silent
Step.eval(explore)(particle).run(env).run(rng)
```

As you can see from the above code, when our `Entity` was evaluated
it's `Point` changed to a `Solution`. However its `state` remained
the same as that is up to us as to how we update it.

### withCompare

An example of use would be comparing the current position of an
`Entity` with its best, and then returning a new `Entity` based on
the comparison.

```tut:book:silent
import monocle._, Monocle._

val particle = Position.createPosition(bounds).map(p => Entity(Mem(p, p.zeroed), p)).eval(rng)

def updatePBest[S](p: Entity[S,Double])(implicit M: HasMemory[S,Double]): Step[Double, Entity[S,Double]] = {
    val pbestL = M._memory
    Step.withCompare(Comparison.compare(p.pos, (p.state applyLens pbestL).get)).map(x =>
        Entity(p.state applyLens pbestL set x, p.pos))
}
```

```tut:book
updatePBest(particle).run(env).eval(rng)
```

### withCompareR

An example of use would be determining which un-evaluated
`RVar[Entity]s` is fitter.

```tut:book:silent
def better[S,A](a: Entity[S,A], b: Entity[S,A]): Step[A,Boolean] =
    Step.withCompareR(comp => RVar.point(Comparison.fitter(a.pos, b.pos).apply(comp)))

val particle1 = Position.createPosition(bounds).map(p => Entity(Mem(p, p.zeroed), p)).eval(rng)
val particle2 = Position.createPosition(bounds).map(p => Entity(Mem(p, p.zeroed), p)).eval(RNG.fromTime)
```

```tut:book
better(particle1, particle2).run(env).eval(rng)
```

### evalF

`evalF` will take a `Position` an returns a `Step` that represents the
evaluation of the position. This is actually used in the `Step.eval`
method.
