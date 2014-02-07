package cilib

trait RNG {
  val seed: Long
  def next(bits: Int): (RNG, Int)
}

final class CMWC(val seed: Long, carry: Long, index: Int, state: Array[Long]) extends RNG {
  private val A = 18782L

  def next(bits: Int) = {
    val s = state.clone
    val i: Int = (index + 1) & 4095
    val t: Long = A * s(i) + carry //carry
    // carry = t / b (done in an unsigned way)
    val div32: Long = t >>> 32 // div32 = t / (b+1)
    val newCarry: Long = div32 + (if ((t & 0xFFFFFFFFL) >= 0xFFFFFFFFL - div32) 1L else 0L)
    // seeds[n] = (b-1)-t%b (done in an unsigned way)
    s(i) = 0xFFFFFFFEL - (t & 0xFFFFFFFFL) - (newCarry - div32 << 32) - newCarry & 0xFFFFFFFFL
    val result: Long = s(i)
    // n = n + 1 & r - 1;
    (new CMWC(seed, newCarry, i, s), (result >>> 32 - bits).toInt)
  }
}

object RNG {

  def init(seed: Long = System.currentTimeMillis): RNG = {
    val PHI = 0x9e3779b9
    val carry = 362436L
    val index = 4095

    val state = new Array[Long](4096)
    state(0) = seed
    state(1) = seed + PHI
    state(2) = seed + PHI + PHI
    (3 until 4096).foreach(x => state(x) = state(x - 3) ^ state(x - 2) ^ PHI ^ x)

    new CMWC(seed, carry, index, state)
  }

  def split(r: RNG) =
    (init(r.seed - 1), init(r.seed + 1))

}
