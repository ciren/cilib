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
package net.sourceforge.cilib.nn.components;

import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.type.types.Randomizable;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Class implements the {@link WeightInitializationStrategy} interface and initializes
 * the weights randomly within a domain.
 * @author andrich
 */
public class RandomWeightInitializationStrategy implements WeightInitializationStrategy {

    private Random randomNumberGenerator;

    /**
     * Default constructor. The default RNG is a Mersenne Twister.
     */
    public RandomWeightInitializationStrategy() {
        randomNumberGenerator = new MersenneTwister();
    }

    /**
     * Initialize the set of weights to random values within their domain bounds.
     * @param weights {@inheritDoc }
     */
    @Override
    public void initialize(Vector weights) {
        for (Randomizable weight : weights) {
            weight.randomize(randomNumberGenerator);
        }
    }

    /**
     * Gets the random number generator to use.
     * @return the random number generator that the class uses.
     */
    public Random getRandomNumberGenerator() {
        return randomNumberGenerator;
    }

    /**
     * Sets the random number generator.
     * @param randomNumberGenerator the class's new random number generator to use.
     */
    public void setRandomNumberGenerator(Random randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }
}
