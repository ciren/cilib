# CIlib - Computational Intelligence Library

[![Build Status](https://travis-ci.org/cirg-up/cilib.svg?branch=series%2F2.0.x)](https://travis-ci.org/cirg-up/cilib)
[![Join the chat at https://gitter.im/cirg-up/cilib](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/cirg-up/cilib?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

CIlib is a library of various computational intelligence
algorithms. The goal of the project is to create a library that can be used
and referenced by individuals and researchers alike.

CIlib is not a "framework", instead the library is a set of a few very simple
abstractions, and allows for a principled manner to define computational
intelligence algorithms and uses several typeclasses such as `Functor` and
`Monad`.

## Quick Start

We are currently developing version *2.0.0*, which requires Scala *2.11* together with

* scalaz 7.2.x
* spire 0.11.0
* monocle 1.2.0

CIlib itself is broken up into several modules:

* `core` for the main abstractions and data structures
* `de` for diffential evolution
* `exec` for minimal simulation execution
* `ga` for genetic algorithms
* `moo` for multi-objective optimization
* `pso` for particle swarm optimization

Please see the documentation for more information on these modules.

## Documentation and Support

* Have a look a the documentation website
* Scaladoc
* The source code of the project also contains several examples, have a look in the `example` directory
* If you run into trouble, please open an issue
* Come join in the discussion in #cilib on FreeNode, or join the [Gitter channel](https://gitter.im/cirg-up/cilib)

CIlib is maintained by several individuals and supported by CIRG @ UP (Computational Intelligence Research Group @ University of Pretoria).
