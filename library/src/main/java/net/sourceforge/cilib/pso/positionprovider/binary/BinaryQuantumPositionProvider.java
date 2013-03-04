/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.positionprovider.binary;

import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.positionprovider.PositionProvider;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implementation of A quantum particle swarm optimisation
 *
 * Velocity of particle is modelled by a quantum bit (qubit)
 * <p>
 * References:
 * </p>
 * <ul><li>
 * Shuyuan Yang, Min Wang, Licheng Jiao.,
 * "A quantum particle swarm optimization" (2004).
 * IEEE Congress on evolutionary computation, vol 1, pp 320--324, 2004
 * </li></ul>
 */
public class BinaryQuantumPositionProvider implements PositionProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public BinaryQuantumPositionProvider getClone() {
        return this;
    }

    /*
     * Updates the particle's position by treating velocity as a
     * set of quantum bits
     */
    @Override
    public Vector get(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector.Builder builder = Vector.newBuilder();

        for (int i = 0; i < particle.getDimension(); i++) {
            double rand = Rand.nextDouble();
            double q = velocity.doubleValueOf(i);

            if (rand > q) {
                builder.addWithin(1.0, position.boundsOf(i));
            } else {
                builder.addWithin(0.0, position.boundsOf(i));
            }
        }
        return builder.build();
    }
}
