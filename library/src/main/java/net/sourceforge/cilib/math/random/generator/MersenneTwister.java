/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math.random.generator;

/**
 * <p>
 * This is an implementation of the MT19937 random number generator.
 * </p><p>
 * The MT19937 generator of Makoto Matsumoto and Takuji Nishimura is a
 * variant of the twisted generalized feedback shift-register
 * algorithm, and is known as the "Mersenne Twister" generator.  It
 * has a Mersenne prime period of 2^19937 - 1 (about 10^6000) and is
 * equi-distributed in 623 dimensions.  It has passed the DIEHARD
 * statistical tests.  It uses 624 words of state per generator and is
 * comparable in speed to the other simulation quality generators.  The original
 * generator used a default seed of 4357 and setting the seed equal to zero
 * </p><p align="right">- Brian Gough</p><p>
 * reproduces this.
 * References:
 * <ul><li>
 * Makoto Matsumoto and Takuji Nishimura, "Mersenne Twister: A
 * 623-dimensionally equidistributed uniform pseudorandom number
 * generator". `ACM Transactions on Modeling and Computer
 * Simulation', Vol. 8, No. 1 (Jan. 1998), Pages 3-30
 * </li></ul></p>
 *
 * <p>
 * This code is based on the implementation in GSL (GNU Scientific Library)
 * which is also covered by the GNU General Public License. The original C
 * source code is Copyright (C) 1998 Brian Gough.
 *
 * Comment text ripped from GSL.
 *
 */
class MersenneTwister {

    private static final long serialVersionUID = -4165908582605023476L;
    private final long seed;
    private long[] data;
    private int index;
    private static final int N = 624;
    private static final int M = 397;
    private static final long UPPER_MASK = 0x80000000L;
    private static final long LOWER_MASK = 0x7fffffffL;

    /**
     * Create a {@linkplain MersenneTwister} with the given seed value.
     * @param seed The initial seed value to use.
     */
    public MersenneTwister(long seed) {
        this.seed = seed;
    }

    /**
     * {@inheritDoc}
     */
    private void setSeed(long seed) {
        data = new long[N];

        if (seed == 0) {
            seed = 5489L;
        }

        data[0] = seed & 0xffffffffL;

        for (index = 1; index < N; ++index) {
            data[index] = (1812433253L * (data[index - 1] ^ (data[index - 1] >>> 30)) + index);
            data[index] &= 0xffffffffL;
        }
    }

    private int next(int bits) {
        if (data == null) {
            setSeed(seed);
        }

        if (index >= N) {
            int i;
            for (i = 0; i < N - M; ++i) {
                long y = (data[i] & UPPER_MASK) | (data[i + 1] & LOWER_MASK);
                data[i] = data[i + M] ^ (y >>> 1);
                magic(y, data, i);
            }
            for (; i < N - 1; ++i) {
                long y = (data[i] & UPPER_MASK) | (data[i + 1] & LOWER_MASK);
                data[i] = data[i + (M - N)] ^ (y >>> 1);
                magic(y, data, i);
            }
            long y = (data[N - 1] & UPPER_MASK) | (data[0] & LOWER_MASK);
            data[N - 1] = data[M - 1] ^ (y >>> 1);
            magic(y, data, i);

            index = 0;
        }

        long k = data[index];
        k ^= (k >>> 11);
        k ^= (k << 7) & 0x9d2c5680L;
        k ^= (k << 15) & 0xefc60000L;
        k ^= (k >>> 18);

        ++index;

        return (int) ((k & 0xffffffffL) >>> (32 - bits));
    }

    public double nextDouble() {
        double result = (((long) next(26) << 27) + next(27)) / (double) (1L << 53);
        index--;
        return result;
    }

    private void magic(long y, long[] data, int i) {
        if ((y & 0x1L) == 1L) {
            data[i] ^= 0x9908b0dfL;
        }
    }
    
    public boolean nextBoolean() {
        return next(1) != 0;
    }

    public int nextInt() {
        return next(32);
    }

    public int nextInt(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }

        if ((n & -n) == n) // i.e., n is a power of 2
        {
            return (int) ((n * (long) next(31)) >> 31);
        }

        int bits, val;
        do {
            bits = next(31);
            val = bits % n;
        } while (bits - val + (n - 1) < 0);
        return val;
    }

    public long nextLong() {
        return ((long) (next(32)) << 32) + next(32);
    }

    public float nextFloat() {
        return next(24) / ((float) (1 << 24));
    }

    public void nextBytes(byte[] bytes) {
        for (int i = 0, len = bytes.length; i < len;) {
            for (int rnd = nextInt(),
                    n = Math.min(len - i, Integer.SIZE / Byte.SIZE);
                    n-- > 0; rnd >>= Byte.SIZE) {
                bytes[i++] = (byte) rnd;
            }
        }
    }

    public long getSeed() {
        return seed;
    }
}
