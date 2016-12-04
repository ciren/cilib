---
layout: docs
title: "Getting Started"
---

# Getting Started

CIlib is currently cross-built for Scala 2.11 & 2.12.

To get started, you need will need to include the required CIlib module
into your project's build

    libraryDependencies += "net.cilib" %% "cilib-<module>" % "<version>"

CIlib consists of several modules, each containing specialized parts of
the larger library. Please refer to the project structure, within the design,
for a listing of each of the modules and what they provide. Additional
dependencies on community projects include the following:

- [Scalaz](https://github.com/scalaz/scalaz) for functional typeclasses
- [Spire](https://github.com/non/spire) for mathematical typeclasses
- [Monocle](http://julien-truffaut.github.io/Monocle/) for optics

