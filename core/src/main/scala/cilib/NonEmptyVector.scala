package cilib

import cilib.NonEmptyVector._
import zio.prelude._
import zio.{ Chunk, ZIO }

/**
 * A `NonEmptyVector` is a structure that is guaranteed to contain at
 * least one element. As a result, operations which would not be safe
 * when performed on `Vector`, such as `head` or `reduce`, are safe
 * when performed on `NonEmptyVector`. Operations on `NonEmptyVector`
 * which could potentially return an empty structure will return a
 * `Chunk` instead.
 */
final class NonEmptyVector[+A] private (private val chunk: Chunk[A]) { self =>

  /**
   * Appends a single element to the end of this `NonEmptyVector`.
   */
  def :+[A1 >: A](a: A1): NonEmptyVector[A1] =
    nonEmpty(chunk :+ a)

  /**
   * Appends the specified `Chunk` to the end of this `NonEmptyChunk`.
   */
  def ++[A1 >: A](that: Chunk[A1]): NonEmptyVector[A1] =
    append(that)

  /**
   * A named alias for `++`.
   */
  def append[A1 >: A](that: Chunk[A1]): NonEmptyVector[A1] =
    nonEmpty(chunk ++ that)

  /**
   * Converts this `NonEmptyVector` of bytes to a `NonEmptyVector` of bits.
   */
  def asBits(implicit ev: A <:< Byte): NonEmptyVector[Boolean] =
    nonEmpty(chunk.asBits)

  /**
   * Returns whether this `NonEmptyVector` and the specified `NonEmptyVector` are
   * equal to each other.
   */
  override def equals(that: Any): Boolean =
    that match {
      case that: NonEmptyVector[_] => self.chunk == that.chunk
      case _                       => false
    }

  /**
   * Maps each element of this `NonEmptyVector` to a new `NonEmptyVector` and
   * then concatenates them together.
   */
  def flatMap[B](f: A => NonEmptyVector[B]): NonEmptyVector[B] =
    nonEmpty(chunk.flatMap(a => f(a).chunk))

  /**
   * Flattens a `NonEmptyVector` of `NonEmptyVector` values to a single
   * `NonEmptyVector`.
   */
  def flatten[B](implicit ev: A <:< NonEmptyVector[B]): NonEmptyVector[B] =
    flatMap(ev)

  /**
   * Returns the hashcode of this `NonEmptyChunk`.
   */
  override def hashCode: Int =
    chunk.hashCode

  def head: A =
    chunk.head

  def init =
    chunk.init

  def last =
    chunk.last

  def length =
    chunk.length

  /**
   * Transforms the elements of this `NonEmptyChunk` with the specified
   * function.
   */
  def map[B](f: A => B): NonEmptyVector[B] =
    nonEmpty(chunk.map(f))

  /**
   * Maps over the elements of this `NonEmptyChunk`, maintaining some state
   * along the way.
   */
  def mapAccum[S, B](s: S)(f: (S, A) => (S, B)): (S, NonEmptyVector[B]) =
    chunk.mapAccum(s)(f) match { case (s, chunk) => (s, nonEmpty(chunk)) }

  /**
   * Effectfully maps over the elements of this `NonEmptyChunk`, maintaining
   * some state along the way.
   */
  def mapAccumM[R, E, S, B](s: S)(f: (S, A) => ZIO[R, E, (S, B)]): ZIO[R, E, (S, NonEmptyVector[B])] =
    chunk.mapAccumM(s)(f).map { case (s, chunk) => (s, nonEmpty(chunk)) }

  /**
   * Effectfully maps the elements of this `NonEmptyChunk`.
   */
  def mapM[R, E, B](f: A => ZIO[R, E, B]): ZIO[R, E, NonEmptyVector[B]] =
    chunk.mapM(f).map(nonEmpty)

