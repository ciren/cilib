## Creating a GA

Isn't this exciting!

All our work over the past chapters is coming together!

It should be noted that the only thing we will be using from
`cilib-ga` is the type `Individual`, which, as we discussed before, is
an `Enity` with a state of type `Unit` since GA's do not need a state.

### Imports

We will be using the following `imports` for our GA.

```tut:book:silent
import cilib._
import cilib.ga._
import scalaz._
import Scalaz._
import eu.timepit.refined.auto._
import spire.implicits.{eu => _, _}
import spire.math.Interval
```

### The Problem Environment

For our problem we are going to attempt to find the greatest area
produced by a 2 dimensional rectangle. The sides can range from 0.1
to 12.

```tut:book
val bounds = Interval(0.1, 12.0)^2

val env = Environment(
    cmp = Comparison.dominance(Max),
    eval =  Eval.unconstrained[NonEmptyList,Double](_.foldLeft1(_ * _)).eval
)
```

### The GA Algorithm

The GA algorithm is the exact same one used in `cilib-ga`.

The reason we are intentionally defining it as opposed to importing it
is so that we can see the inner workings and learn from it.

```scala
def ga[S](
             p_c: Double,
             parentSelection: NonEmptyList[Individual[S]] => RVar[List[Individual[S]]],
             crossover: List[Individual[S]] => RVar[List[Individual[S]]],
             mutation: List[Individual[S]] => RVar[List[Individual[S]]]
         ): NonEmptyList[Individual[S]] => Individual[S] => Step[Double,List[Individual[S]]] =
    collection => x => for {
        parents   <- Step.pointR(parentSelection(collection))
        r         <- Step.pointR(Dist.stdUniform.map(_ < p_c))
        crossed   <- if (r) Step.pointR[Double,List[Individual[S]]](crossover(parents))
        else Step.point[Double,List[Individual[S]]](parents)
        mutated   <- Step.pointR[Double,List[Individual[S]]](mutation(crossed))
        evaluated <- mutated.traverseU(x => Step.eval((v: Position[Double]) => v)(x))
    } yield evaluated
```

Wow. There is a lot going on here.
Let's break it down.
First the parameters:

```scala
p_c //is our crossover rate
parentSelection //is the function we will use to select the parents
crossover //is the function that we will use to produce off spring
mutation //is the function we will use to mutate the offspring
```

All right. Not too bad. By being able to pass functions to a generic
GA method allows us to build a GA specific to our needs. It should
also be noted that all the passable functions yield the same data
type. Now before we move to the actual implementation of the `ga`
method, I would like to remind you that for comprehensions are
essentially chained `flatMaps`. This allows us to chain steps
together. So we know that this method will return a function of type

`NonEmptyList[Individual[S]] => Individual[S] => Step[Double,List[Individual[S]]]`

Which in simply means, that we are going to get a function that return
a `Step` representing a series of computations applied to every
`Individual` with a collection of `Individuals`. Okay, so now we know
what we are returning but let's see how we get to that by looking at
the "series of computations".

`parents <- Step.pointR(parentSelection(collection))`

Here we are selecting our parents from the collection using the
parentSelection function that was passed as a parameter. Lastly, it
will be wrapped in a `Step`.

`r <- Step.pointR(Dist.stdUniform.map(_ < p_c))`

Here we are assigning a `Boolean` wrapped in an `Step`.

```
crossed <-
  if (r) Step.pointR[Double,List[Individual[S]]](crossover(parents))
  else Step.point[Double,List[Individual[S]]](parents)
```

If r is `true` we will produce crossovers based on the `parents` using
the crossover function parameter, else we are going to just leave the
parents as is. How frequently the crossover occurs depends on your
`p_c`.

`mutated <- Step.pointR[Double,List[Individual[S]]](mutation(crossed))`

Nothing too complex here. We are simply mutating our ``crossed``
individuals using the mutation function parameter. Also note that
``mutated`` will have a data type very similar tto our return type.

`evaluated <- mutated.traverseU(x => Step.eval((v: Position[Double]) => v)(x))`

Lastly, we are taking every `Individual` from `mutated` and wrapping
each one in an evaluation `Step`. Remember that this is just a step
representing evaluation, not actually evaluating the `Individuals`
right now.

Let's get to creating the functions we will be passing to our ga method.

### Selection Method

For our selection method we will be creating a random selection
method. We know that it needs to have the type
`NonEmptyList[Individual[S]] => RVar[List[Individual[S]]]`. To make
this even easier to read we can include a custom type, `type Ind =
Individual[Unit]`, making it `NonEmptyList[Ind] =>
RVar[List[Individual[S]]]`. Create a function with the name
`randomSelection` that will randomly select two `Inds` from an non
empty list.

