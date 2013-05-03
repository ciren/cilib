/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.positionprovider.binary;

import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.LinearlyVaryingControlParameter;
import net.sourceforge.cilib.functions.activation.Sigmoid;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.positionprovider.PositionProvider;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Fast converging Binary PSO that makes use of inertia
 * <p>
 * References:
 * </p>
 * <ul><li>
 * Fang Gao, Gang Cui, Qiang Zhao, and Hongwei Liu,
 * "Application of Improved Discrete Particle Swarm Algorithm
 * in Partner Selection of Virtual Enterprise" (2006).
 * IJCSNS International Journal of Computer Science and Network Security, VOL.6.
 * </li></ul>
 */
public class BinaryInertiaPositionProvider implements PositionProvider {

    private Sigmoid sigmoid;
    private ControlParameter delta;

    /**
     * Create an instance of {@linkplain BinaryInertiaPositionProvider}.
     */
    public BinaryInertiaPositionProvider() {
        this.sigmoid = new Sigmoid();
        this.delta = new LinearlyVaryingControlParameter(0.25, 0);
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public BinaryInertiaPositionProvider(BinaryInertiaPositionProvider copy) {
        this.sigmoid = copy.sigmoid;
        this.delta = copy.delta.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BinaryInertiaPositionProvider getClone() {
        return new BinaryInertiaPositionProvider(this);
    }

    /**
     * BinaryPSO particle position update
     */
    @Override
    public Vector get(Particle particle) {

        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector.Builder builder = Vector.newBuilder();

        for (int i = 0; i < particle.getDimension(); i++) {
            double result = sigmoid.apply(velocity.doubleValueOf(i));
            double min = 0.5 - delta.getParameter();
            double max = 0.5 + delta.getParameter();

            double newPosition = position.doubleValueOf(i);

            if (result < min) {
                newPosition = 0;
            } else if (result > max) {
                newPosition = 1;
            }

            builder.addWithin(newPosition, position.boundsOf(i));
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

    /**
    * Get the {@code delta} control parameter used within the update strategy.
    * @return the {@linkplain ControlParameter} used.
    */
    public ControlParameter getDelta() {
        return this.delta;
    }

    /**
    * Set the {@code delta} control parameter to use.
    * @param delta the {@code delta} {@linkplain ControlParameter} to set.
    */
    public void setDelta(ControlParameter delta) {
        this.delta = delta;
    }
}
