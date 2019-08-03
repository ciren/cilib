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