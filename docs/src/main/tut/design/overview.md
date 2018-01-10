# Overview

This section details the design of CIlib. The intention is that this part of the documentation
should be enough to allow you to fully understand the reasoning behind some design decisions
as well as giving the needed knowledge to allow extensions to be made that use the core
data structures in CIlib.

The core of CIlib itself is divided into a few data structures. These structures control the
effects of randomness within an algorithm, the manner in which the optimization scheme is used
(minimization or maximization) and defines the way in which small pieces of logic may be composed
into larger pieces for inclusion in an algorithm definition.

The manner in which such effects are controlled, is managed by structures provided to us by the world
of mathematics and these general structures, may, seem to have odd names. We assure you that these
names only sound odd due to the fact that they are unfamiliar to you. As they become familiar
you will not only see the benefits that they provide, but also that they provide a common
nomenclature, which is very handy! It allows us to talk about various aspects of an algorithm
without having to worry about the manner in which such an action may be performed. It is,
however, true that we do need to worry about the execution semantics at some point so that
the algorithm is executed correctly.

The sections that follow will explain different aspects of CIlib, which is a
progression of different types to build up the needed structures.

For all usage samples, the following imports are required within the REPL
session, but will be ignored to favor discussion:

```scala
import scalaz._
import Scalaz._

import cilib._
```
