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
 * This is a maximally equidistributed combined Tausworthe generator
 * by L'Ecuyer.
 * </p><p>
 * The period of this generator is 2^88 (about 10^26).  It uses 3
 * words of state per generator.
 * </p><p>
 * References:
 * <ul><li>
 * P. L'Ecuyer, "Maximally Equidistributed Combined Tausworthe
 * Generators", `Mathematics of Computation', 65, 213 (1996),
 * 203-213.
 * </li><li>
 *  P. L'Ecuyer, "Tables of Maximally Equidistributed Combined
 *  LFSR Generators", `Mathematics of Computation', 68, 225
 *  (1999), 261-269
 * </li></ul></p>
 *
 * This code is based on the implementation in GSL (GNU Scientific Library)
 * which is also covered by the GNU General Public License. The original C
 * source code is Copyright (C) 1996, 1997, 1998, 1999, 2000 James Theiler
 * and Brian Gough.
 *
 * Comment text ripped from GSL.
 *
 * @author  Edwin Peer
 */
public class Tausworthe implements RandomProvider {
    private static final long serialVersionUID = -2863057390167225361L;
    private final long seed;

    private long s1;
    private long s2;
    private long s3;

    public Tausworthe() {
        seed = Seeder.getSeed();
        setSeed(seed); // Not the best code.... nothing should happen in a constructor.
    }

    public Tausworthe(long seed) {
        this.seed = seed;
        setSeed(seed); // Not the best code.... nothing should happen in a constructor.
    }

    private long getLCG(long n) {
        return (69069 * n) & 0xffffffffL;
    }

    private void setSeed(long seed) {
        if (seed == 0) {
            seed = 1;
        }

        s1 = getLCG(seed);
        if (s1 < 2) {
            s1 += 2;
        }
        s2 = getLCG(s1);
        if (s2 < 8) {
            s2 += 8;
        }
        s3 = getLCG(s2);
        if (s3 < 16) {
            s3 += 16;
        }

        next(32);
        next(32);
        next(32);
        next(32);
        next(32);
        next(32);
    }

    private int next(int bits) {
        s1 = (((s1 & 4294967294L) << 12) & 0xffffffffL) ^ ((((s1 << 13) & 0xffffffffL) ^ s1) >>> 19);
        s2 = (((s2 & 4294967288L) << 4) & 0xffffffffL) ^ ((((s2 << 2) & 0xffffffffL) ^ s2) >>> 25);
        s3 = (((s3 & 4294967280L) << 17) & 0xffffffffL) ^ ((((s3 << 3) & 0xffffffffL) ^ s3) >>> 11);

        return (int) ((s1 ^ s2 ^ s3) >>> (32 - bits));
    }

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

    @Override
    public long nextLong() {
        return ((long) (next(32)) << 32) + next(32);
    }

    @Override
    public float nextFloat() {
        return next(24) / ((float) (1 << 24));
    }

    @Override
    public double nextDouble() {
        return (((long) next(26) << 27) + next(27)) / (double) (1L << 53);
    }

    @Override
    public void nextBytes(byte[] bytes) {
        for (int i = 0, len = bytes.length; i < len;) {
            for (int rnd = nextInt(),
                    n = Math.min(len - i, Integer.SIZE / Byte.SIZE);
                    n-- > 0; rnd >>= Byte.SIZE) {
                bytes[i++] = (byte) rnd;
            }
        }
    }
}
