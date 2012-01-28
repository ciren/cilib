/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sourceforge.cilib.controlparameter;

import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.math.random.UniformDistribution;

/**
 * A control parameter that provides uniformly-distributed random parameter values.
 * 
 * @author bennie
 */
public class UniformControlParameter implements BoundedControlParameter {
    protected ProbabilityDistributionFuction distribution;
    protected double lower, upper;

    public UniformControlParameter() {
        this.distribution = new UniformDistribution();
        this.lower = 0.0;
        this.upper = 1.0;
    }

    public UniformControlParameter(ProbabilityDistributionFuction distribution) {
        this.distribution = distribution;
        this.lower = 0.0;
        this.upper = 1.0;
    }

    @Override
    public ControlParameter getClone() {
        return new UniformControlParameter();
    }

    @Override
    public double getParameter() {
        return distribution.getRandomNumber(lower, upper);
    }

    @Override
    public double getParameter(double min, double max) {
        return distribution.getRandomNumber(min, max);
    }

    @Override
    public void setParameter(double value) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void updateParameter() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public double getLowerBound() {
        return lower;
    }

    @Override
    public double getUpperBound() {
        return upper;
    }

    @Override
    public void setLowerBound(double lower) {
        this.lower = lower;
    }

    @Override
    public void setRange(String range) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setUpperBound(double upper) {
        this.upper = upper;
    }

    public ProbabilityDistributionFuction getDistribution() {
        return distribution;
    }

    public void setDistribution(ProbabilityDistributionFuction distribution) {
        this.distribution = distribution;
    }
}
