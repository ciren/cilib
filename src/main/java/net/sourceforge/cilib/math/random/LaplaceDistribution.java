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
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

/**
 * 
 */
public class LaplaceDistribution implements ProbabilityDistributionFuction {

    private RandomProvider provider;

    public LaplaceDistribution() {
        provider = new MersenneTwister();
    }

    public LaplaceDistribution(long seed) {
        provider = new MersenneTwister(seed);
    }

    /**
     * Get a Laplace-distributed random number with location 0.0 and scale 1.0.
     * @return a Laplace-distributed random number with location 0.0 and scale 1.0.
     */
    @Override
    public double getRandomNumber() {
        return getRandomNumber(0, 1);
    }

    /**
     * Get a Laplace-distributed random number. Two parameters are required.
     * The first specifies the location, the second specifies the scale.
     *
     * @param location The location of the Laplace distribution.
     * @param scale The scale of the Laplace distribution
     * @return a Laplace-distributed random number.
     */
    @Override
    public double getRandomNumber(double... parameters) {
        checkArgument(parameters.length == 2, "The Laplace distribution requires two parameters.");
        checkArgument(parameters[1] > 0, "The scale parameter must be greater than zero.");

        double r = provider.nextDouble() - 0.5; //uniform number in the range (-0.5, 0.5]:

        return parameters[0] - parameters[1] * (Math.log(1 - 2 * Math.abs(r))) * Math.signum(r);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RandomProvider getRandomProvider() {
        return provider;
    }
}
