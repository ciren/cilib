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
 * The GFSR4 generator is like a lagged-fibonacci generator, and
 * produces each number as an `xor''d sum of four previous values.
 * </p><pre>
 *     r_n = r_{n-A} ^^ r_{n-B} ^^ r_{n-C} ^^ r_{n-D}
 * </pre><p>
 * Ziff notes that "it is now widely known" that two-tap registers have serious
 * flaws, the most obvious one being the three-point correlation that
 * comes from the definition of the generator.  Nice mathematical
 * properties can be derived for GFSR's, and numerics bears out the
 * claim that 4-tap GFSR's with appropriately chosen offsets are as
 * random as can be measured, using the author's test.
 * </p><p>
 * This implementation uses the values suggested the the example on
 * p392 of Ziff's article: A=471, B=1586, C=6988, D=9689
 * </p><p>
 * If the offsets are appropriately chosen (such the one ones in this
 * implementation), then the sequence is said to be maximal.  I'm not
 * sure what that means, but I would guess that means all states are
 * part of the same cycle, which would mean that the period for this
 * generator is astronomical; it is (2^K)^D approx 10^{93334} where
 * K=32 is the number of bits in the word, and D is the longest lag.
 * This would also mean that any one random number could easily be
 * zero; ie 0 <= r < 2^32.
 * </p><p>
 * Ziff doesn't say so, but it seems to me that the bits are
 * completely independent here, so one could use this as an efficient
 * bit generator; each number supplying 32 random bits.  The quality
 * of the generated bits depends on the underlying seeding procedure,
 * which may need to be improved in some circumstances.
 * <p align="right">- James Theiler</p><p>
 * References:
 * <ul><li>
 * Robert M. Ziff, "Four-tap shift-register-sequence
 * random-number generators", `Computers in Physics', 12(4),
 * Jul/Aug 1998, pp 385-392.
 * </li></ul></p>
 * <p>
 * This code is based on the implementation in GSL (GNU Scientific Library)
 * which is also covered by the GNU General Public License. The original C
 * source code is Copyright (C) 1998 James Theiler
 *
 * @author  Edwin Peer
 */
public class ZiffGFSR4 extends Random {

    private static final long serialVersionUID = -1714226372864316570L;

    /**
     * Create an instance of {@linkplain ZiffGFSR4}.
     */
    public ZiffGFSR4() {
        super(Seeder.getSeed());
    }

    /**
     * Create an instance with the given seed value.
     * @param seed The seed value to use.
     */
    public ZiffGFSR4(long seed) {
        super(seed);
    }

    /**
     * {@inheritDoc}
     */
    public ZiffGFSR4 getClone() {
        return new ZiffGFSR4();
    }

    private long getLCG(long n) {
        return (69069 * n) & 0xffffffffL;
    }

    /**
     * {@inheritDoc}
     */
    public void setSeed(long seed) {
        ra = new long[M + 1];

        if (seed == 0) {
            seed = 4357;
        }

        long msb = 0x80000000L;
        long mask = 0xffffffffL;

        for (int i = 0; i <= M; ++i) {
            long t = 0;
            long bit = msb;
            for (int j = 0; j < 32; ++j) {
                seed = getLCG(seed);
                if ((seed & msb) == msb) {
                    t |= bit;
                }
                bit >>>= 1;
            }
            ra[i] = t;
        }

        for (int i = 0; i < 32; ++i) {
            int k = 7 + i * 3;
            ra[k] &= mask;
            ra[k] |= msb;
            mask >>>= 1;
            msb >>>= 1;
        }

        nd = 32;
    }

    /**
     * {@inheritDoc}
     */
    protected int next(int bits) {
        nd = (nd + 1) & M;

        ra[nd] = ra[(nd + M + 1 - A) & M] ^
                 ra[(nd + M + 1 - B) & M] ^
                 ra[(nd + M + 1 - C) & M] ^
                 ra[(nd + M + 1 - D) & M];

        return (int) (ra[nd] >>> (32 - bits));
    }

    private static final int A = 471;
    private static final int B = 1586;
    private static final int C = 6988;
    private static final int D = 9689;
    private static final int M = 16383;

    private int nd;
    private long[] ra;

}
