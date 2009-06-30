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
 * This is an implementation of Martin Luescher's second generation
 * double-precision (48-bit) version of the RANLUX generator.
 * </p><p>
 * It uses a lagged-fibonacci-with-skipping algorithm to produce "luxury random
 * numbers". The period of the generator is about 10^171.  The algorithm has
 * mathematically proven properties and it can provide truly decorrelated
 * numbers at a known level of randomness.
 * </p><p>
 * This generator is slow.
 * </p><p>
 * References:
 * <ul><li>
 * M. Lu"scher, "A portable high-quality random number generator
 * for lattice field theory calculations", `Computer Physics
 * Communications', 79 (1994) 100-110.
 * </li><li>
 * F. James, "RANLUX: A Fortran implementation of the
 * high-quality pseudo-random number generator of Lu"scher",
 * Computer Physics Communications', 79 (1994) 111-114
 * </li></ul></p>
 *
 * <p>
 * This code is based on the implementation in GSL (GNU Scientific Library)
 * which is also covered by the GNU General Public License. The original C
 * source code is Copyright (C) 1996, 1997, 1998, 1999, 2000 James Theiler
 * and Brian Gough.
 *
 * Comment text ripped from GSL.
 *
 * @author  Edwin Peer
 */
public class RANLUX extends Random {

    private static final long serialVersionUID = -2393841490133897078L;

    /**
     * Create a new instance of {@linkplain RANLUX}.
     */
    public RANLUX() {
        super(Seeder.getSeed());
    }

    /**
     * Create a new instance of {@linkplain RANLUX} with the provided <code>seed</code>
     * value.
     * @param seed The initial value for the seed.
     */
    public RANLUX(long seed) {
        super(seed);
    }

    /**
     * {@inheritDoc}
     */
    public RANLUX getClone() {
        return new RANLUX();
    }

    /**
     * {@inheritDoc}
     */
    public void setSeed(long seed) {
        xdbl = new double[12];

        if (seed == 0) {
            seed = 1;
        }

        long i = seed & 0xffffffffL;

        int[] xbit = new int[31];
        for (int k = 0; k < 31; ++k) {
            xbit[k] = (int) (i & 1L);
            i /= 2;
        }

        int ibit = 0;
        int jbit = 18;

        for (int k = 0; k < 12; ++k) {
            double x = 0;

            for (int l = 1; l <= 48; ++l) {
                double y = (double) (xbit[ibit] ^ 1);
                x += x + y;
                xbit[ibit] = xbit[ibit] ^ xbit[jbit];
                ibit = (ibit + 1) % 31;
                jbit = (jbit + 1) % 31;
            }
            xdbl[k] = ONE_BIT * x;
        }

        carry = 0;
        ir = 11;
        jr = 7;
        irOld = 0;
    }

    private void increment() {
        int k, kmax;
        double y1, y2, y3;

        for (k = 0; ir > 0; ++k) {
            y1 = xdbl[jr] - xdbl[ir];
            y2 = y1 - carry;
            if (y2 < 0) {
                carry = ONE_BIT;
                y2 += 1;
            }
            else {
                carry = 0;
            }
            xdbl[ir] = y2;
            ir = NEXT[ir];
            jr = NEXT[jr];
        }

        kmax = LUXURY - 12;

        for (; k <= kmax; k += 12) {
            y1 = xdbl[7] - xdbl[0];
            y1 -= carry;

            // RANLUX_STEP (y2, y1, 8, 1, 0);
            y2 = xdbl[8] - xdbl[1];
            if (y1 < 0) {
                y2 -= ONE_BIT;
                y1 += 1;
            }
            xdbl[0] = y1;

            // RANLUX_STEP (y3, y2, 9, 2, 1);
            y3 = xdbl[9] - xdbl[2];
            if (y2 < 0) {
                y3 -= ONE_BIT;
                y2 += 1;
            }
            xdbl[1] = y2;

            // RANLUX_STEP (y1, y3, 10, 3, 2);
            y1 = xdbl[10] - xdbl[3];
            if (y3 < 0) {
                y1 -= ONE_BIT;
                y3 += 1;
            }
            xdbl[2] = y3;

            // RANLUX_STEP (y2, y1, 11, 4, 3);
            y2 = xdbl[11] - xdbl[4];
            if (y1 < 0) {
                y2 -= ONE_BIT;
                y1 += 1;
            }
            xdbl[3] = y1;

            // RANLUX_STEP (y3, y2, 0, 5, 4);
            y3 = xdbl[0] - xdbl[5];
            if (y2 < 0) {
                y3 -= ONE_BIT;
                y2 += 1;
            }
            xdbl[4] = y2;

            // RANLUX_STEP (y1, y3, 1, 6, 5);
            y1 = xdbl[1] - xdbl[6];
            if (y3 < 0) {
                y1 -= ONE_BIT;
                y3 += 1;
            }
            xdbl[5] = y3;

            // RANLUX_STEP (y2, y1, 2, 7, 6);
            y2 = xdbl[2] - xdbl[7];
            if (y1 < 0) {
                y2 -= ONE_BIT;
                y1 += 1;
            }
            xdbl[6] = y1;

            // RANLUX_STEP (y3, y2, 3, 8, 7);
            y3 = xdbl[3] - xdbl[8];
            if (y2 < 0) {
                y3 -= ONE_BIT;
                y2 += 1;
            }
            xdbl[7] = y2;

            // RANLUX_STEP (y1, y3, 4, 9, 8);
            y1 = xdbl[4] - xdbl[9];
            if (y3 < 0) {
                y1 -= ONE_BIT;
                y3 += 1;
            }
            xdbl[8] = y3;

            // RANLUX_STEP (y2, y1, 5, 10, 9);
            y2 = xdbl[5] - xdbl[10];
            if (y1 < 0) {
                y2 -= ONE_BIT;
                y1 += 1;
            }
            xdbl[9] = y1;

            // RANLUX_STEP (y3, y2, 6, 11, 10);
            y3 = xdbl[6] - xdbl[11];
            if (y2 < 0) {
                y3 -= ONE_BIT;
                y2 += 1;
            }
            xdbl[10] = y2;

            if (y3 < 0) {
                carry = ONE_BIT;
                y3 += 1;
            }
            else {
                carry = 0;
            }
            xdbl[11] = y3;
        }

        kmax = LUXURY;

        for (; k < kmax; ++k) {
            y1 = xdbl[jr] - xdbl[ir];
            y2 = y1 - carry;
            if (y2 < 0) {
                carry = ONE_BIT;
                y2 += 1;
            }
            else {
                carry = 0;
            }
            xdbl[ir] = y2;
            ir = NEXT[ir];
            jr = NEXT[jr];
        }

        irOld = ir;
    }

    /**
     * {@inheritDoc}
     */
    protected int next(int bits) {
        ir = NEXT[ir];

        if (ir == irOld) {
            increment();
        }

        return (int) (((long) (xdbl[ir] * 4294967296.0) & 0xffffffffL) >>> (32 - bits));

    }

    private double[] xdbl;
    private double carry;
    private int ir;
    private int jr;
    private int irOld;

    private static final int LUXURY = 397;
    private static final double ONE_BIT = 1.0 / 281474976710656.0;
    private static final int[] NEXT = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 0};
}
