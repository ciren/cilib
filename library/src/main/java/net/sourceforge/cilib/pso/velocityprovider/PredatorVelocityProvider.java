/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

/**
 * Implementation of the predator velocity provider.
 * <p>
 * References:
 * <p>
 * <ul><li>
 * A. Silva, A. Nevers, and E. Costa. "An empirical comparison of particle swarm
 * and predator prey optimisation." In Artificial Intelligence and Cognitive
 * Science, pp. 103-110. Springer Berlin Heidelberg, 2002.
 * </li>
 * <li>
 * M. Higashitani, A. Ishigame, and K. Yasuda. "Particle swarm optimization
 * considering the concept of predator-prey behavior." Evolutionary Computation,
 * 2006. CEC 2006. IEEE Congress on. IEEE, 2006.
 * </li></ul>
 */
public final class PredatorVelocityProvider implements VelocityProvider {
    protected ControlParameter acceleration;

    public PredatorVelocityProvider() {
        this(ConstantControlParameter.of(4.1));
    }

    public PredatorVelocityProvider(ControlParameter acceleration) {
        this.acceleration = acceleration;
    }

    /**
     * Copy constructor.
     * @param copy The object to copy.
     */
    public PredatorVelocityProvider(PredatorVelocityProvider copy) {
        this.acceleration = copy.acceleration.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PredatorVelocityProvider getClone() {
        return new PredatorVelocityProvider(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector get(Particle particle) {
        Vector position = (Vector) particle.getPosition();
        Vector globalGuide = (Vector) particle.getGlobalGuide();

        double phi4 = acceleration.getParameter() * Rand.nextDouble();
        return globalGuide.subtract(position).multiply(phi4);
    }

    /**
     * Get the {@linkplain ControlParameter} representing the acceleration or
     * how fast the predator "catches up" with prey particles.
     *
     * @return the acceleration.
     */
    public ControlParameter getAcceleration() {
        return acceleration;
    }

    /**
     * Set the {@linkplain ControlParameter} representing the acceleration or
     * how fast the predator "catches up" with prey particles.
     *
     * @param acceleration The acceleration to set.
     */
    public void setAcceleration(ControlParameter acceleration) {
        this.acceleration = acceleration;
    }
}
