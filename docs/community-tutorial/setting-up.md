---
id: setting-up
title: Getting started
---

Add the appropriate library dependencies to your `build.sbt` (adjusting the module and version):

```
libraryDependencies += "net.cilib" %% "cilib-<module>" % "@CILIB_VERSION@"
```

In the above, the `<module>` identifier can be replaced with one of the following:

- `core` - Contains type class definitions together with required data structures.
- `exec` - Simplistic execution code allowing for experimental execution.
- `de` - Data structures and logic related to Differential Evolution.
- `docs` - Sources for the website.
- `ga` - Data structures and logic related to Genetic Algorithms.
- `pso` - Data structures and logic related to Particle Swarm Optimization.
- `moo` - Type classes, instances and data structures for Multi-Objective Optimization.


## Contributing

The developement of CIlib itself uses the `nix` package manager to define a reproducible development environment.
The same environment is used during the automated continuous integration process.
Once `nix` has been installed, simply `cd` to the project root directory (which contains `shell.nix`) and enter a `nix-shell`.


## About the community tutorial

This tutorial is aimed at walking you through the different parts of CILib (Computational Intelligence Library).
CILib is written in Scala and familiarity with the language is highly recommended.
Scala is a language for the JVM that allows for the expression and usage of more advanced type system capabilities, which Java nor the other JVM languages are able to provide.
[Essential Scala](1) and [Advanced Scala With Cats](2) are freely available, online Scala resources but note that the resources probably recommend practives that are not followed in the development of CIlib.
The core focus in CIlib development is to *always* prefer a functional approach for implementation and avoid the use of object-orientation as much as possible.
The benefits obtained using this style far outweigh any overhead, and is therefore preferred.

> ##### Compiler verified code samples
> Throughout the tutorial you will see a lot of theory along side practical examples represented through blocks of code.
> These blocks of code are verified during the documentation processing and will always be up to date with the referenced version of CIlib.
