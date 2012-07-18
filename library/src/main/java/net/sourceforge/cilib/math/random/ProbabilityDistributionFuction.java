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

import net.sourceforge.cilib.math.random.generator.RandomProvider;

/**
 * An interface for probability distribution functions. Concrete classes of
 * this type provide methods to sample random values from specific probability
 * distributions.
 *
 */
public interface ProbabilityDistributionFuction {

    /**
     * Sample a random number from the distribution.
     * @return A random number, sampled from the distribution.
     */
    double getRandomNumber();

    /**
     * Sample a random number from the distribution, given a number of control
     * parameters.
     * The control parameters can be any parameters needed by the specific
     * concrete distribution.
     * eg. mean and variance, or upper and lower bounds etc.
     *
     * @param parameters The control parameter for the distribution function.
     * @return
     */
    double getRandomNumber(double... parameters);

    RandomProvider getRandomProvider();

    void setRandomProvider(RandomProvider provider);
}
