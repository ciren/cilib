/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider.binary;

import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.velocityprovider.VelocityProvider;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implementation of Modified particle swarm optimisation algorithm for variable
 * selection in MLR and PLS modelling.
 *
 * Velocity of each particle is random
 * <p>
 * References:
 * </p>
 * <ul><li>
 * Qi Shen, Jian-Hui Jiang, Chen-Xu Jiao, Guo-li Shen, Ru-Qin Yu.,
 * "Modified particle swarm optimization algorithm for variable
 * selection in MLR and PLS modelling: QSAR studies of antagonism of
 * angiotensin II antagonists" (2004). European Journal of Pharmaceutical
 * Sciences 22, 145-152
 * </li></ul>
 */
public final class BinaryRandomVelocityProvider implements VelocityProvider {

    protected Bounds bounds;

    public BinaryRandomVelocityProvider() {
        this(new Bounds(0,1));
    }

    public BinaryRandomVelocityProvider(Bounds bounds) {
        this.bounds = bounds;
    }

    public BinaryRandomVelocityProvider(BinaryRandomVelocityProvider copy) {
        this.bounds = copy.bounds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BinaryRandomVelocityProvider getClone() {
        return new BinaryRandomVelocityProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector.Builder velocity = Vector.newBuilder();

        for(int i = 0; i < particle.getDimension(); i++) {
            velocity.addWithin(Rand.nextDouble(), bounds);
        }

        return velocity.build();
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }
}
