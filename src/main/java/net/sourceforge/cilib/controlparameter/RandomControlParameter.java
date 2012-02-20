/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.controlparameter;

import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.math.random.UniformDistribution;

/**
 * A control parameter that provides random parameter values, distributed
 * according to a specified probability distribution function. By default,
 * the distribution is uniform.
 */
public class RandomControlParameter implements ControlParameter {
    private ProbabilityDistributionFuction distribution;
    private double lowerBound;
    private double upperBound;

    public RandomControlParameter() {
        this(new UniformDistribution());
    }

    public RandomControlParameter(ProbabilityDistributionFuction distribution) {
        this.distribution = distribution;
        this.lowerBound = 0.0;
        this.upperBound = 1.0;
    }

    @Override
    public RandomControlParameter getClone() {
        return new RandomControlParameter();
    }

    @Override
    public double getParameter() {
        return getParameter(lowerBound, upperBound);
    }
    
    @Override
    public double getParameter(double min, double max) {
        return distribution.getRandomNumber(min, max);
    }

    /**
     * This method overrides getParamter() to allow the use of distributions
     * that require more parameters than min and max.
     * @param parameters The parameters required by the ProbabilityDistributionFunction.
     * @return A random number.
     */
    public double getParameter(double... parameters) {
        return distribution.getRandomNumber(parameters);
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public void setLowerBound(double lower) {
        this.lowerBound = lower;
    }

    public void setUpperBound(double upper) {
        this.upperBound = upper;
    }

    public ProbabilityDistributionFuction getDistribution() {
        return distribution;
    }

    public void setDistribution(ProbabilityDistributionFuction distribution) {
        this.distribution = distribution;
    }
    
    public void updateParameter(double value) {
        
    }
    
    /*
     * Get the current velocity of the parameter
     * @return The current velocity of the parameter
     */
    @Override
    public double getVelocity(){
        return 0.0;
    }
    
    /*
     * Set the current velocity of the parameter
     */
    @Override
    public void setVelocity(double value){
        
    }
    
    /*
     * Not currently applicable
     */
    public boolean wasSetByUser() {
        return false;
    }
}
