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
public class GammaDistribution implements ProbabilityDistributionFuction {

    private RandomProvider provider;

    public GammaDistribution() {
        provider = new MersenneTwister();
    }

    public GammaDistribution(long seed) {
        provider = new MersenneTwister(seed);
    }

    /**
     * Get a Gamma-distributed random number with shape <code>k</code> and scale <code>theta</code>.
     * @return a Gamma-distributed random number with shape 2 and scale 2.0.
     */
    @Override
    public double getRandomNumber() {
        return getRandomNumber(2, 2.0);
    }

    /**
     * Get a Gamma-distributed random number. Two parameters are required.
     * The first specifies the shape, the second specifies the scale.
     *
     * This method takes advantage of the following relationship between
     * the Gamma and Exponential distributions:
     *
     * if X1...Xn ~ Exponential(lambda) are exponentially distributed
     * and Y = X1 + X2 + ... + Xn, then Y ~ Gamma(n, 1/lambda).
     *
     * @param shape The shape of the Gamma distribution.
     * @param scale The scale of the Gamma distribution.
     * @pre shape is assumed to be an integer.
     * @return a Gamma-distributed random number.
     */
    @Override
    public double getRandomNumber(double... shapeScale) {
        checkArgument(shapeScale.length == 2, "The Gamma distribution requires two parameters. ");
        checkArgument(shapeScale[0] > 0, "The first provided parameter (shape parameter) must be an integer greater than zero.");
        checkArgument(shapeScale[1] > 0, "The second provided parameter (scale parameter) must be greater than zero.");

        ProbabilityDistributionFuction expPdf = new ExponentialDistribution();
        double sum = 0;

        for (int i = 0; i < shapeScale[0]; i++) {
            sum += expPdf.getRandomNumber(1 / shapeScale[1]);
        }

        return sum;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RandomProvider getRandomProvider() {
        return provider;
    }
}
