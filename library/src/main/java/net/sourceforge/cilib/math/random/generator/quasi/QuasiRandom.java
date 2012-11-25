/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math.random.generator.quasi;

/**
 * TODO: Need to complete javadoc.
 *
 */
public abstract class QuasiRandom {
    private static final long serialVersionUID = -1631441422804523649L;
    private final long seed;

    protected int dimensions;
    protected int skipValue;

    public QuasiRandom(long seed) {
        this.seed = seed;
        this.dimensions = 3;
        this.skipValue = 0;
    }

    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    public int getDimensions() {
        return this.dimensions;
    }

    public void setSkipValue(int skipValue) {
        this.skipValue = skipValue;
    }

    public int getSkipValue() {
        return this.skipValue;
    }

    public abstract double[] nextPoint();
}
