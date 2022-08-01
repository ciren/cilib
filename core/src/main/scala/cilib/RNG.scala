package cilib

import _root_.scala.Predef.{ any2stringadd => _ }

sealed trait RNG {

  /** The seed that initialised the RNG instance */
  val seed: Long

  /** Obtain the next `n` bits from the generator, producing an Int and the updated RNG state */
  def next(bits: Int): (RNG, Int)
}

private final class CMWC(val seed: Long, carry: Long, index: Int, state: Vector[Long]) extends RNG {
  val multiplier = 18782L // 1030770L
  val r          = 4096L

  def next(bits: Int): (CMWC, Int) = {
    val t: Long     = multiplier * state(index) + carry
    // carry = t / b (done in an unsigned way)
    val div32: Long = t >>> 32 // div32 = t / (b+1)
    val newCarry    = div32 + (if ((t & 0xffffffffL) >= 0xffffffffL - div32) 1L else 0L)
    // seeds[n] = (b-1)-t%b (done in an unsigned way)
    val result      = 0xfffffffeL - (t & 0xffffffffL) - (newCarry - div32 << 32) - newCarry & 0xffffffffL
    val updated     = state.updated(index, result)

    (new CMWC(seed, newCarry, (index + 1 & r - 1).toInt, updated), (result >>> 32 - bits).toInt)
  }
}

object RNG {

  /**
   * Create an `RNG` instance seeded weith the current systemj time.
   *
   * '''NOTE''': even though this function exists, it does so purely to
   * allow for unpredictable results. It is _STRONGLY_ advised not to
   * use this function for any real uses of the library.
   */
  def fromTime: RNG =
    init(System.currentTimeMillis)

  /**
   * Generate a new `RNG` instance.
   *
   * The `seed` value is used as the seed for a
   * [[https://en.wikipedia.org/wiki/Linear_congruential_generator
   * LCG generator]], which is sampled for a seed in order to create
   * a
   * [[https://en.wikipedia.org/wiki/Multiply-with-carry_pseudorandom_number_generator
   * `CMWC` instance]]. The CMWC generator is not only a fast
   * generator of randomness, but it is a generator that has very
   * strong generation capabilities, such as having a very long
   * period. CIlib uses the CMWC generator by default for all
   * randomness because it is a better generator than the popular
   * Mersenne Twister.
   */
  def init(seed: Long): RNG = {
    val seedGen      = initLCG(seed)
    val seedGenState = RVar.next[Int].map(_ & 0xffffffffL).replicateM(4097).runResult(seedGen).toVector

    new CMWC(seed, seedGenState(4096), 4095, seedGenState.take(4096))
  }

  /**
   * Generate `n` unique `RNG` instances,  applying the `init` effect repeatedly.
   *
   * The generator of the multiple `RNG`s samples an LCG generator
   * `n` times and passes the generated seeds to the `init` function.
   */
  def initN(n: Int, seed: Long): List[RNG] =
    RVar.next[Long].replicateM(n).map(_.map(init)).runResult(initLCG(seed)).toList

  private final class LCG(val seed: Long) extends RNG {
    def next(bits: Int): (RNG, Int) = {
      val next = (seed * 25214903917L + 11L) & ((1L << 48) - 1)
      (new LCG(next), (next >>> (48 - bits)).toInt)
    }
  }

  private def initLCG(seed: Long): RNG =
    new LCG((seed ^ 0x5deece66dL) & ((1L << 48) - 1))

  /** Split a given RNG into two by adjusting the seed */
  def split(r: RNG): (RNG, RNG) =
    (init(r.seed - 1), init(r.seed + 1))

}
