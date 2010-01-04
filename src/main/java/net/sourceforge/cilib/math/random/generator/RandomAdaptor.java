/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
