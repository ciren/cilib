/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic;

import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.VelocityProvider;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * VelocityProvider for QSO (Quantum PSO). Implemented according
 * to paper by Blackwell and Branke, "Multiswarms, Exclusion, and
 * Anti-Convergence in Dynamic Environments."
 *
 *
 */
public class QuantumVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -940568473388702506L;
    private static final double EPSILON = 0.000000001;

    private VelocityProvider delegate;

    /**
     * Create a new instance of {@linkplain QuantumVelocityProvider}.
     */
    public QuantumVelocityProvider() {
        this.delegate = new StandardVelocityProvider();
    }

    /**
     * Create an copy of the provided instance.
     * @param copy The instance to copy.
     */
    public QuantumVelocityProvider(QuantumVelocityProvider copy) {
        this.delegate = copy.delegate.getClone();
    }

    @Override
    public QuantumVelocityProvider getClone() {
        return new QuantumVelocityProvider(this);
    }

    /**
     * Update particle velocity; do it in a standard way if the particle is neutral, and
     * do not update it if the particle is quantum (charged), since quantum particles do
     * not use the velocity to update their positions.
     * @param particle the particle to update position of
     */
    @Override
    public Vector get(Particle particle) {
        ChargedParticle checkChargeParticle = (ChargedParticle) particle;
        if (checkChargeParticle.getCharge() < EPSILON) {    // the particle is neutral
            return this.delegate.get(particle);
        }
        return (Vector) particle.getVelocity().getClone();
    }

        public void setDelegate(VelocityProvider delegate) {
        this.delegate = delegate;
    }

    public VelocityProvider getDelegate() {
        return this.delegate;
    }
}
