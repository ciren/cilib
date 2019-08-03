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

