---
id: pso
title: PSO
---

The particle swarm optimization library of CILib is one of the most extensive libraries.
While offering numerous pso components and algorithms it's best to begin with the basics of the library and then progressively work from there.

To include this library in your project you can add the following to your list of library dependencies

`"net.cilib" %% "cilib-pso" % "@CILIB_VERSION@"`

There are also several examples of implementations of PSOs in the example package. You can view the example package [here][example-package].


## Package

The package object has 3 valuable types specific to pso.
These being:

- `Particle`
- `Guide`
- `Pool`

### Particle

`Particle` is simply another name for `Entity`.

`type Particle[S,A] = Entity[S,A]`

### Guide

The `Guide` represents guides that we have seen in pso algorithms to guide particles.

`type Guide[S,A] = (NonEmptyList[Particle[S,A]], Particle[S,A]) => Step[A,Position[A]]`

As use can see `Guide` features a type of a method that takes a collection of particles as well a single particle that will result in a `Step`.
in short, a `Guide` is a selection followed by a comparison, wrapped up in a `Step`.


### Pool

The `Pool` represents a pool.
This type is a non empty list of `PoolItems`, which we will see shortly.

`type Pool[A] = NonEmptyList[PoolItem[A]]`


## Guide Object

The `Guide` object is a companion object to the `Guide`.
It offers us the following methods to produce guides:

### Identity

```scala
identity[S,F[_],A]: Guide[S,A]
```

### Personal best

```scala
pbest[S,A](implicit M: HasMemory[S,A]): Guide[S,A]
```

### Nearest best

```scala
nbest[S](neighbourhood: IndexSelection[Particle[S,Double]])(implicit M: HasMemory[S,Double]): Guide[S,Double]
```

### Dominance

```scala
dominance[S](selection: IndexSelection[Particle[S,Double]]): Guide[S,Double]
```

### Global best

```scala
gbest[S](implicit M: HasMemory[S,Double]): Guide[S,Double]
```

### Local best

```scala
lbest[S](n: Int)(implicit M: HasMemory[S,Double]): Guide[S,Double]
```

### Von Neumann

```scala
vonNeumann[S](implicit M: HasMemory[S,Double]): Guide[S,Double]
```

### Crossover

```scala
crossover[S](parentAttractors: NonEmptyList[Position[Double]], op: Crossover[Double]): Guide[S,Double]
```

### Nonlinear Model Predictive Control

```scala
nmpc[S](prob: Double): Guide[S,Double]
```

### Parent-Centric Crossover

```scala
pcx[S](s1: Double, s2: Double)(implicit M: HasMemory[S,Double]): Guide[S,Double]
```

### Unimodal Normally Distributed Crossover

```scala
undx[S](s1: Double, s2: Double)(implicit M: HasMemory[S,Double]): Guide[S,Double]
```

## Pool Object

The `Pool` object allows us to create instances of `Pools` which are non empty lists containing `PoolItems`.
To understand `Pools` we need to first see how `PoolItems` work.

### PoolItem Object

The `PoolItem` object allows us to create `PoolItems` which assign a score to some item.
The following code will demonstrate this.

```scala :silent
import cilib._
import cilib.pso._
import spire.implicits.{eu => _, _}
import spire.math.Interval

val interval = Interval(-5.12,5.12)^1
val rng = RNG.init(12)
```
```scala
val particle = Position.createPosition(interval).map(p => Entity(Mem(p, p.zeroed), p)).eval(rng)

val poolitem = PoolItem.apply(particle, 1.25)
```

Now that we have created a `PoolItem` we are able use the class methods.

```scala
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

```scala :silent
import scalaz._
import Scalaz._
```
```scala
val doubles = RVar.doubles(5).eval(rng).toNel.get

val pool1 = Pool.mkPool(0.85, doubles)
```

Now each item in the pool has a score of `0.85`.
Similarly we could use `mkEvenPool` which will give each item a score based on the amount of items in the pool.

```scala
val pool2 = Pool.mkEvenPool(doubles)
```

Or `mkZeroPool` which will give each item a score of 0.

```scala
val pool2 = Pool.mkEvenPool(doubles)
```

The `mkPoolListScore` when provided with a `Pool` will turn the score of each item into a `List`.

```scala
Pool.mkPoolListScore(pool1)
```

We can also update a `Pool's` items by using `mkFromOldPool`.

```scala
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


## Heterogeneous

So far we have seen a lot from the pso package.
At this point it has mostly been centered around learning the basics of the package.
From here on out we will be looking at all the related methods to build pso algorithms as well as the algorithms themselves.
However, these sections will feel more like documentation as they are methods based on existing pso knowledge and not anything specific to CILib.

### Types

There are several predefined types that are used in the `Heterogeneous` object that represent components we would see in heterogeneous PSOs.

```scala
type Behaviour[S, A, B] = NonEmptyList[Entity[S, A]] => Entity[S, A] => StepS[A, B, Entity[S, A]]

