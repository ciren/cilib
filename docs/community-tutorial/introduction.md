---
id: introduction
title: Introduction
---

## About this tutorial

This tutorial is aimed at walking you through the different components of CILib (Computational Intelligence Library).
CILib is written in Scala and familiarity with the language is highly recommended.
Scala is a language for the JVM that allows for the expression and usage of more advanced type system capabilities.
The type system of Scala is more advanced than the type systems available in Java/Kotlin/Clojure etc.
[Essential Scala](1) and [Advanced Scala With Cats](2) are freely available, online Scala resources.

> ##### Compiler verified code samples
> Throughout the tutorial you will see a lot of theory along side practical examples represented through blocks of code.
> These blocks of code are verified during the documentation processing and will always be up to date with the referenced version of CIlib.

Within this tutorial we will be covering four major sections:

- Setting up CILib
- Learning the core components
- Learning about algorithm implementations
- Working through some examples

As we cover each section you will begin to learn how components work and where to use them.
After all this, you will be more than ready to start implementing your work using CILib.

## What is CILib?

CILib is software library which aids in the experimentation and research of Computational Intelligence algorithms.
The library was created through a combined effort by several researchers.
The project originally was called PSOlib as it focused on the PSO algorithm, but over time had additional contributions outside of the PSO area and the project was renamed to CILib.

### Principled design

Pure functions operate only the passed in function parameters and return a value.
Given the same set of function parameters, the same result from the function is expected and is known as _referential transparency_.
It is very important to ensure that the library code is pure: it reduces complexity.
Due to pure functions only returning values, it allows for the controlling of effects.
Effects are actions that a program perfroms to achieve a goal.
One of the primary effects managed by CIlib is the effect of randomness applied to other values.
As a consequence of this, and other aspects, CILib makes an active effort to address the following:

- **Correctness**: All algorithmic components should be correct and operate as intened, doing nothing more.
  Peer-review is hugely valuable, providing confidence that the implementations are correct and sound.
- **Type safety**: The use of types is a fantastic way to ensure that a program cannot represent invalid states.
  This removes a huge set of potential errors and ensures greater confidence, as the compiler is always double-checking the code.
- **Reproducibility**: The abiltity to reproduce the work of another researcher is a fundamental part of the scientific method.
  With effects such as randomness applied to algorithms, reproducing results becomes much, much more difficult.
  It is, therefore, required that CILib be able to provide the perfect replication of experimental work.

With all this in mind, we can think of CILib as a platform that allows us to build a project by combining a number of components that are reinforced by type safety to ensure correctness, whilst allowing for easily reproduced results.


## Help and support

If you have any feedback or questions, please come and chat to us in the project [Gitter channel](4); alternatively, feel free to [open an issue](5).


[1]: https://underscore.io/training/courses/essential-scala/
[2]: https://underscore.io/training/courses/advanced-scala/
[3]: https://underscore.io/
[4]: https://gitter.im/cirg-up/cilib
[5]: https://github.com/cirg-up/cilib/issues/new/choose
