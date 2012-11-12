/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.stoppingcondition;

/**
 * An interface defining the predicate to use with a MeasuredStoppingCondition.
 */
public interface CompletionCalculator {
    
    public double getPercentage(double actualValue, double targetValue);
    
    public boolean apply(double actualValue, double targetValue);
}
