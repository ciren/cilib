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
public class CauchyDistribution implements ProbabilityDistributionFuction {

    private RandomProvider provider;
    private ControlParameter location;
    private ControlParameter scale;

    /**
     * Default Constructor
     */
    public CauchyDistribution() {
        this.provider = new MersenneTwister();
        this.location = ConstantControlParameter.of(0.0);
        this.scale = ConstantControlParameter.of(1.0);
    }

    public CauchyDistribution(long seed) {
        this.provider = new MersenneTwister(seed);
        this.location = ConstantControlParameter.of(0.0);
        this.scale = ConstantControlParameter.of(1.0);
    }

    /**
     * Convenience method to obtain a Cauchy number. The distribution has a <code>mean</code>
     * of <code>0.0</code> and a <code>deviation</code> of <code>1.0</code>.
     * @return A cauchy number in the given distribution.
     */
    @Override
    public double getRandomNumber() {
        return getRandomNumber(location.getParameter(), scale.getParameter());
    }

    /**
     * Return a random number sampled from the Cauchy distribution.
     * Two parameters are required. The first specifies the location,
     * the second specifies the scale.
     * @param location The location of the mean of the distribution.
     * @param scale The allowed variation that can be observed.
     * @return A Cauchy random number with location <tt>location</tt> and
     *         scale parameter <tt>scale</tt>
     */
    @Override
    public double getRandomNumber(double... locationScale) {
        checkArgument(locationScale.length == 2, "The Cauchy distribution requires two parameters.");
        checkArgument(locationScale[1] > 0, "The scale must be greater than zero.");

        double x = provider.nextDouble(); // Uniform number between 0.0 and 1.0

        return locationScale[0] + locationScale[1] * Math.tan(Math.PI * (x - 0.5));
    }

    @Override
    public RandomProvider getRandomProvider() {
        return provider;
    }

    @Override
    public void setRandomProvider(RandomProvider provider) {
        this.provider = provider;
    }

    public void setScale(ControlParameter scale) {
        this.scale = scale;
    }

    public ControlParameter getScale() {
        return scale;
    }

    public void setLocation(ControlParameter location) {
        this.location = location;
    }

    public ControlParameter getLocation() {
        return location;
    }
}
