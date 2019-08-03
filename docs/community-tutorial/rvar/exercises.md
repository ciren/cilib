## Exercises

### Question 1
Define a function that accepts a parameter of type `Int`, called n, that returns `RVar[List[Double]]`,
where the `List[Double]` is of size n.

<div class="solution">

```tut:book:invisible
import cilib._
```
```tut:book:silent
def genList(n: Int): RVar[List[Double]] = RVar.doubles(n)
```
</div>

### Question 2
Modify your function to include two additional parameters of type `Double`,
which represent the lower and upper bounds of numbers we want to generate.

<div class="solution">

```tut:book:invisible
val rng = RNG.fromTime
```
```tut:book:silent
import scalaz._
import Scalaz._
import spire.implicits._
import spire.math.Interval

def genList(n: Int, lower: Double, upper: Double): RVar[List[Double]] = {
    val interval = Interval(lower, upper)
    Dist.uniform(interval).replicateM(n)
}
```
```tut:book
genList(5, 0.2, 0.5).run(rng)
```
</div>

### Question 3
Define a second function that accepts a parameter of type `List[Double]`, called randomList.
The function returns 3 randomly selected values from randomList, along with each selected value multiplied by a factor of 0.15.
The type of the returned value should be `RVar[List[Double]]`.

<div class="solution">

```tut:book:silent
def select(randomList: NonEmptyList[Double]): RVar[List[Double]] = {
    RVar.sample(3, randomList).getOrElse(List(0.0))
        .flatMap(x => RVar.point(x.flatMap(el => List(el, el * 0.15))))
}
```
</div>

### Question 4
Define a third function that accepts a parameter of type `Int`, called iterations, and returns a value of `List[Double]`.
The function should run from 0 to iterations.
At each iteration select the highest value from 5 random numbers and add it to a `List`.
This list should be returned.

<div class="solution">

```tut:book:silent
import scala.collection.mutable.ListBuffer
def getMax(iterations: Int): List[Double] = {
    var rng = RNG.fromTime
    var result = ListBuffer[Double]()
    for (count <- 0 to iterations){
        val (newRNG, randomValues) = RVar.doubles(5).run(rng)
        result = result += randomValues.max
        rng = newRNG
    }
    result.toList
}
```
</div>

### Question 5
Write the necessary statements that will produce a `RVar[List[Double]]` where each element is determined by a `Boolean` value.
If the `Boolean` is `true` the element should be in the range of 20 to 75.
And if the `Boolean` is `false` the element should be -1.
The `List ` shoould be of size 5.

<div class="solution">

```tut:book:silent
val composition = for {
    value <- Dist.uniform(Interval(20.0, 75.0))
    check <- RVar.next[Boolean] 
} yield if (check) value else -1.0

composition.replicateM(5)
```
</div>