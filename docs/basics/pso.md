---
id: pso
title: PSO
---

<!--
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
-->
