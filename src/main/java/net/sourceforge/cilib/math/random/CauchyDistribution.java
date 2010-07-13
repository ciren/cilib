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

import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

/**
 *
 * @author Gary Pampara
 * @author Bennie Leonard
 */
public class CauchyDistribution implements ProbabilityDistributionFuction {

    private RandomProvider provider;

    /**
     * Default Constructor
     */
    public CauchyDistribution() {
        this.provider = new MersenneTwister();
    }

    public CauchyDistribution(long seed) {
        this.provider = new MersenneTwister(seed);
    }

    /**
     * Convenience method to obtain a Cauchy number. The distribution has a <code>mean</code>
     * of <code>0.0</code> and a <code>deviation</code> of <code>1.0</code>.
     * @return A cauchy number in the given distribution.
     */
    @Override
    public double getRandomNumber() {
        return getRandomNumber(0.0, 1.0);
    }

    /**
     * Return a random number sampled from the Cauchy distribution.
     * Two parameters are required. The first specifies the location,
     * the second sepcifies the scale.
     * @param location The location of the mean of the distribution.
     * @param scale The allowed variation that can be observed.
     * @return A Cauchy random number with location <tt>location</tt> and
     *         scale parameter <tt>scale</tt>
     */
    @Override
    public double getRandomNumber(double... locationScale) {
        if(locationScale.length != 2 || locationScale[1] <= 0) {
            throw new IllegalArgumentException("The Cauchy distribution requires two parameters. The first specifies the location, the second specifies the scale. The scale must be greater than zero.");
        }
        
        double x = provider.nextDouble(); // Uniform number between 0.0 and 1.0

        return locationScale[0] + locationScale[1] * Math.tan(Math.PI * (x - 0.5));
    }

    public RandomProvider getProvider() {
        return provider;
    }

    public void setProvider(RandomProvider provider) {
        this.provider = provider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RandomProvider getRandomProvider() {
        return provider;
    }
}
