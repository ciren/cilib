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