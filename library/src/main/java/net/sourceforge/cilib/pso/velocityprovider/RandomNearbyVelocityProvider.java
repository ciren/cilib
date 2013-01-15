/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider;

import fj.P1;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

public final class RandomNearbyVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = 8204479765311251730L;

    private ProbabilityDistributionFunction random;

    /** Creates a new instance of StandardVelocityUpdate. */
    public RandomNearbyVelocityProvider() {
        this.random = new GaussianDistribution();
    }

    /**
     * Copy constructor.
     * @param copy The object to copy.
     */
    public RandomNearbyVelocityProvider(RandomNearbyVelocityProvider copy) {
        this.random = copy.random;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RandomNearbyVelocityProvider getClone() {
        return new RandomNearbyVelocityProvider(this);
    }

    /**
     * Perform the velocity update for the given <tt>Particle</tt>.
     * @param particle The Particle velocity that should be updated.
     */
    @Override
    public Vector get(Particle particle) {
        Topology<Particle> topology = ((PSO) AbstractAlgorithm.get()).getTopology();
        Vector average = Vector.fill(0.0, particle.getDimension());

        for(Particle p : topology) {
            average = average.plus((Vector) p.getVelocity());
        }

        average = average.divide(topology.size());

        average.multiply(new P1<Number>() {
            @Override
            public Number _1() {
                return random.getRandomNumber();
            }
        });

        return average;
    }

    public void setRandom(ProbabilityDistributionFunction random) {
        this.random = random;
    }

    public ProbabilityDistributionFunction getRandom() {
        return random;
    }
}
