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
public class UniformDistribution implements ProbabilityDistributionFuction {

    private RandomProvider provider;

    /**
     * Default Constructor
     */
    public UniformDistribution() {
        this.provider = new MersenneTwister();
    }

    public UniformDistribution(long seed) {
        this.provider = new MersenneTwister(seed);
    }

    /**
     * Get a uniform random number located within <code>0 &lt;= x &lt; 1</code>.
     *
     * @return Uniform random number (<code>0 &lt;= x &lt; 1</code>).
     */
    @Override
    public double getRandomNumber() {
        return getRandomNumber(0.0, 1.0);
    }

    /**
     * Get the uniform random number. The number is located within <code>A &lt;= x &lt; B</code>
     * where <code>A == mean</code> and <code>B == deviation</code>.
     *
     * Two parameters are required. The first specifies the lower bound,
     * the second specifies the upper bound.
     *
     * @param lower The lower bound for the number generation.
     * @param upper The upper bound for the number generation.
     * @return Uniform random number (<code>lower &lt;= x &lt; upper</code>).
     */
    @Override
    public double getRandomNumber(double... bounds) {
        if(bounds.length != 2 || bounds[0] >= bounds[1]) {
            throw new IllegalArgumentException("The Uniform distribution requires two parameters. The first specifies the lower bound, the second specifies the upper bound. The lower bound must be less than upper bound.");
        }

        double r = provider.nextDouble();
        return ((bounds[1] - bounds[0]) * r + bounds[0]);
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
        return this.provider;
    }
}
