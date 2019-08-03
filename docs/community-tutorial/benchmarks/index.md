# Benchmarks

Often in computational intelligence we will need to test algorithms on
standard problems. It should be noted that CIRG contains a library of
benchmarks that we can use with CILib. The benchmarks can be found
[here][benchmarks] as well as a list of available benchmarks. To
import benchmarks into your build you need the following dependency
added to your sbt.build.

`    "net.cilib"  %% "benchmarks"        % "0.1.1"`

The following snippets of code will demonstrate how to include
benchmarks in your program as an example use.

```tut:book:silent
import cilib._
import cilib.benchmarks._ // Import for benchmarks
import scalaz._
import spire.implicits._
import spire.math.Interval

// Example use
val bounds = Interval(-5.12,5.12)^30
val env =
    Environment(
        cmp = Comparison.dominance(Min),
        eval = Eval.unconstrained(Benchmarks.spherical[NonEmptyList, Double]).eval)
```

That's all there is to it! Be sure to check out the list of
benchmarks that are included and can be accessed by the `Benchmark`
object.
