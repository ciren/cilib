# CIlib - Computational Intelligence Library

![CI](https://github.com/ciren/cilib/workflows/CI/badge.svg?branch=master&event=push)
![Discord](https://img.shields.io/discord/997122192909291550?style=flat-square)
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

* Have a look a the [website](https://cilib.net). Please note that some pages are still being written, but contributions are always welcome.
* [scaladoc](https://cilib.net/api/cilib)
* The source code of the project also contains several examples, have
  a look in the [example](https://github.com/ciren/cilib/tree/master/example/src/main/scala/cilib/example) directory
* If you run into trouble, please open an issue
* For help, consider using either the GitHub discussions or come chat to us on Discord.
