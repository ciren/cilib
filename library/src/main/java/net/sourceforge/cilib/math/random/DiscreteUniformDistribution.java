/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math.random;

import static com.google.common.base.Preconditions.checkArgument;
import net.sourceforge.cilib.type.types.container.Vector;

public class DiscreteUniformDistribution implements ProbabilityDistributionFunction {

    private Vector elements;

    /**
     * Default Constructor.
     */
    public DiscreteUniformDistribution() {
        this.elements = Vector.of(0.0, 1.0);
    }

    /**
     * Contructor to initialize the elements.
     * @param elements The vector containing possible outcomes.
     */
    public DiscreteUniformDistribution(Vector elements) {
        this.elements = elements;
    }

    /**
     * Return a random element from the vector of possible outcomes.
     *
     * @return Uniformly selected element.
     */
    @Override
    public double getRandomNumber() {
        return elements.sample().doubleValue();
    }

    /**
     * Since the Discrete Uniform Distribution does not require parameters,
     * this function simply returns a random element.
     */
    @Override
    public double getRandomNumber(double... parameters) {
        Vector.Builder builder = Vector.newBuilder();
        for (double d : parameters) {
            builder.add(d);
        }
        return builder.build().sample().doubleValue();
    }

    /**
     * Sets the vector of elements to use for the distribution function.
     * @param elements The vector of elements to sample from.
     */
    public void setElements(Vector elements) {
        this.elements = elements;
    }

    /**
     * Returns the vector of elements that is used for the distribution function.
     * @return The elements are sampled from.
     */
    public Vector getElements() {
        return elements;
    }
}
