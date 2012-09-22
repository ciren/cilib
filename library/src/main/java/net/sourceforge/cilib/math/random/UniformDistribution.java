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
public class UniformDistribution implements ProbabilityDistributionFunction {

    private RandomProvider provider;
    private ControlParameter lowerBound;
    private ControlParameter upperBound;

    /**
     * Default Constructor
     */
    public UniformDistribution() {
        this.provider = new MersenneTwister();
        lowerBound = ConstantControlParameter.of(0.0);
        upperBound = ConstantControlParameter.of(1.0);
    }

    public UniformDistribution(long seed) {
        this.provider = new MersenneTwister(seed);
        lowerBound = ConstantControlParameter.of(0.0);
        upperBound = ConstantControlParameter.of(1.0);
    }

    /**
     * Get a uniform random number located within {@code 0 &lt;= x &lt; 1}.
     *
     * @return Uniform random number ({@code 0 &lt;= x &lt; 1}).
     */
    @Override
    public double getRandomNumber() {
        return getRandomNumber(lowerBound.getParameter(), upperBound.getParameter());
    }

    /**
     * Get the uniform random number. The number is located within {@code A &lt;= x &lt; B}
     * where {@code A == mean} and {@code B == deviation}.
     *
     * Two parameters are required. The first specifies the lower bound,
     * the second specifies the upper bound.
     *
     * @param lower The lower bound for the number generation.
     * @param upper The upper bound for the number generation.
     * @return Uniform random number ({@code lower &lt;= x &lt; upper}).
     */
    @Override
    public double getRandomNumber(double... bounds) {
        checkArgument(bounds.length == 2, "The Uniform distribution requires two parameters.");
        checkArgument(bounds[1] > bounds[0], "The lower bound (first parameter) must be less than upper bound (second parameter).");

        double r = provider.nextDouble();
        return ((bounds[1] - bounds[0]) * r + bounds[0]);
    }

    @Override
    public RandomProvider getRandomProvider() {
        return provider;
    }

    @Override
    public void setRandomProvider(RandomProvider provider) {
        this.provider = provider;
    }
}
