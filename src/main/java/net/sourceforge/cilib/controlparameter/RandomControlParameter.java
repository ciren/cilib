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
 * 
 * @author bennie
 */
public class RandomControlParameter implements BoundedControlParameter {
    protected ProbabilityDistributionFuction distribution;
    protected double lower, upper;

    public RandomControlParameter() {
        this.distribution = new UniformDistribution();
        this.lower = 0.0;
        this.upper = 1.0;
    }

    public RandomControlParameter(ProbabilityDistributionFuction distribution) {
        this.distribution = distribution;
        this.lower = 0.0;
        this.upper = 1.0;
    }

    @Override
    public ControlParameter getClone() {
        return new RandomControlParameter();
    }

    @Override
    public double getParameter() {
        return distribution.getRandomNumber(lower, upper);
    }

    @Override
    public double getParameter(double min, double max) {
        return distribution.getRandomNumber(min, max);
    }

    /**
     * This method overrides getParamter() to allow the use of distributions
     * that reuquire more parameters than min and max.
     * @param parameters The parameters required by the ProbabilityDistributionFunction.
     * @return A random number.
     */
    public double getParameter(double... parameters) {
        return distribution.getRandomNumber(parameters);
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
        throw new UnsupportedOperationException("Not supported.");
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
