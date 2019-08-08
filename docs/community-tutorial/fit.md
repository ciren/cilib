---
id: fit
title: Fitness
---

After the last chapter you're now somewhat familiar with `Feasible` and `Infeasible`.
`CILib` offers this and more for dealing with the fitness of solutions.
These concepts will be the main focus of this chapter,
as well as starting to put everything together of what we have learned thus far.

We are going to cover the following

- The `Fit` data type, which we have seen before
- The `Opt` data type, for optimization purposes
- The `Comparison` class and its companion object

## The Fit Type

In the past we have seen `Feasible` and `Infeasible`.
But, you might have noticed, there is a third type, `Adjusted`.
These types used to indicate information about a possible solution.

```scala
Feasible(v: Double)

Infeasible(v: Double, violations: Int)

Adjusted (original: Infeasible, adjust: Double)
```

And the purpose of this is to allow for pattern matching so that we may control the flow of logic.
For example

```tut:book:invisible
import cilib._
```
```tut:book:silent
def control (fit: Fit) : Double = {
    fit match {
        case Feasible(v) => v
        case Infeasible(_, _) => -1.0
        case Adjusted (_, _) => -1.0
    }
}
```

### Feasible

<div class="callout callout-info">
A data type to represent a feasible solution.
</div>

```tut:book
val f = Feasible.apply(5)
Feasible.unapply(f)

```

### Infeasible and Adjusted

<div class="callout callout-info">
Infeasible is a data type to represent an infeasible solution.
</div>

From the `Infeasible` constructor we know that it requires a `Double` for our solution, and an `Int`
to show the amount of `Constraints` violated.
`Infeasible` offers an `adjust` method, `adjust(f: Double => Double): Adjusted`, which will create
adjust the solution using the given parameter function.

<div class="callout callout-info">
Adjusted indactes we have adjusted an `Infeasible` solution. It contains the `Infeasible` we had to adjust and the new adjusted value.
</div>

```tut:book:invisible
import scalaz._
```
```tut:book
val badSolution = Infeasible(45.0, 1)
badSolution.adjust(x => x * 0.73)
```

With `Adjust` we are given a second chance at correcting an incorrect solution.

## Min and Max Optimization

Often we need to choose either the minimum or maximin solution out of our set of possible solutions.
For this, we have `Min` and `Max` which extends `Opt`.
The operate as one would expect.
They do offer a function, `objectiveOrder[A]: Order[Objective[A]]`, which when supplied with two
`Objectives` results in an `Order`.


## Comparison

Like the most of CILib, it's easy to deduce what a component does based on its name.
With `Comparison` we are going to be making... comparisons!
This is going to be a great addition to our arsenal of collected CILib components thus far.
Our `Comparison` object offers us several methods

```scala
dominance(o: Opt): Comparison
quality(o: Opt) = dominance(o)
compare[F[_],A](x: F[A], y: F[A])(implicit F: Fitness[F,A]): Comparison => F[A]
fitter[F[_],A](x: F[A], y: F[A])(implicit F: Fitness[F,A]): Comparison => Boolean
```

Through the use of these methods we will be able to make our programs more elaborate and start getting more optimum results.

First things first, `implicits`.
You might have noticed the `(implicit F: Fitness[F,A])` parameter.
Later on you'll see we need not to worry about this as the implicits needed for the types we will
be using are defined within CILib.
However, for this case and if you intend to use your own data types, we will
need to define the implicits ourselves.
We will be using `NonEmptyList`.

```tut:book:invisible
import cilib._
import scalaz._
import Scalaz._
```
```tut:book:silent
val rng = RNG.init(12L)
val cons = cilib.Equal(ConstraintFunction((l: NonEmptyList[Double]) => l.index(0).getOrElse(0.0)), 4.0)
val e = Eval.constrained[NonEmptyList,Double]((l: NonEmptyList[Double]) => l.suml, List(cons))

implicit def i = new Fitness[NonEmptyList, Double] {
    def fitness(l: NonEmptyList[Double]) = Option.apply(e.eval.run(rng)._2(l))
}
```

We want to calculate the sum of the list with the constraint that the first number needs to be 4.
With that out of the way we may begin exploring the `Comparison` methods.

### dominance

`dominance` creates an instance of a `Comparison` type, based on the supplied `Opt`, that we may use.

```tut:book:silent
val test1 = NonEmptyList(4.0, 5.0, 6.0)
val test2 = NonEmptyList(4.0, 2.0, 33.0)

val comparison = Comparison.dominance(Max)
```
```tut:book
comparison.apply(test1, test2)
```

So clearly `test2` is the better option, as we are calculating the sum of the list.
What if `test2's` first number isn't 4?
Well, then `test1` will be the better choice as `test2` will become infeasible.
Ah but what if both test1's and test2's first number is something other that 4?
Well, both lists will become infeasible, and the first parameter will be returned.

### quality

Does the same thing as `dominance`, just a different name.

### compare

With this we can produce a function, that will work in a similar way to `dominance`.
All we need to do is supply a `Comparison` instance.