type SI[S, A, B] = StepS[A, S, B]

type HEntity[S, A, B] = User[Entity[S, A], B]

type HEntityB[S, A, B] = HEntity[S, A, Behaviour[S, A, B]]
```

### Helper Functions

#### updateStagnation

```scala
updateStagnation[S, A](p: Entity[S,A])(implicit M: HasMemory[S,A], S: HasPBestStagnation[S]): Step[A, Entity[S,A]]
```

#### assignRandom

Creates a population with behaviours.

```scala
assignRandom[A, B, C](implicit M: MonadState[StepS[C,Pool[B],?], Pool[B]]): NonEmptyList[A] => StepS[C,Pool[B],NonEmptyList[User[A, B]]]
```

#### pbestStagnated

```scala
pbestStagnated[S, A, B](threshold: Int)(implicit S: HasPBestStagnation[S]): HEntity[S,A,B] => Boolean
```

#### resetStagnation

```scala
resetStagnation[S, A, B](implicit S: HasPBestStagnation[S]): HEntity[S,A,B] => HEntity[S,A,B]
```

#### poolSelectRandom

```scala
poolSelectRandom[A, B, C]: NonEmptyList[User[A, B]] => User[A, B] => StepS[C,Pool[B],User[A, B]]
```

#### poolSelectTournament

```scala
poolSelectTournament[A, B, C](k: Int): NonEmptyList[User[A, B]] => User[A, B] => StepS[C,Pool[B],User[A, B]]
```

#### useBehaviour

```scala
useBehaviour[S, A, B]: NonEmptyList[HEntityB[S, A, B]] => HEntityB[S, A, B] => StepS[A, B, HEntityB[S, A, B]]
```

#### incrementOne

```scala
incrementOne[S, A, B](implicit M: MonadState[StepS[A,Pool[Behaviour[S,A,B]],?],Pool[Behaviour[S,A,B]]]): HEntityB[S, A, B] => HEntityB[S, A, B] => StepS[A,Pool[Behaviour[S,A,B]],Pool[Behaviour[S,A,B]]]
```

#### updateStagnation

```scala
nullPoolUpdate[S, A, B]: HEntityB[S, A, B] => HEntityB[S, A, B] => StepS[A,Pool[Behaviour[S,A,B]],Pool[Behaviour[S,A,B]]]
```

### Algorithms

Keep in mind that all these algorithms will return the same type.

`NonEmptyList[Particle[S,Double]] => Particle[S,Double] => Step[Double,Particle[S,Double]]`

#### Generic Heterogeneous PSO

```scala
genericHPSO[S, A, B](
    schedule: HEntityB[S, A, B] => Boolean,
    selector: NonEmptyList[HEntityB[S, A, B]] => HEntityB[S, A, B] => StepS[A, Pool[Behaviour[S, A, B]], HEntityB[S, A, B]],
    updater: HEntityB[S, A, B] => HEntityB[S, A, B] => StepS[A, Pool[Behaviour[S, A, B]], Pool[Behaviour[S, A, B]]]
)
```

#### Dynamic Heterogeneous PSO

```scala
dHPSO[S: HasPBestStagnation, A, B](stagThreshold: Int)
```

#### Fuzzy Cluster PSO

```scala
fkPSO[S: HasPBestStagnation, A, B](stagThreshold: Int, tournSize: Int)
```


## PSO Methods

The `PSO` object supports all the necessary methods required to build a pso in CILib.
The majority of these methods will return a `Step` for the purpose of chaining them together.
For example

```scala
collection => x => for {
      cog     <- cognitive(collection, x)
      soc     <- social(collection, x)
      v       <- stdVelocity(x, soc, cog, w, c1, c2)
      p       <- stdPosition(x, v)
      p2      <- evalParticle(p)
      p3      <- updateVelocity(p2, v)
      updated <- updatePBest(p3)
    } yield updated
