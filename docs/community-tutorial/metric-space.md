# Metric Space

In this chapter we will start off with a brief explanation of "what are metric spaces".
After which we explore CILib's implementations of certain metric spaces.
It should be noted that the focus of this chapter is learning what and how metric spaces are used in CILib, not actually learning metric spaces.

## Brief Definition of Metric Spaces

A MetricSpace is a set together with a notion of distance between elements.
Those distances, taken together, are called a metric on the set.
Distance is computed by a function dist which has the following four laws:

* Non-negative: for all x y. dist x y >= 0
* Identity of indiscernibles: for all x y. dist x y == 0 <=> x == y
* Symmetry: for all x y. dist x y == dist y x
* Triangle inequality: for all x y z. dist x z <= dist x y + dist y z

The [Wikipedia][metric-space-link] article can provide more details on metric spaces if you are unfamiliar.

## Metric Spaces in CILib

Firstly, let's take a look at how CILib defines a metric space.

```scala
trait MetricSpace[A,B] { self =>
  def dist(x: A, y: A): B

  def map[C](f: B => C): MetricSpace[A,C] =
    new MetricSpace[A,C] {
      def dist(x: A, y: A) = f(self.dist(x, y))
    }

  def flatMap[C](f: B => MetricSpace[A,C]): MetricSpace[A,C] =
    new MetricSpace[A,C] {
      def dist(x: A, y: A) =
        f(self.dist(x,y)).dist(x, y)
    }
}
```

As you can see, it uses a trait to provide am abstract definition of a metric space object.
This abstraction becomes fully realized through the use of the `Metric Space` object (MSO for short).
The MSO has several methods, all of which return implementation of the trait `MetricSpace`.
Thus, the following metric spaces are available to us:

* Levenshtein
* Discrete
* Minkowski
* Manhattan
* Euclidean
* Chebyshev
* Hamming

Now for formal definitions of the methods:

```scala
levenshtein[B](implicit B: Integral[B]): MetricSpace[String,B]

discrete[A,B](implicit A: scalaz.Equal[A], B: Integral[B]): MetricSpace[A, B]

chebyshev[F[_]:Foldable, A:Order:Signed](implicit ev: Ring[A]): MetricSpace[F[A], A]

minkowski[F[_]:Foldable, A:Signed:Field,B](alpha: Int)(implicit A: Fractional[A], ev: Field[B]): MetricSpace[F[A],B]

manhattan[F[_]:Foldable, A:Signed:Field:Fractional, B:Fractional:Field] = minkowski[F, A, B](1)

euclidean[F[_]:Foldable, A:Signed:Field:Fractional, B:Fractional:Field] = minkowski[F, A, B](2)

hamming[F[_]:Foldable, A]: MetricSpace[F[A],Int]

point[A,B](a: B): MetricSpace[A,B]: MetricSpace[A,B]
```

If any of this seems daunting or scary in any way, don't worry.
These are methods, just like any other methods, but have strict type parameters.
So the code, `F[_]:Foldable`, simply means we require some sort of functor that is an extension of `Foldable`.
If you are still a bit unsure, I recommend looking at the "Scala With Cats" book by the team over at [Underscore][link-underscore].

Currently `MetricSpaces` are only used with regards to selection methods in CILib.
However, how you choose to use them is entirely up to you.

### Implicits

Lastly, the MSO provides us with three implicit definitions that which might be useful to know.
These being:

```scala
metricSpaceProfunctor: Profunctor[MetricSpace]

metricSpaceSemiGroup[A,B](implicit B: Semigroup[B]): Semigroup[MetricSpace[A,B]]

metricSpaceMonad[A]: Monad[MetricSpace[A,?]]
```

## Summary

This chapter was one of the shorter ones in this book, but by no means does it make it any less important.
Armed with this new theory knowledge we can start putting things putting metric spaces to work in the next chapter,
where we will see how they are used through practical examples.
