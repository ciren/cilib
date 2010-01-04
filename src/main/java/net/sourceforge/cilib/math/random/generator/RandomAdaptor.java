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
 *
 * @author gpampara
 */
public final class RandomAdaptor extends java.util.Random {
    private static final long serialVersionUID = 9072860165404102971L;
    private final RandomProvider delegate;

    private double nextNextGaussian;
    private boolean haveNextNextGaussian = false;

    public RandomAdaptor(RandomProvider provider) {
        this.delegate = provider;
    }

    @Override
    public boolean nextBoolean() {
        return delegate.nextBoolean();
    }

    @Override
    public void nextBytes(byte[] bytes) {
        delegate.nextBytes(bytes);
    }

    @Override
    public double nextDouble() {
        return delegate.nextDouble();
    }

    @Override
    public float nextFloat() {
        return delegate.nextFloat();
    }

    @Override
    public synchronized double nextGaussian() {
        // See Knuth, ACP, Section 3.4.1 Algorithm C.
        if (haveNextNextGaussian) {
            haveNextNextGaussian = false;
            return nextNextGaussian;
        } else {
            double v1, v2, s;
            do {
                v1 = 2 * nextDouble() - 1; // between -1 and 1
                v2 = 2 * nextDouble() - 1; // between -1 and 1
                s = v1 * v1 + v2 * v2;
            } while (s >= 1 || s == 0);
            double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s)/s);
            nextNextGaussian = v2 * multiplier;
            haveNextNextGaussian = true;
            return v1 * multiplier;
        }
    }

    @Override
    public int nextInt() {
        return delegate.nextInt();
    }

    @Override
    public int nextInt(int n) {
        return delegate.nextInt(n);
    }

    @Override
    public long nextLong() {
        return delegate.nextLong();
    }

    @Override
    public synchronized void setSeed(long seed) {
        throw new UnsupportedOperationException("Cannot redefine the seed value. Please recreate the instance with the specified seed.");
    }

}