```tut:book
Comparison.compare(test1, test2)
Comparison.compare(test1, test2).apply(comparison)
```

### fittest

Works in a similar fashion to `compare` but returns a function of `Comparison => Boolean`.
The function determines if the first parameter is fitter than the second.

```tut:book
Comparison.fitter(test2, test1).apply(comparison)
Comparison.fitter(test1, test2).apply(comparison)
```

And thats how we use the `Comparison` object!


## Exercises

These questions are going to test your knowledge of everything involved in fitness and will require you to use the knowledge you have acquired thus far.

The premise of these questions will focus around the following scenario.

You are an assassin in ancient japan tasked with taking out an evil general.
To do this you will need to build a custom bow and arrow.
You visit the weapon smith with 400 coins in your pocket.
You can choose the type of the wood used for the bow and the type of material used for the tip of the arrow.

For the bow, there are 5 different types/levels of woods. Type 5 being the best.

- The price of the wood is the wood number * 40
- Each increase in type of wood yields an additional 16 meters in distance. Type 1 wood will yield 16m, Type 2 32m and so on.

For the tip of the arrows, there are 10 types of materials to choose from. Type 10 being the best.

- The price of the material is calculated at 40 * the material number.
- Each increase in type of material yields an additional 90 points in damage. Type 1 material will yield 90 damage points, Type 2 180 and so on.
- However with each increase in material type it reduces the distance by 4m.
- 1 damage point takes away 1 health point

You know that the general's office is 38m from you hiding spot and that he is always wearing armor, giving him 520 health points.
You need to calculate the best custom bow and arrow for this problem, no AI is required, and return the solution in the following list format:

- `Distance, Damage, Bow Type, Arrow Type, Cost, Fitness`

You are given the following code to start with

```tut:book:silent
import cilib._
import scalaz._
import Scalaz._
import spire.implicits._
import spire.math.Interval

val coins = 400
val rng = RNG.fromTime

def getGenBowType: Double = Math.floor(Dist.uniform(Interval(1.0, 5.0)).eval(RNG.fromTime))
def getGenArrowType(remainingCoins: Double): Double = Math.floor(Dist.uniform(Interval(1.0, remainingCoins / 40)).eval(RNG.fromTime))

def getBowCost(bowType: Double): Double = Math.pow(2.0, bowType).toInt * 10.0
def getArrowCost(arrowType: Double): Double = arrowType * 40.0
def getTotalCost(bowType: Double, arrowType: Double): Double = getBowCost(bowType) + getArrowCost(arrowType)

def getBowDistance(bowType: Double, arrowType: Double): Double = (bowType * 16.0 ) - (arrowType * 4.0)
def getArrowDamage(arrowType: Double): Double = arrowType * 90.0
```

### Question 1
Create two methods with the following headers

```scala
generateValuesBasedOn(bowType: Double, arrowType: Double) : NonEmptyList[Double]

generateValues : NonEmptyList[Double]
```

These must return a `NonEmptyList[Double]` with the format `Distance, Damage, Bow Type, Arrow Type, Cost`

<div class="solution">

```tut:book:silent
def generateValuesBasedOn(bowType: Double, arrowType: Double) : NonEmptyList[Double] = {
    val bowCost = getBowCost(bowType)
    val arrowCost = getArrowCost(arrowType)
    val distance = getBowDistance(bowType, arrowType)
    val damage = getArrowDamage(arrowType)
    NonEmptyList(distance, damage, bowType, arrowType, arrowCost + bowCost)
}

def generateValues : NonEmptyList[Double] = {
    val bowType = getGenBowType
    val bowCost = getBowCost(bowType)
    val arrowType = getGenArrowType(coins - bowCost)
    val arrowCost = getArrowCost(arrowType)
    val distance = getBowDistance(bowType, arrowType)
    val damage = getArrowDamage(arrowType)
    NonEmptyList(distance, damage, bowType, arrowType, arrowCost + bowCost)
}
```
</div>

### Question 2
Implement a fitness method, `fitness (l: NonEmptyList[Double]): Double`, to be used in our evaluations.

<div class="solution">

```tut:book:silent
def fitness (l: NonEmptyList[Double]): Double = {
    val distanceRating = (l.index(0).getOrElse(0.0) / 38.0) - 1.0
    val damageRating = (l.index(1).getOrElse(0.0) / 520.0) - 1.0
    100 - (distanceRating + damageRating) * 100
}
```
</div>

### Question 3
Create the two `Constraints` from the information given, these will be used to guide our problem.

<div class="solution">

```tut:book:silent
val distanceConstraint = GreaterThanEqual(ConstraintFunction((l: NonEmptyList[Double]) => l.index(0).getOrElse(0)) , 38)
val damageConstraint = GreaterThanEqual(ConstraintFunction((l: NonEmptyList[Double]) => l.index(1).getOrElse(0)), 520)
```
</div>

### Question 4
Using the `fitness` method and `Constraints` that were created in the previous questions, create an instance of `Eval`

<div class="solution">

```tut:book:silent
val e = Eval.constrained[NonEmptyList, Double](fitness(_), List(damageConstraint, distanceConstraint))
```
</div>

