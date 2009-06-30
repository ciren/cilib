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
 * An implementation of Knuth's subtractive random number generator.
 * This generator is relatively fast but is not considered simulation quality.
 *
 * <p>
 * This code is based on the implementation in GSL (GNU Scientific Library)
 * which is also covered by the GNU General Public License. The original C
 * source code is Copyright (C) 1996, 1997, 1998, 1999, 2000 James Theiler
 * and Brian Gough.
 *
 * @author  Edwin Peer
 */
public class KnuthSubtractive extends Random {

    private static final long serialVersionUID = 8124520969303604479L;

    /**
     * Create an instance of {@linkplain KnuthSubtractive}.
     */
    public KnuthSubtractive() {
        super(Seeder.getSeed());
    }

    /**
     * Create an instance, with the given <code>seed</code> value.
     * @param seed The seed value.
     */
    public KnuthSubtractive(long seed) {
        super(seed);
    }

    /**
     * {@inheritDoc}
     */
    public KnuthSubtractive getClone() {
        return new KnuthSubtractive();
    }

    /**
     * {@inheritDoc}
     */
    public void setSeed(long seed) {
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
    protected int next(int bits) {
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

}
