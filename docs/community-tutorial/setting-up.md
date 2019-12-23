---
id: setting-up
title: Getting started
---

Include CIlib into your project by adding the following library dependencies to your `build.sbt`:

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


# Development environments

There is no prescrived development environment.
Please feel free to use whatever combination of editor / lanagauge server / IDE.
The most important focus is the configuration within the build tool, `sbt`, which defines the build process on the command line and on the continuous integration pipeline.

Users have reported using IDEs such as IntelliJ and others have used Scala Metals and their favourite text editor.
Please consult the documentation of the relevant software vendor to get your developement environment setup to meet your needs.


# Contributing

Developement on CIlib it self uses the `nix` package manager to define a reproducible developer environment.
The same environment is used on during the automated continuous integration.
Once `nix` has been installed, simply `cd` to the project directory and enter a `nix-shell`.
