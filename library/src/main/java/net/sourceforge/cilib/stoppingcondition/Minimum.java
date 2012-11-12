/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.stoppingcondition;

/**
 * A stopping predicate used to stop an algorithm when a measurement is less than or equal to a value.
 */
public class Minimum implements CompletionCalculator {
    
    private double percentage;
    private double maxValue;
    
    public Minimum() {
        this.percentage = 0.0;
        this.maxValue = -Double.MAX_VALUE;
    }

    @Override
    public double getPercentage(double actualValue, double targetValue) {
        maxValue = Math.max(actualValue, maxValue);
        percentage = Math.max(percentage, 1.0 - (actualValue - targetValue) / (maxValue -  targetValue));
        
        return Math.max(Math.min(percentage, 1.0), 0.0);
    }

    @Override
    public boolean apply(double actualValue, double targetValue) {
        return actualValue <= targetValue;
    }
}