<div class="solution">

```tut:book:silent
type Ind = Individual[Unit]
```
```tut:book
val randomSelection: NonEmptyList[Ind] => RVar[List[Ind]] =
    (l: NonEmptyList[Ind]) => RVar.sample(2, l).getOrElse(List.empty[Ind])
```
</div>

### Selection Method

For our selection method we will be creating a random selection
method. We know that it needs to have the type
`NonEmptyList[Individual[S]] => RVar[List[Individual[S]]]`. To make
this even easier to read we can include a custom type, `type Ind =
Individual[Unit]`, making it `NonEmptyList[Ind] =>
RVar[List[Individual[S]]]`. Create a function with the name
`randomSelection` that will randomly select two `Inds` from an non
empty list.

<div class="solution">

```tut:book:silent
type Ind = Individual[Unit]
```
```tut:book
val randomSelection: NonEmptyList[Ind] => RVar[List[Ind]] =
    (l: NonEmptyList[Ind]) => RVar.sample(2, l).getOrElse(List.empty[Ind])
```
</div>

### Crossover Method

We will be creating a one point crossover method to be used in our GA.
It will need to have the type `List[Individual[S]] =>
RVar[List[Individual[S]]]` and let's call it `onePoint`. There should
be two `Individuals`, parents, in the list that will be used to create
two new `Individuals` from a one point cross over. Else we should
output an error.

<div class="solution">
```tut:book
def onePoint(xs: List[Ind]): RVar[List[Ind]] =
    xs match {
        case a :: b :: _ =>
            val point: RVar[Int] = Dist.uniformInt(Interval(0, a.pos.pos.size - 1))
            point.map(p => List(
                a.pos.take(p) ++ b.pos.drop(p),
                b.pos.take(p) ++ a.pos.drop(p)
            ).traverse(_.toNel.map(x => Entity((), Point(x, a.pos.boundary)))).getOrElse(List.empty[Ind]))
        case _ => sys.error("Incorrect number of parents")
    }
```
</div>

### Mutation Method

Our mutation method will be relatively simply. For every `Individual`
we want to mutate its position where each point in the position is
mutated based on a `Boolean`. If It is true, mutate the point by
multiplying by a random number from a gaussian distribution. Else
leave it as it is. This deciding `Boolean` is determined the same way
r is in our `ga` method. Except, that is compared with a double
parameter, `p_m`, representing the mutation rate. So you might need
groups of parameters. Call this method `mutation` and be aware of
it's return type. Also some helpful hints are to think about using
`Lenses` and the `ModifyF` function, as well as `traverse` for
sequencing through points ot a list.

<div class="solution">
```tut:book
def mutation(p_m: Double)(xs: List[Ind]): RVar[List[Ind]] =
    xs.traverse(x => {
        Lenses._position.modifyF((p: Position[Double]) => p.traverse(z => for {
            za <- Dist.stdUniform.map(_ < p_m)
            zb <- if (za) Dist.stdNormal.flatMap(Dist.gaussian(0,_)).map(_ * z) else RVar.point(z)
        } yield zb
        ))(x)
    })
```
</div>

### My GA

Now that we have defined all those methods we should be able to
construct our GA with the following line of code:

`val myGA = ga(0.7, randomSelection, onePoint, mutation(0.2))`

Great! Remember that what we have here is still just a function. One
that takes a collection of `Individuals` and returns a `Step`.

`NonEmptyList[Ind] => Ind => Step[Double,List[Ind]]`

### Creating a Collection

We have the `myGA` function, but not the collection. So let's go ahead and
create that. Keep in mind that we are dealing with `Individuals`

```tut:book:invisible
```tut:book
val swarm = Position.createCollection[Ind](x => Entity((), x))(bounds, 20)
```

### Creating an Iterator

Here is where things get a little more interesting. Remember the
`Iteration` section earlier in this chapter? Here is where we dive
into that. I'll first present the code and then we can go over it.

```scala
val iterator =
    Iteration.sync(myGA).map(_.suml)
      .flatMapK(r => Step.withCompare(o =>
        r.sortWith((x,y) => Comparison.fittest(x.pos,y.pos).apply(o)))
          .map(_.take(20).toNel.getOrElse(sys.error("error"))))
```

This code creates a synchronous iterator based on our ga that at each
generation select the best 20 based on our comparison dominance.
`Iteration.sync(myGA).map(_.suml)` allows us to work with a
`List[Ind]`. `r` is an `Ind` which becomes a `Step` with a
comparison.

### Running our GA

Lastly, running our program requires a `Runner`.

`println(Runner.repeat(1000, iterator, swarm).run(env).eval(RNG.init(12L)).toString)`

This line of code will produce the results of our ga after 1000
iterations as a string.
