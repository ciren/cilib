/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.nn.components;

import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.types.Randomizable;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Class implements the {@link WeightInitializationStrategy} interface and initializes
 * the weights randomly within a domain.
 */
public class RandomWeightInitializationStrategy implements WeightInitializationStrategy {

    private RandomProvider randomNumberGenerator;

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
    public RandomProvider getRandomNumberGenerator() {
        return randomNumberGenerator;
    }

    /**
     * Sets the random number generator.
     * @param randomNumberGenerator the class's new random number generator to use.
     */
    public void setRandomNumberGenerator(RandomProvider randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }
}
