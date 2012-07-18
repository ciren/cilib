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
package net.sourceforge.cilib.math.random;

import static com.google.common.base.Preconditions.checkArgument;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

/**
 *
 */
public class ExponentialDistribution implements ProbabilityDistributionFuction {

    private RandomProvider provider;
    private ControlParameter rate;

    public ExponentialDistribution() {
        provider = new MersenneTwister();
        rate = ConstantControlParameter.of(1.0);
    }

    public ExponentialDistribution(long seed) {
        provider = new MersenneTwister(seed);
        rate = ConstantControlParameter.of(1.0);
    }

    /**
     * Get an Exponentially-distributed random number with default rate 1.0.
     * @return a Laplace-distributed random number with rate 1.0.
     */
    @Override
    public double getRandomNumber() {
        return getRandomNumber(rate.getParameter());
    }

    /**
     * Get an Exponentially-distributed random number. The rate of the distribution
     * is given by <code>rate</code>.
     *
     * @param rate The rate of the exponential distribution.
     * @return a Laplace-distributed random number.
     */
    @Override
    public double getRandomNumber(double... rate) {
        checkArgument(rate.length == 1, "The Exponential distribution requires a single parameter that specifies the rate.");
        checkArgument(rate[0] > 0, "The rate for the Exponential distribution parameter must be greater than zero.");

        double r = provider.nextDouble(); //uniform number in the range (0.0, 1.0]:
        return -Math.log(1 - r) / rate[0];
    }

    @Override
    public RandomProvider getRandomProvider() {
        return provider;
    }

    @Override
    public void setRandomProvider(RandomProvider provider) {
        this.provider = provider;
    }

    public ControlParameter getRate() {
        return rate;
    }

    public void setRate(ControlParameter rate) {
        this.rate = rate;
    }


}
