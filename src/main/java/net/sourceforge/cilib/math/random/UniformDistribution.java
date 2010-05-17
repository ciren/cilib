/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
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
     * @param lower The lower bound for the number generation.
     * @param upper The upper bound for the number generation.
     * @return Uniform random number (<code>lower &lt;= x &lt; upper</code>).
     */
    @Override
    public double getRandomNumber(double lower, double upper) {
        double r = provider.nextDouble();
        return ((upper - lower) * r + lower);
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
