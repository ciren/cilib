# Revision history for CIlib

## [2.2.0]

This release replaces some of the internal library code with zio and
zio-prelude libraries. The changes are not fundamental and have
allowed for some simplifications to some of the types within the
library.

The list of Notable changes are found below.

### Added

- Support for Scala 3.x

### Changed

- Default version of Scala is now 2.13
- `zio`, `zio-stream` and `zio-prelude` are new dependencies,
  replacing `scalaz` and `scalaz-stream`/`fs2`.
- `RVar`, `Step` and `StepS` data types have been simplified, with a
  type parameter being removed from both `Step` and `StepS`.
- Data types now have variance annotations where relevant, allowing
  for better error messages from the compiler, aiding in the
  simplification of types (e.g. the `Eval` instances).
- `zio-optics` is used for optics
- The underlying list-like representation of `Position` has been
  replaced with a much more performant and memory-efficient structure.
- Updates to including required upgrades for project dependencies

### Removed

- Dropped Scala 2.11 and 2.12 support
- Superfluous `hoist` syntax has been dropped
- Removed the `refined` library in favour of using `newtype`s from
  `zio-prelude`.
- The dependency on `monocle` has been removed to reduce the size of
  transitive dependencies.
- Removed the use of `spire` with CIlib
- The heterogeneous PSO has been removed in favour of a new, far
  simpler implementation.
- The `cilib.Environment` type has been removed and values are now
  just used directly (the indirection has now been reduced).
- Support for outputting data in CSV format has been removed. The CSV
  format is troublesome, dropping type information in favour of
  `String`s. Instead, the only supported format is `parquet` and CSV
  formats can be obtained through the conversion of the `parquet` file
  using additional tools. This is preferred as the size of `parquet`
  files is dramatically more reasonable than a plain-text encoding.


## [2.0.0]

This is the first release of CIlib 2.0, a library for computational
intelligence. This version focuses on correctness, type-safety and,
most importantly, the ability to perfectly reproduce results.

The project is divided up into several modules, each providing the
minimum amount of functionality for a given CI algorithmic metaphor.
Examples of modules include modules for PSO, GA, DE and EDA
algorithms.

Given that this is the first release of the library, what follows is a
general description of the main features within the library. For usage
details and examples, please consult either the project
[website](https://cilib.net), the _examples_ directory in the project
source, or the community created [tutorial
book](https://github.com/ciren/cilib-tutorial/releases/latest)

For more information, please consult the
[scaladoc](https://cilib.net/api/cilib/index.html) and come join the
community on [gitter.im](https://gitter.im/cirg-up/cilib)

### Controlled Randomness

Probably the most important aspect of the library is to control the
effects of randomness within an algorithm and problem. Often, these
PRNGs are created on the fly in an ad-hoc manner, or they are simply
sampled from the provided PRNG of the host language or the operating
system. Although these may be "good enough" in most cases, their usage
is simply unacceptable in CI. The PRNG used and the manner in which
the values from the PRNG are sampled creates a snowball effect within
the algorithm as the next value produced from the PRNG is dependant on
how the previous value was produced.

For this reason, the randomness has been extracted into an effect that
is tracked and maintained by the core data structures of the
library. Because of this tracking behaviour, it is also possible
replicate and repeat experiments or simulations to achieve the exact
same results. Additionally, this tracking forces the user to provide
the source of randomness, but only at the point where an algorithm is
to be executed.

### Composition

The library is built with composition in mind. This allows the same
pieces of logic to be reused in a variety of ways, preventing
duplication and most importantly, allowing for simpler
experimentation. Creating larger pieces of logic from smaller pieces
is a desirable property to have.

### Type-safety

The library is implemented in a purely functional way, favoring
immutability. Using immutable structures and pure functions prevents
a whole series of errors, which is just too valuable to ignore.

Furthermore, where possible, as many errors will be reported to the
user during compile time. Although this may seem inconvenient, the
benefits far out-weigh the perceived disadvantages. One of the main
ideas with the design and implementation of the library is that if the
code compiles, it will execute. This does not mean that there will be
no errors - that's a foolish thing to say - but what it does mean is
that any problems will be logic errors and not related to the
structure of the resulting algorithm and problem definitions.

### Explicit focus on algorithms

It's tempting to expand a software project to eventually support
everything, but it is not the correct way nor a good idea. To this
end, CIlib will provide the user with the tools needed to execute
algorithms and perform measurements on the results of the algorithms.
These results can then be written to different file formats (CSV or
Parquet). Once the results have been obtained, it is recommended that
the user then use these data files within existing analysis
frameworks. Many such tools already exist (R / Spark / Pandas / etc)
and the file formats supported by CIlib can be read by these packages
without much effort. It should be noted that parquet is the preferred
format, not only because the resulting file is smaller than that of a
CSV, but because the format contains metadata about the data columns
it maintains, and that this metadata can be used within the analysis
tools.

The format of this data is also defined based on a data structure that
the user provides.

---

Note: The version 1.x series of CIlib is a complete framework,
completely based around a simulation program. This series has been
deprecated and will no longer be updated, but is kept within the
repository purely for prosperity. There are several problems with this
implementation and was the inspiration for the current series of
CIlib.
