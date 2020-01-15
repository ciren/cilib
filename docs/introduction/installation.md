---
id: installation
title: Installation
hide_title: true
---

# Installation

To include CIlib into your project, add the appropriate library dependencies to your project build tool (adjusting the artifact name for the different available modules).

An example, using SBT, would be:

```scala
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

Have a look at [the central repository](https://search.maven.org/search?q=cilib) for the different import statements for a variety of build tools.
For example, [this](https://search.maven.org/artifact/net.cilib/cilib-core_2.12/@CILIB_VERSION@/jar) is the page for the `core` cilib module which lists the different build tool configurations.
If there are any specifics that are not answered by these configurations, please feel free to get in contact with us.


<!-- ## Contributing -->

<!-- The developement of CIlib itself uses the `nix` package manager to define a reproducible development environment. -->
<!-- The same environment is used during the automated continuous integration process. -->
<!-- Once `nix` has been installed, simply `cd` to the project root directory (which contains `shell.nix`) and enter a `nix-shell`. -->
