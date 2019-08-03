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
