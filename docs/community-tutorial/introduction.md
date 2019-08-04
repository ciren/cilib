---
id: introduction
title: Introduction
---

## About this tutorial

This tutorial is aimed at walking you through the different components of CILib (Computational Intelligence Library).
CILib is written in Scala and familiarity with the language is highly recommended.
Scala is a language for the JVM that allows for the expression and usage of more advanced type system capabilities.
The type system of Scala is more adbvanced than that of Java/Kotlin/Clojure etc.
[Essential Scala](1) and [Advanced Scala With Cats](2) are freely available, online Scala resources.

:::note Compiler verified code samples
Throughout the tutorial you will see a lot of theory along side practical examples represented through blocks of code.
These blocks of code are verified during the documentation processing and will always be up to date with the referenced version of CIlib.
:::

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

It is very important to ensure that the library code is pure - thereby reducing complexity. This has many advantages but, most importantly, it allows for the controlling of side-effects which is a primary concern, especially when randomness is involved.
As a consequence of this, and other aspects, CILib makes an active effort to address the following:

- **Correctness**: All algorithmic components should be correct and operate as intened, doing nothing more.
  Peer-review is hugely valuable in this regard, providing confidence that the implementations are correct and sound.
- **Type safety**: The use of types is a fantastic way to ensure that a program cannot represent invalid states.
  This removes a huge set of potential errors and ensures greater confidence, as the compiler is always double-checking the code.
- **Reproducibility**: Within scientific research, being able to reproduce the work of another researcher is important.
  It's also a fundamental part of the scientific method.
  With complexities such as randomness, this becomes much more difficult and is generally extremely cumbersome.
  CILib must allow for the perfect replication of experimental work.

With all this in mind, we can think of CILib as a platform that allows us to build a project by combining a number of components that are reinforced by type safety to ensure correctness and can be easily reproduced.


## Help and support

If you have any feedback or questions, please come and chat to us in the project's [Gitter channel](4); alternatively, feel free to [open an issue](5).


[1]: https://underscore.io/training/courses/essential-scala/
[2]: https://underscore.io/training/courses/advanced-scala/
[3]: https://underscore.io/
[4]: https://gitter.im/cirg-up/cilib
[5]: https://github.com/cirg-up/cilib/issues/new/choose
