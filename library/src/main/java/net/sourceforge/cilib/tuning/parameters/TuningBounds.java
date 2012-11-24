/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.tuning.parameters;

/**
 * Bounds used for tuning parameters with bounds.
 */
public class TuningBounds {
    
    private double lowerBound;
    private double upperBound;
    
    public TuningBounds() {
        this.lowerBound = 0.0;
        this.upperBound = 1.0;
    }
    
    public TuningBounds(double lBound, double uBound) {
        this.lowerBound = lBound;
        this.upperBound = uBound;
    }

    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }

    public double getLowerBound() {
        return lowerBound;
    }
    
    public double getRange() {
        return upperBound - lowerBound;
    }
}
