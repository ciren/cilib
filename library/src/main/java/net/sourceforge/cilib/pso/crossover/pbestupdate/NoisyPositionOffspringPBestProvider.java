/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.pbestupdate;

import fj.P1;
import java.util.List;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This OffspringPBestProvider sets an offspring's pBest using another
 * OffspringPBestProvider and adding some noise to the resulting pbest.
 */
public class NoisyPositionOffspringPBestProvider extends OffspringPBestProvider {
    private ProbabilityDistributionFunction random;
    private OffspringPBestProvider delegate;

    public NoisyPositionOffspringPBestProvider() {
        this.random = new GaussianDistribution();
        this.delegate = new CurrentPositionOffspringPBestProvider();
    }

    @Override
    public StructuredType f(List<Particle> parents, Particle offspring) {
        return ((Vector) delegate.f(parents, offspring)).multiply(new P1<Number>(){
            @Override
            public Number _1() {
                return random.getRandomNumber();
            }
        });
    }

    public ProbabilityDistributionFunction getRandom() {
        return random;
    }

    public void setRandom(ProbabilityDistributionFunction random) {
        this.random = random;
    }

    public void setDelegate(OffspringPBestProvider delegate) {
        this.delegate = delegate;
    }

    public OffspringPBestProvider getDelegate() {
        return delegate;
    }
}
