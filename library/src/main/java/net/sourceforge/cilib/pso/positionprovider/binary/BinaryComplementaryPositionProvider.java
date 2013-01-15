/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.positionprovider.binary;

import net.sourceforge.cilib.functions.activation.Sigmoid;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.positionprovider.PositionProvider;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implementation of A novel Binary Particle Swarm Optimisation.
 * Particle position is updated using complementary bits
 * <p>
 * References:
 * </p>
 * <ul><li>
 * Mojtabe Ahmadieh Khanesar, Mohammed Teshnehlab and Mahdi Aliyari Shoorehdeli.,
 * "A Novel Binary Particle Swarm Optimization" (2007). Proceedings of the 15th
 * Mediterranear Conference on Control and Automation.
 * </li></ul>
 */
public class BinaryComplementaryPositionProvider implements PositionProvider {

    private Sigmoid sigmoid;

    /**
     * Create an instance of {@linkplain BinaryComplementaryPositionProvider}.
     */
    public BinaryComplementaryPositionProvider() {
        this.sigmoid = new Sigmoid();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public BinaryComplementaryPositionProvider(BinaryComplementaryPositionProvider copy) {
        this.sigmoid = copy.sigmoid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BinaryComplementaryPositionProvider getClone() {
        return new BinaryComplementaryPositionProvider(this);
    }

    /**
     * A 2's complement BinaryPSO particle position update.
     */
    @Override
    public Vector get(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); i++) {
            double result = this.sigmoid.apply(velocity.doubleValueOf(i));
            double rand = Math.random();

            // 2's complement update strategy
            if (rand < result) {
                builder.addWithin(1 - position.doubleValueOf(i), position.boundsOf(i));
            } else {
                builder.addWithin(position.doubleValueOf(i), position.boundsOf(i));
            }
        }
        return builder.build();
    }

    /**
     * Get the sigmoid function used within the update strategy.
     * @return The {@linkplain Sigmoid} function used.
     */
    public Sigmoid getSigmoid() {
        return this.sigmoid;
    }

    /**
     * Set the sigmoid function to use.
     * @param sigmoid The function to set.
     */
    public void setSigmoid(Sigmoid sigmoid) {
        this.sigmoid = sigmoid;
    }
}
