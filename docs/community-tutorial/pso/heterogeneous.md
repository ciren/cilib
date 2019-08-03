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