### Question 5
Now that we have created the evaluation we will need some methods to extract the information from the the `Objective` result.
Keeping this in mind, implement the following methods:

```scala
getSolution (fit: Fit): Double

evalObjective (objective: Objective[Double]): Double

getViolations (objective: Objective[Double]): List[Constraint[Double]]
```

<div class="solution">

```tut:book:silent
def getSolution (fit: Fit): Double = {
    fit match {
        case Feasible(v) => v
        case Infeasible(_, _) => -1.0
        case Adjusted (_, _) => -1.0
    }
}

def evalObjective (objective: Objective[Double]): Double = {
    objective match {
        case Single(f, _) => getSolution(f)
        case Multi(_) => -1.0
    }
}

def getViolations (objective: Objective[Double]): List[Constraint[Double]] = {
    objective match {
        case Single(_, l) => l
        case Multi(_) => List()
    }
}
```
</div>

### Question 6
You should now be able to start generating possible solutions as well as evaluate them to get their fitness score.
More likely than not you will get an infeasible solution and low fitness score.
Instead of continuously generating new possible solutions let's adjust our current one.
create a method called `adjust` that will accept `values: NonEmptyList[Double]` and `violations: List[Constraint[Double]]`.
Based on the given `values` list make the appropriate changes keeping the `violations` in mind.

<div class="solution">

```tut:book:silent
def adjust (values: NonEmptyList[Double], violations: List[Constraint[Double]]): NonEmptyList[Double] = {
    val distanceConsViolated = violations.contains(distanceConstraint)
    val damageConsViolated = violations.contains(damageConstraint)

    val bowType = values.index(2).get
    val arrowType = values.index(3).get
    var result = values

    if (distanceConsViolated && damageConsViolated){
        result = generateValuesBasedOn(bowType + 1.0, arrowType + 1.0)
    }else if (distanceConsViolated){
        if (getTotalCost(bowType + 1.0, arrowType) > coins) result = generateValuesBasedOn(bowType + 1.0, arrowType - 1.0)
        else result = generateValuesBasedOn(bowType + 1.0, arrowType)
    }else if (damageConsViolated){
        if (getTotalCost(bowType, arrowType + 1.0) > coins) result = generateValuesBasedOn(bowType - 1.0, arrowType + 1.0)
        else result = generateValuesBasedOn(bowType, arrowType + 1.0)
    }
    result
}
```
</div>

### Question 7
Now all thats left is to get the most cost effective solution for our custom bow and arrow.
Implement a method called `getCustomWeapon` which will return a the details of the bow and arrow as well as the fitness score.

- `Distance, Damage, Bow Type, Arrow Type, Cost, Fitness`

<div class="solution">

```tut:book:silent
def getCustomWeapon : NonEmptyList[Double] = {
    var list = generateValues
    var objective = e.eval.run(rng)._2(list)
    var violations = getViolations(objective)
    list = list.append(NonEmptyList(evalObjective(objective)))
    while (violations.nonEmpty){
        list = adjust(list, violations)
        objective = e.eval.run(rng)._2(list)
        violations = getViolations(objective)
        list = list.append(NonEmptyList(evalObjective(objective)))

    }
    list
}
```
```tut:book
getCustomWeapon
```
</div>

### Question 8
Upon further inspection you realize you have 1000 coins.
You then decide to rethink your custom bow and arrow.
You come to the conclusion to the arrow should deal as much damage as possible.
Change the fitness method to return the amount of damage as the fitness score (also change the coins value to 1000).

<div class="solution">

```tut:book:silent
def fitness (l: NonEmptyList[Double]): Double = l.index(1).get
```
</div>

### Question 9
Create the method `highestDamageWeapon(n: Int): NonEmptyList[Double]` which will generate n amount of custom bows and arrows.
Return the one that does the highest amount of damage.

<div class="solution">

```tut:book:silent
implicit def i = new Fitness[NonEmptyList, Double] {
    def fitness(l: NonEmptyList[Double]) = Option.apply(e.eval.run(rng)._2(l))
}
def highestDamageWeapon(weapons: Int): NonEmptyList[Double] ={
    val comparison = Comparison.dominance(Max)
    var result = getCustomWeapon
    for (j <- 1 until weapons){
        result = comparison.apply(getCustomWeapon, result)

    }
    result
}
```
```tut:book
highestDamageWeapon(5)
```
</div>


## Summary

We learnt about some amazing functionality of CILib with regards to fitness.
We got to test our knowledge of CILIb thus far by figuring out how to build a deadly custom bow and arrow.
Its important to see how the various data types are starting to connect to one another.
And with the data types defined for us, we only need to implement the finer details specific to our problem.

<div class="callout callout-info">
They key take away points from this chapter are that

- By using pattern matching on `Feasible`, `Infeasible` and `Adjusted` we are able to control the logic as well as the validity of our solutions.
- We can define an optimization scheme, `Max` or `Min`.
- Using the optimization scheme, we can begin to compare solutions (preferably `Feasible`).

</div>
