package cilib

import _root_.scala.Predef.{any2stringadd => _}

sealed trait RNG {
  val seed: Long
  def next(bits: Int): (RNG, Int)
}

private final class CMWC(val seed: Long, carry: Long, index: Int, state: Vector[Long]) extends RNG {
  val multiplier = 18782L //1030770L
  val r = 4096L

  def next(bits: Int) = {
    val t: Long = multiplier * state(index) + carry
    // carry = t / b (done in an unsigned way)
    val div32: Long = t >>> 32 // div32 = t / (b+1)
    val newCarry = div32 + (if ((t & 0xFFFFFFFFL) >= 0xFFFFFFFFL - div32) 1L else 0L)
    // seeds[n] = (b-1)-t%b (done in an unsigned way)
    val result = 0xFFFFFFFEL - (t & 0xFFFFFFFFL) - (newCarry - div32 << 32) - newCarry & 0xFFFFFFFFL
    val updated = state.updated(index, result)

    (new CMWC(seed, newCarry, (index + 1 & r - 1).toInt, updated), (result >>> 32 - bits).toInt)
  }
}

object RNG {
  import scalaz._, Scalaz._

  def fromTime: RNG =
    init(System.currentTimeMillis)

  def init(seed: Long): RNG = {
    val seedGen = initLCG(seed)
    val seedGenState = RVar.next[Int].map(_ & 0xFFFFFFFFL).replicateM(4097).run(seedGen)._2

    new CMWC(seed, seedGenState(4096), 4095, seedGenState.take(4096).toVector)
  }

  def initN(n: Int, seed: Long): List[RNG] =
    RVar.next[Long].replicateM(n).map(_.map(init)).run(initLCG(seed))._2

  private final class LCG(val seed: Long) extends RNG {
    def next(bits: Int): (RNG, Int) = {
      val next = (seed * 25214903917L + 11L) & ((1L << 48) - 1)
      (new LCG(next), (next >>> (48 - bits)).toInt)
    }
  }

  private def initLCG(seed: Long): RNG =
    new LCG((seed ^ 0x5DEECE66DL) & ((1L << 48) - 1))

  def split(r: RNG): (RNG, RNG) =
    (init(r.seed - 1), init(r.seed + 1))

}
