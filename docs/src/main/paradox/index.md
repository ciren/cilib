# Cilib

@@@ index

* [Design](design/index.md)
* [Usage](usage/index.md)

@@@

CIlib is a library of computational intelligence algorithms. The goal
is to provide a library that can be used by researchers and
invididuals, which provides sound implementations that adhere to very
principled design, and verification.

Some of the core requirements for the library are:

  * Provide implmentations that are type safe, functional and focused
    on composition.
  * Allow for the exact reproduction of experiments
    and simulations, allowing researchers to validate and reuse prior
    work with confidence

CIlib is _not_ a framework - instead the library is founded on some
very simple abstractions, from which the algorithm parts are
constructed.

## Resources

* Come join in the discussion in `#cilib` on `FreeNode`, or join the
  [Gitter channel](https://gitter.im/cirg-up/cilib)
* [scaladoc](https://cilib.net/api/cilib)
* The source code of the project also contains several examples, have
  a look in the @github[example](/example/src/main/scala/cilib/example)
  directory
* If you run into trouble, please open an issue
