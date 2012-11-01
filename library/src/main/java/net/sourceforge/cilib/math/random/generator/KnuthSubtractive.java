/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math.random.generator;

import net.sourceforge.cilib.math.random.generator.seeder.Seeder;

/**
 * An implementation of Knuth's subtractive random number generator.
 * This generator is relatively fast but is not considered simulation quality.
 *
 * <p>
 * This code is based on the implementation in GSL (GNU Scientific Library)
 * which is also covered by the GNU General Public License. The original C
 * source code is Copyright (C) 1996, 1997, 1998, 1999, 2000 James Theiler
 * and Brian Gough.
 *
 */
public class KnuthSubtractive implements RandomProvider {
    private static final long serialVersionUID = 8124520969303604479L;
    private final long seed;

    /**
     * Create an instance of {@linkplain KnuthSubtractive}.
     */
    public KnuthSubtractive() {
        this.seed = Seeder.getSeed();
    }

    /**
     * Create an instance, with the given <code>seed</code> value.
     * @param seed The seed value.
     */
    public KnuthSubtractive(long seed) {
        this.seed = seed;
    }

    /**
     * {@inheritDoc}
     */
    private void setSeed(long seed) {
        buffer = new long[56];

        if (seed == 0) {
            seed = 1;
        }

        long j = (M_SEED - seed) % M_BIG;

        buffer[0] = 0;
        buffer[55] = j;

        long k = 1;
        for (int i = 1; i < 55; ++i) {
            int n = (21 * i) % 55;
            buffer[n] = k;
            k = j - k;
            if (k < 0) {
                k += M_BIG;
            }
            j = buffer[n];
        }

        for (int i1 = 0; i1 < 4; ++i1) {
            for (int i = 1; i < 56; ++i) {
                long t = buffer[i] - buffer[1 + (i + 30) % 55];
                if (t < 0) {
                    t += M_BIG;
                }
                buffer[i] = t;
            }
        }

        x = 0;
        y = 31;
    }

    /**
     * {@inheritDoc}
     */
    private int next(int bits) {
        if (buffer == null)
            setSeed(seed);

        ++x;
        if (x == 56) {
            x = 1;
        }
        ++y;
        if (y == 56) {
            y = 1;
        }

        long j = buffer[x] - buffer[y];
        if (j < 0) {
            j += M_BIG;
        }
        buffer[x] = j;

        return (int) ((j & 0xffffffffL) >>> (32 - bits));
    }

    private static final long M_BIG = 0xffffffffL;
    private static final long M_SEED = 161803398;

    private int x;
    private int y;
    private long[] buffer;

    @Override
    public boolean nextBoolean() {
        return next(1) != 0;
    }

    @Override
    public int nextInt() {
        return next(32);
    }

    @Override
    public int nextInt(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n must be positive");

        if ((n & -n) == n)  // i.e., n is a power of 2
            return (int)((n * (long)next(31)) >> 31);

        int bits, val;
        do {
            bits = next(31);
            val = bits % n;
        } while (bits - val + (n-1) < 0);
        return val;
    }

    @Override
    public long nextLong() {
        return ((long)(next(32)) << 32) + next(32);
    }

    @Override
    public float nextFloat() {
        return next(24) / ((float)(1 << 24));
    }

    @Override
    public double nextDouble() {
        return (((long)next(26) << 27) + next(27)) / (double)(1L << 53);
    }

    @Override
    public void nextBytes(byte[] bytes) {
       for (int i = 0, len = bytes.length; i < len; )
            for (int rnd = nextInt(),
                     n = Math.min(len - i, Integer.SIZE/Byte.SIZE);
                 n-- > 0; rnd >>= Byte.SIZE)
                bytes[i++] = (byte)rnd;
    }

}
