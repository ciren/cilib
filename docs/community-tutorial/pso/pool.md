## Pool Object

The `Pool` object allows us to create instances of `Pools` which are non empty lists containing `PoolItems`.
To understand `Pools` we need to first see how `PoolItems` work.

### PoolItem Object

The `PoolItem` object allows us to create `PoolItems` which assign a score to some item.
The following code will demonstrate this.

```tut:book:silent
import cilib._
import cilib.pso._
import spire.implicits.{eu => _, _}
import spire.math.Interval

val interval = Interval(-5.12,5.12)^1
val rng = RNG.init(12)
```
```tut:book
val particle = Position.createPosition(interval).map(p => Entity(Mem(p, p.zeroed), p)).eval(rng)

val poolitem = PoolItem.apply(particle, 1.25)
```

Now that we have created a `PoolItem` we are able use the class methods.

```tut:book
poolitem.score // Will yield the score
poolitem.reward(0.54) // Will Modify the score 
poolitem.change(particle) // Change the item
```

## Creating Pools

The `Pool` object is very easy to use.
Nearly all of the methods take at least a `NonEmptyList[A]` which represent our items.
In these examples, for simplicity purposes, we will be using `Double` as our type.
Let's say we needed to create a pool where each item within the pool had the same score.
To do this we would use `mkPool`.

```tut:book:silent
import scalaz._
import Scalaz._
```
```tut:book
val doubles = RVar.doubles(5).eval(rng).toNel.get

val pool1 = Pool.mkPool(0.85, doubles)
```

Now each item in the pool has a score of `0.85`.
Similarly we could use `mkEvenPool` which will give each item a score based on the amount of items in the pool.

```tut:book
val pool2 = Pool.mkEvenPool(doubles)
```

Or `mkZeroPool` which will give each item a score of 0.

```tut:book
val pool2 = Pool.mkEvenPool(doubles)
```

The `mkPoolListScore` when provided with a `Pool` will turn the score of each item into a `List`.

```tut:book
Pool.mkPoolListScore(pool1)
```

We can also update a `Pool's` items by using `mkFromOldPool`.

```tut:book
val newDoubles = RVar.doubles(5).eval(RNG.init(12)).toNel.get
Pool.mkFromOldPool(pool1, newDoubles)
```

Lastly, we have the method `updateUserBehaviours`.

```scala
updateUserBehaviours[A, B](oldP: Pool[B], newP: Pool[B])(xs: NonEmptyList[User[A,B]]) 
```

As you can see, it makes use of a `User` type.
`User` is simply some user representation, such as `Particle` matched with a `PoolItem`.

`User[A, B](user: A, item: PoolItem[B])`

The purpose of this is to give updated behaviours.