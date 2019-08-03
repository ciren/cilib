## Step Class

`Step` is has a very simple class definition. It takes a single
parameter, that being a function of type `Environment[A] => RVar[B]`.
We see this member function used we we call `run` of a `Step`.

```
final case class Step[A,B] private (run: Environment[A] => RVar[B])
```

Not complicated at all. And because of its monadic nature we have
the following functions at our disposal.

```scala
map[C](f: B => C): Step[A,C]

flatMap[C](f: B => Step[A,C]): Step[A,C]
```

We will be using the `Step` we created at the beginning of this
chapter, `myStep`.

### map

Here we changing the context of the `Step`.

```tut:book:invisible
import cilib._
import spire.implicits._
import spire.math._
import scalaz._
import Scalaz._

val bounds = Interval(-5.12,5.12)^2

val env = Environment(
    cmp = Comparison.dominance(Min),
    eval =  Eval.unconstrained[NonEmptyList,Double](_.map(x => x * x).suml).eval
)

val rng = RNG.init(12)
val particle = Position.createPosition(bounds).map(p => Entity(Mem(p, p.zeroed), p)).eval(rng)
def explore (position: Position[Double]): Position[Double] = position.map(x => x * 0.73)
val myStep = Step.eval(explore)(particle)
```
```tut:book
myStep.map(entity => Lenses._position.get(entity)).run(env).eval(rng)
```

In this example we changed it from

- a step in where the particle's position is updated and evaluated to
  return a new `Entity`
- to a step in where the particle's position is updated and evaluated
  to return the resulting `Position`.

### flatMap

Here we changing the context. In this example, We are passing an
`Entity[A] => Step[A, C]`, thus producing a new `Step[A, C]` which
will differ from our original `Step[A, B]`. What will happen here is
that we adding an extra *step*.


```tut:book:silent
val rng = RNG.init(12)
def explore (position: Position[Double]): Position[Double] = position.map(x => x * 0.73)
val particle = Position.createPosition(bounds).map(p => Entity(Mem(p, p.zeroed), p)).eval(rng)
val myStep = Step.eval(explore)(particle)

def negate (position: Position[Double]): Position[Double] = position.map(x => x * -1)
```
```tut:book
myStep.flatMap(entity => Step.eval(negate)(particle)).run(env).eval(rng)
```

So now our `Step` represents a step in an algorithm where a position
is multiplied by 0.73 and then negated. This step might not serve any
real world purpose but it demonstrates how we may chain `Steps`
together to form an algorithm. This is easily achieved by using for
comprehensions. For example, take a look at the following method.

```tut:book
def algorithm(entity: Entity[Mem[Double], Double]) = (for {
    step1 <- Step.eval(explore)(entity)
    step2 <- Step.eval(negate)(entity)
} yield step2).map(entity => Lenses._position.get(entity))

algorithm(particle).run(env).eval(rng)
```
