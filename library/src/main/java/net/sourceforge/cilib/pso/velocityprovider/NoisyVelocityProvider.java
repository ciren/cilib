/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider;

import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

/**
 * Decorates a {@link VelocityProvider} with random noise from any
 * {@link ProbabilityDistributionFunction}.
 */
public class NoisyVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -4398497101382747367L;
    private ProbabilityDistributionFunction distribution;
    private VelocityProvider delegate;

    public NoisyVelocityProvider() {
        this.distribution = new GaussianDistribution();
        this.delegate = new StandardVelocityProvider();
    }

    public NoisyVelocityProvider(NoisyVelocityProvider rhs) {
        this.distribution = rhs.distribution;
        this.delegate = rhs.delegate.getClone();
    }

    @Override
    public Vector get(Particle particle) {
        Vector velocity = this.delegate.get(particle);
        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < velocity.size(); i++) {
            builder.add(this.distribution.getRandomNumber());
        }
        return Vectors.sumOf(velocity, builder.build());
    }

    @Override
    public NoisyVelocityProvider getClone() {
        return new NoisyVelocityProvider(this);
    }

    public VelocityProvider getDelegate() {
        return this.delegate;
    }

    public void setDelegate(VelocityProvider delegate) {
        this.delegate = delegate;
    }

    public ProbabilityDistributionFunction getDistribution() {
        return this.distribution;
    }

    public void setDistribution(ProbabilityDistributionFunction distribution) {
        this.distribution = distribution;
    }
}
