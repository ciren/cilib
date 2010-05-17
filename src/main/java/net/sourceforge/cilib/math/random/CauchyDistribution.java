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
     * @param location The location of the mean of the distribution.
     * @param scale The allowed variation that can be observed.
     * @return A Cauchy random number with location <tt>location</tt> and
     *         scale parameter <tt>scale</tt>
     */
    @Override
    public double getRandomNumber(double location, double scale) {
        double x = provider.nextDouble(); // Uniform number between 0.0 and 1.0

        return location + scale * Math.tan(Math.PI * (x - 0.5));
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
