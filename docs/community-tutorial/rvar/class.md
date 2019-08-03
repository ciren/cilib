## RVar Class

Now that we have some introductory knowledge as to how `RVar` 
works we should distinguish between the RVar class and its companion object. 
The easiest way to do this is to look at the the methods that each offer. 

In this section, as is the theme of this book, 
I will cover each method to help you understand functionally the `RVar` class has to offer. 
This will also serve as documentation to the class. At a quick glance we will be covering 

```scala
run(initial: RNG): (RNG, A)

exec(s: RNG): RNG

eval(s: RNG): A

map[B](f: A => B): RVar[B]

flatMap[B](f: A => RVar[B]): RVar[B]
```

However, it's important to note that we are only able to instantiate `RVars` through the companion object. 
Alright, let's get started. 


### run

We have seen `run` before in our introductory example. 
It returns the a `tuple` containing the `RNG` used as well as the resulting random value/s of type A. 

<div class="callout callout-info">
It should be noted that when we call `run`, or any method that uses `run`, on the same `RVar` with the same `RNG` it will produce the same result.

```tut:book:invisible
import cilib._
```
```tut:book:silent
val rng = RNG.fromTime
val doubles = RVar.doubles(2)
```
```tut:book
val doubleResult = doubles.run(rng)._2
val doubleResult2 = doubles.run(rng)._2 
doubleResult == doubleResult2
```
</div> 

### exec

`exec` performs `run` and only returns the `RNG` used. 

<div class="callout callout-warning">
Note that the `RNG` returned is not our original, but rather a new `RNG` that represents the next state of the original after being used.
If we were to use the returned `RNG` on the same `RVar` it would result in new random numbers.

```tut:book
val (newRNG, x) = doubles.run(rng)
doubles.run(newRNG)
newRNG == rng
```
</div>

### eval

`eval` performs `run` and only returns the resulting random value/s of type A.

### map

When we use `map`, we are simply changing the values in the context (RVar).
An example of such a use would be to multiply each number by a factor.

```tut:book
doubles.eval(rng)
doubles.map(x => x map(_ * 0.2)).eval(rng)
```

In this case, `x` is our `List[Double]` and we are saying for every `Double` in the list, multiply it by 0.2.

### flatMap

When we use `flatMap`, we are changing the values and the context.
Lets say we had a `RVar` of type `List[Double]` and we wanted a new `RVar` that contained a `List` with elements 
from the original as well as each element multiplied by 0.2.

```tut:book
doubles.eval(rng)
doubles.flatMap(x => RVar.point(x.flatMap(el => List(el, el * 0.2)))).eval(rng)
```

We now have our original values along side our newly mutated values. 