```

This is in contrast to some other methods which exist as helper methods.

### stdPosition

```scala
stdPosition[S,A](
    c: Particle[S,A],
    v: Position[A]
)(implicit A: Module[Position[A],A]): Step[A,Particle[S,A]]
```

### stdVelocity

```scala
stdVelocity[S](
    entity: Particle[S,Double],
    social: Position[Double],
    cognitive: Position[Double],
    w: Double,
    c1: Double,
    c2: Double
)(implicit V: HasVelocity[S,Double]): Step[Double,Position[Double]]
```

### evalParticle

```scala
evalParticle[S](entity: Particle[S,Double]) = Step.eval[S,Double](x => x)(entity)
```

### updatePBest

```scala
updatePBest[S](p: Particle[S,Double])(implicit M: HasMemory[S,Double]): Step[Double,Particle[S,Double]]
```

### updatePBestBounds

```scala
updatePBestBounds[S](p: Particle[S,Double])(implicit M: HasMemory[S,Double]): Step[Double,Particle[S,Double]]
```

### updateVelocity

```scala
updateVelocity[S](p: Particle[S,Double], v: Position[Double])(implicit V: HasVelocity[S,Double]): Step[Double,Particle[S,Double]]
```

### singleComponentVelocity

```scala
singleComponentVelocity[S](
    entity: Particle[S,Double],
    component: Position[Double],
    w: Double,
    c: Double
)(implicit V: HasVelocity[S,Double]): Step[Double,Position[Double]]
```

### gcVelocity

The `gcVelocity` method has a unique parameter that is of type `GCParams`.

```scala
gcVelocity[S](
    entity: Particle[S,Double],
    nbest: Position[Double],
    w: Double,
    s: GCParams
)(implicit V: HasVelocity[S,Double]): Step[Double,Position[Double]]
```

`GCParams` has the following class constructor

```scala
final case class GCParams(p: Double, successes: Int, failures: Int, e_s: Double, e_f: Double)
```

We are also provided with a `defaultGCParams` that will return a `GCParams` with default values.

```scala
defaultGCParams = GCParams(p = 1.0, successes = 0, failures = 0, e_s = 15, e_f = 5)
final case class GCParams(p: Double, successes: Int, failures: Int, e_s: Double, e_f: Double)
```

### barebones

```scala
barebones[S](p: Particle[S,Double], global: Position[Double])(implicit M: HasMemory[S,Double])
```

### quantum

```scala
quantum[S]( // This is relative to the origin
    x: Particle[S,Double], // passed in only to get the length of the vector
    r: RVar[Double],       // magnitude of the radius for the hypersphere
    dist: (Double, Double) => RVar[Double] // Distribution used
  ): Step[Double,Position[Double]]
```

### acceleration

```scala
acceleration[S](
    collection: NonEmptyList[Particle[S,Double]],
    x: Particle[S,Double],
    distance: (Position[Double], Position[Double]) => Double,
    rp: Double,
    rc: Double)(
    implicit C: HasCharge[S]): Step[Double,Position[Double]]
```

### replace

```scala
replace[S](entity: Particle[S,Double], p: Position[Double]): Step[Double,Particle[S,Double]]
```

### better

```scala
better[S,A](a: Particle[S,A], b: Particle[S,A]): Step[A,Boolean]
```

### createParticle

```scala
createParticle[S](f: Position[Double] => Particle[S,Double])(pos: Position[Double]): Particle[S,Double]
```


## Default Algorithms

CILib has several predefined pso algorithms for us to use in the `Defaults`.
Keep in mind that all these algorithms will return the same type.

`NonEmptyList[Particle[S,Double]] => Particle[S,Double] => Step[Double,Particle[S,Double]]`

This is stated before hand to avoid code repetition.

### Global best

```scala
gbest[S](
    w: Double,
    c1: Double,
    c2: Double,
    cognitive: Guide[S,Double],
    social: Guide[S,Double]
)(implicit M: HasMemory[S,Double], V: HasVelocity[S,Double])
```

### Cognitive

```scala
cognitive[S](
    w: Double,
    c1: Double,
    cognitive: Guide[S,Double]
)(implicit M: HasMemory[S,Double], V: HasVelocity[S,Double])
```

### Social

```scala
social[S](
    w: Double,
    c1: Double,
    social: Guide[S,Double]
)(implicit M: HasMemory[S,Double], V: HasVelocity[S,Double])
```

### Guaranteed Convergence PSO

```scala
gcpso[S](
    w: Double,
    c1: Double,
    c2: Double,
    cognitive: Guide[S,Double])(implicit M: HasMemory[S,Double], V: HasVelocity[S,Double]
): NonEmptyList[Particle[S,Double]] => Particle[S,Double] => StepS[Double, GCParams, Particle[S,Double]]
```

### Charged

```scala
charged[S:HasCharge](
    w: Double,
    c1: Double,
    c2: Double,
    cognitive: Guide[S,Double],
    social: Guide[S,Double],
    distance: (Position[Double], Position[Double]) => Double,
    rp: Double,
    rc: Double
)(implicit M:HasMemory[S,Double], V:HasVelocity[S,Double])
```

### Nonlinear Model Predictive Control

```scala
nmpc[S](
    guide: Guide[S,Double]
)(implicit M: HasMemory[S,Double])
```

### Crossover PSO

```scala
crossoverPSO[S](
    guide: Guide[S,Double]
)(implicit M: HasMemory[S,Double])
```