  /**
   * Effectfully maps the elements of this `NonEmptyChunk` in parallel.
   */
  def mapMPar[R, E, B](f: A => ZIO[R, E, B]): ZIO[R, E, NonEmptyVector[B]] =
    chunk.mapMPar(f).map(nonEmpty)

  /**
   * Materialize the elements of this `NonEmptyVector` into a `NonEmptyVector`
   * backed by an array.
   */
  def materialize[A1 >: A]: NonEmptyVector[A1] =
    nonEmpty(chunk.materialize)

  /**
   * Prepends the specified `Chunk` to the beginning of this `NonEmptyVector`.
   */
  def prepend[A1 >: A](that: Chunk[A1]): NonEmptyVector[A1] =
    nonEmpty(that ++ chunk)

  def +:[A1 >: A](that: Chunk[A1]): NonEmptyVector[A1] =
    nonEmpty(that ++ chunk)

  /**
   * Reduces the elements of this `NonEmptyVector` from left to right using the
   * function `map` to transform the first value to the type `B` and then the
   * function `reduce` to combine the `B` value with each other `A` value.
   */
  def reduceMapLeft[B](map: A => B)(reduce: (B, A) => B): B = {
    val iterator = chunk.materialize.toIterator
    var b: B     = null.asInstanceOf[B]

    while (iterator.hasNext) {
      val a = iterator.next()
      if (b == null) b = map(a) else b = reduce(b, a)
    }

    b
  }

  /**
   * Reduces the elements of this `NonEmptyVector` from right to left using the
   * function `map` to transform the first value to the type `B` and then the
   * function `reduce` to combine the `B` value with each other `A` value.
   */
  def reduceMapRight[B](map: A => B)(reduce: (A, B) => B): B = {
    val iterator = chunk.materialize.reverse.toIterator
    var b: B     = null.asInstanceOf[B]

    while (iterator.hasNext) {
      val a = iterator.next()
      if (b == null) b = map(a) else b = reduce(a, b)
    }

    b
  }

  def forEach[F[+_]: AssociativeBoth: Covariant, B](f: A => F[B]): F[NonEmptyVector[B]] =
    reduceMapRight(f(_).map(single))((a, fas) => f(a).zipWith(fas)((h, t) => Chunk(h) +: t))

  /**
   * Converts this `NonEmptyVector` to a `Chunk`, discarding information about
   * it not being empty.
   */
  def toChunk: Chunk[A] =
    chunk

  /**
   * Converts this `NonEmptyVector` to the `::` case of a `List`.
   */
  def toCons[A1 >: A]: ::[A1] =
    ::(chunk(0), chunk.drop(1).toList)

  /**
   * Renders this `NonEmptyVector` as a `String`.
   */
  override def toString: String =
    chunk.mkString("NonEmptyVector(", ", ", ")")

  /**
   * Zips this `NonEmptyVector` with the specified `Chunk`, using the specified
   * functions to "fill in" missing values if one chunk has fewer elements
   * than the other.
   */
  def zipAllWith[B, C](
    that: Chunk[B]
  )(left: A => C, right: B => C)(both: (A, B) => C): NonEmptyVector[C] =
    nonEmpty(chunk.zipAllWith(that)(left, right)(both))

  /**
   * Zips this `NonEmptyVector` with the specified `NonEmptyVector`, only
   * keeping as many elements as are in the smaller `NonEmptyVector`.
   */
  final def zipWith[B, C](that: NonEmptyVector[B])(f: (A, B) => C): NonEmptyVector[C] =
    nonEmpty(chunk.zipWith(that.chunk)(f))

  /**
   * Annotates each element of this `NonEmptyVector` with its index.
   */
  def zipWithIndex: NonEmptyVector[(A, Int)] =
    nonEmpty(chunk.zipWithIndex)

  /**
   * Annotates each element of this `NonEmptyVector` with its index, with the
   * specified offset.
   */
  final def zipWithIndexFrom(indexOffset: Int): NonEmptyVector[(A, Int)] =
    nonEmpty(chunk.zipWithIndexFrom(indexOffset))
}

