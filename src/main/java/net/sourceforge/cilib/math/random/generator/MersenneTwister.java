/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
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
 * @author  Edwin Peer
 */
public class MersenneTwister extends Random {

    private static final long serialVersionUID = -4165908582605023476L;

    /**
     * Default Constructor. Initialises the {@linkplain MersenneTwister} with the
     * seed value from {@link Seeder#getSeed()}.
     */
    public MersenneTwister() {
        super(Seeder.getSeed());
    }

    /**
     * Create a {@linkplain MersenneTwister} with the given seed value.
     * @param seed The initial seed value to use.
     */
    public MersenneTwister(long seed) {
        super(seed);
    }

    /**
     * Copy constructor. Create an instance with the same seed as the given
     * instance.
     * @param copy The instance to copy.
     */
    public MersenneTwister(MersenneTwister copy) {
        super(Seeder.getSeed());
    }

    /**
     * Get a cloned instance of the current object.
     * @return The cloned instance.
     */
    @Override
    public MersenneTwister getClone() {
        return new MersenneTwister(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSeed(long seed) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected int next(int bits) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public double nextDouble() {
       double result = (((long)next(26) << 27) + next(27)) / (double)(1L << 53);
       index--;
       return result;
    }

    private void magic(long y, long[] data, int i) {
        if ((y & 0x1L) == 1L)
            data[i] ^= 0x9908b0dfL;
    }

    private long[] data;
    private int index;

    private static final int N = 624;
    private static final int M = 397;
    private static final long UPPER_MASK = 0x80000000L;
    private static final long LOWER_MASK = 0x7fffffffL;
}
