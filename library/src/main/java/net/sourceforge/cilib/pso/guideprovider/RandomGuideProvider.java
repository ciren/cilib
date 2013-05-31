/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.guideprovider;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * A concrete implementation of {@link GuideProvider} where the neighbourhood
 * best position of a random particle gets selected as a guide.
 */
public class RandomGuideProvider implements GuideProvider {

    private ProbabilityDistributionFunction random;

    public RandomGuideProvider() {
        this.random = new UniformDistribution();
    }

    public RandomGuideProvider(RandomGuideProvider copy) {
        this.random = copy.random;
    }

    @Override
    public RandomGuideProvider getClone() {
        return new RandomGuideProvider(this);
    }

    @Override
    public StructuredType get(Particle particle) {
        fj.data.List<Particle> topology = ((PSO) AbstractAlgorithm.get()).getTopology();
        Particle other = topology.index((int) random.getRandomNumber(0, topology.length()));

        return other.getBestPosition();
    }
}