object NonEmptyVector {

  /**
   * Constructs a `NonEmptyVector` from one or more values.
   */
  def apply[A](a: A, as: A*): NonEmptyVector[A] =
    nonEmpty(Chunk(a) ++ Chunk.fromIterable(as))

  /**
   * Checks if a `chunk` is not empty and constructs a `NonEmptyVector` from it.
   */
  def fromChunk[A](chunk: Chunk[A]): Option[NonEmptyVector[A]] =
    if (chunk.isEmpty) None else Some(NonEmptyVector.nonEmpty(chunk))

  /**
   * Constructs a `NonEmptyVector` from the `::` case of a `List`.
   */
  def fromCons[A](as: ::[A]): NonEmptyVector[A] =
    as match { case h :: t => fromIterable(h, t) }

  /**
   * Constructs a `NonEmptyVector` from an `Iterable`.
   */
  def fromIterable[A](a: A, as: Iterable[A]): NonEmptyVector[A] =
    nonEmpty(Chunk.single(a) ++ Chunk.fromIterable(as))

  /**
   * Constructs a `NonEmptyVector` from an `Iterable`.
   */
  def fromIterableOption[A](iterable: Iterable[A]): Option[NonEmptyVector[A]] =
    iterable.toList match {
      case Nil    => None
      case h :: t => Some(fromIterable(h, t))
    }

  /**
   * Constructs a `NonEmptyVector` from a single value.
   */
  def single[A](a: A): NonEmptyVector[A] =
    NonEmptyVector(a)

  /**
   * Extracts the elements from a `NonEmptyVector`.
   */
  def unapplySeq[A](seq: Seq[A]): Option[Seq[A]] =
    seq match {
      case chunk: Chunk[A] if chunk.nonEmpty => Some(chunk)
      case _                                 => None
    }

  /**
   * The unit `NonEmptyVector`.
   */
  val unit: NonEmptyVector[Unit] = single(())

  // /**
  //  * Provides an implicit conversion from `NonEmptyChunk` to `Chunk` for
  //  * methods that may not return a `NonEmptyChunk`.
  //  */
  // implicit def toChunk[A](nonEmptyChunk: NonEmptyChunk[A]): Chunk[A] =
  //   nonEmptyChunk.chunk

  /**
   * Constructs a `NonEmptyVector` from a `Chunk`. This should only be used
   * when it is statically known that the `Chunk` must have at least one
   * element.
   */
  private[cilib] def nonEmpty[A](chunk: Chunk[A]): NonEmptyVector[A] =
    new NonEmptyVector(chunk)

  /**
   * The `IdentityBoth` (and thus `AssociativeBoth`) instance for `NonEmptyList`.
   */
  implicit val NonEmptyVectorIdentityBoth: IdentityBoth[NonEmptyVector] =
    new IdentityBoth[NonEmptyVector] {
      val any: NonEmptyVector[Any] = single(())
      def both[A, B](fa: => NonEmptyVector[A], fb: => NonEmptyVector[B]): NonEmptyVector[(A, B)] =
        fa.flatMap(a => fb.map(b => (a, b)))
    }

  /**
   * The `NonEmptyForEach` instance for `NonEmptyVector`.
   */
  implicit val nonEmptyVectorForEach: NonEmptyForEach[NonEmptyVector] =
    new NonEmptyForEach[NonEmptyVector] {
      def forEach1[G[+_]: AssociativeBoth: Covariant, A, B](fa: NonEmptyVector[A])(f: A => G[B]): G[NonEmptyVector[B]] =
        fa.forEach(f)
    }

  implicit def nonEmptyVectorEqual[A: zio.prelude.Equal]: zio.prelude.Equal[NonEmptyVector[A]] =
    zio.prelude.Equal.make(_.equals(_))
}
