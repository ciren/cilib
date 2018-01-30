# About This Book #{-}

This book is aimed at walking you through the different components of CILib (Computational Intelligence Library). CILib is written in Scala, if you don't have any experience with Scala I would recommend first learning the language and getting a good idea of what Scala is and how it works. To do this I would recommend Essential Scala and Scala With Cats by the team over at [Underscore][link-underscore].

Along with theory explanations of what certain components do, the book will also include code to demonstrate an example of use. In a way, this book will also serve as documentation by example for CILib.

During this book we will be covering four major sections:

- Setting up CILib
- Learning the core components
- Learning about algorithm implementations
- Working through some examples

As we cover each section you will begin to learn how components work and where to use them.
After all this, you will me more than ready to start implementing your work using CILib.

## What is CILib? #{-}

CILib is software library which aids in the experimentation and research of Computational Intelligence algorithms. The library was created by the CIRG research group of the University of Pretoria. Gary Pamparà, a member of CIRG, created CILib and has helped grow the library ever since. And I would like to thank him and everyone who has contributed towards CILib.

### Principled design #{-}
It is very important to ensure that the library code is pure - thereby reducing complexity. This has many advantages but, most importantly, it allows for the controlling of side-effects which is a primary concern, especially when randomness is involved. As a consequence of this, and other aspects, CILib makes an active effort to address the following:

- **Correctness**: All algorithmic components should be correct and operate as intended, doing nothing more. Peer-review is hugely valuable in this regard, providing the confidence that the implementations are correct and sound.
- **Type safety**: The use of types is a fantastic way to ensure that a program cannot represent invalid states. This removes a huge set of potential errors and ensures greater confidence, as the compiler is always double-checking the code.
- **Reproducibility**: Within scientific research, being able to reproduce the work of another researcher is important. It's also a fundamental part of the scientific method. When complexities such as randomness, this becomes much more  difficult and is generally extremely cumbersome. CILib must allow for the perfect replication of experimental work.

 With all this in mind, we can think of CILib as a platform that allows us to build a project by combining a number of components that are reinforced by type safety to ensure correctness and can be easily reproduced.

### Project structure #{-}

Trying to maintain a modular set of functionalities, CILib consists of several sub-projects:

- core - Contains type class definitions together with required data structures.
- exec - Simplistic execution code allowing for experimental execution.
- de - Data structures and logic related to Differential Evolution.
- docs - Sources for the website.
- ga - Data structures and logic related to Genetic Algorithms.
- eda
- io - For CSV and parquet encoding
- moo - Type classes, instances and data structures for Multi-Objective Optimization.
- pso - Data structures and logic related to Particle Swarm Optimization.

There is also an examples sub project which offers full imputation of algorithms as an example of how CILib is used.

### Support #{-}
If you have any feedback or questions, please contact is in #CILib on FreeNode IRC or come chat to us in the project's Gitter channel; alternatively, feel free to open an issue.

Another book is also in development that will be a guide to developing for CILib. Mostly covering how to set up the developing environment as well as offer some insight it to "behind the scenes" of how things work.

Additional dependencies on community projects include the following:

- [Scalaz][Scalaz-link] for functional typeclasses
- [Spire][Spire-link] for mathematical typeclasses
- [Monocle][Monocle-link] for optics
- [Shapeless][Shapeless-link]
- [Refined][Refined-link]