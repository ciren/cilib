```tut:invisible
import cilib._
import cilib.algebra._
import spire.implicits._
import spire.math._
import scalaz._
import Scalaz._
```

# Position

Candidate solution vectors within a search space are the basic pieces of
information that computational algorithms maintain and, includes feature
vectors that represent training patterns in a neural network.
Within population based algorithms, a collection of algorithm
participants are employed in a search of the problem space. Each represents
a possible solution to the problem at hand, and may be in one of two
possible states:

1. It may be a "point" in the search space where no other information about
   the point is known, except for the value of the multi-dimensional vector
   representing the position within the search space
2. It may be a possible "solution", where the position in the multi-dimensional
   search space is known but, an additional value representing the
   "quality" of the vector is also maintained. This "quality" is
   referred to as the fitness of the candidate solution.

`Position` is a data structure that encodes the above two cases
exactly, allowing a `Position` to either be a `Point` or a `Solution`.
Further more, `Position` is an Algebraic Data Type (ADT), whereby the set
of possible representations may not be extended further (and is enforced
by the compiler). Any changes to a `Solution` will yield a `Point` - the
new `Position` has not yet had a fitness calculated, i.e: the
quality of the `Position` is an unknown.

A `Position`, within a search space, can be created by providing the search
space bounds to the `createPosition` function. The search space must have
at least a single dimension (the zero-dimensional search is trivial) and, a
`NonEmptyList[Interval[Double]]` represents the search space bounds.
A `NonEmptyList` is a list that is guaranteed to have at least one
contained element.

A search space is determined by a list of `Interval` instances (provided
by [spire]()), one for each dimension. The `Interval` need not be the same
for each dimension and differing `Interval`s may be placed together in a
`NonEmptyList` to define the problem search space. As it is rather common
to define a search space where an interval repeats `n` times, some syntax
has been added to the `Interval` data constructor to allow for repetition in
a more convenient way. This syntax models the
text parser used in CIlib 1.0 for the "domain string", but is now available
at the type level and verifiable during compilation.
As an example, let's create a 30-dimensional vector in the interval
$[-5.12, 5.12]$:

```t
Interval(-5.12,5.12)^30
```

A `Position` may now be constructed, as we know what the bounds of the search
space are

```t
Position.createPosition(Interval(-5.12,5.12)^30)
```

The result of creating a `Position` is a `RVar[Position[A]]` computation,
as the `Position` is created with a vector placed randomly within the search
space bounds. Furthermore, the type of the dimension elements within the
`Position` is inferred, based on the provided `Interval` information (in this
example a `Double`).

The normal vector operations are provided, as syntax, to make the usage
simpler and to mirror the mathematics defined in literature more closely.
Below are some examples of combining `Position` instances. Take careful
note of the return value for the different cases of `Position`. In order
to evaluate the quality of a `Position` an `Eval` instance is required.

```t
val e = Eval.unconstrained[NonEmptyList,Double](_.map(x => x*x).suml)

val (_, (a, b)) = // a is a Point and b is a Solution
  (for {
    a <- Position.createPosition(Interval(-5.12,5.12)^3)
    b <- Position.createPosition(Interval(-5.12,5.12)^3).flatMap(p => Position.eval(e, p))
  } yield (a, b)).run(RNG.init(1234L))

-a // Unary syntax to negate a Position

a + b // Add Point and Solution

a + a // Add Point and Point

b + b // Add Solution and Solution

a - b // Subtract Solution from Point

// a * b does not compile. Vector multiplication makes little sense.
// If the objective was to use pairwise multiplication, there is another
// structure called `Pointwise` which can be used to achieve this operation
// via the `Algebra` object.
Algebra.pointwise(a, b)

3.0 *: a // Scalar multiplication
```

Whenever a `Position` is moved to a "new point" within the search space,
it requires re-evaluation.
