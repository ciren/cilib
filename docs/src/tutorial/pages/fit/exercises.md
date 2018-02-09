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