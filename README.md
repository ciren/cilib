# CIlib - Computational Intelligence Library

![](https://github.com/ciren/cilib/workflows/build/badge.svg)
[![Join the community on Spectrum](https://withspectrum.github.io/badge/badge.svg)](https://spectrum.chat/cilib)
[![Join the chat at https://gitter.im/ciren/cilib](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/ciren/cilib?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Maven Central](https://img.shields.io/maven-central/v/net.cilib/cilib-core_2.12.svg)](https://maven-badges.herokuapp.com/maven-central/net.cilib/cilib-core_2.12)
[![Javadocs](https://javadoc.io/badge/net.cilib/cilib-core_2.12.svg)](https://javadoc.io/doc/net.cilib/cilib-core_2.12)

CIlib is a library of various computational intelligence
algorithms. The goal of the project is to create a library that can be used
and referenced by individuals and researchers alike.

The goals of the project are:
 * To provide a type-safe library, preventing as many runtime errors and
   invalid data representations as possible
 * Allow for the perfect reproduction of simulations, enabling researchers
   to validate and reuse previous work and published results with confidence
 * Enable composition, reducing the need to repeat implementations

CIlib is not a "framework", instead the library is a set of a few very simple
abstractions, and allows for a principled manner to define computational
intelligence algorithms and uses several typeclasses such as `Functor` and
`Monad`.

## Documentation and Support

* Have a look a the [website](https://cilib.net)
* [scaladoc](https://cilib.net/api/cilib)
* The source code of the project also contains several examples, have
  a look in the `example` directory
* If you run into trouble, please open an issue
* Come join in the discussion in `#cilib` on `FreeNode`, or join
  the [Gitter channel](https://gitter.im/ciren/cilib)
