/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.measurement.multiple;

import java.util.List;

import net.cilib.algorithm.Algorithm;
import net.cilib.algorithm.initialisation.HeterogeneousPopulationInitialisationStrategy;
import net.cilib.measurement.Measurement;
import net.cilib.pso.PSO;
import net.cilib.pso.hpso.HeterogeneousIterationStrategy;
import net.cilib.pso.particle.Particle;
import net.cilib.pso.particle.ParticleBehavior;
import net.cilib.type.types.Int;
import net.cilib.type.types.container.Vector;

/**
 * Measures how many particles in a an adaptive heterogeneous PSO are currently
 * following each of the different behaviors in the behavior pool.
 */
public class AdaptiveHPSOBehaviorProfileMeasurement implements Measurement<Vector> {
    private static final long serialVersionUID = -255308745515061075L;

    /**
     * {@inheritDoc}
     */
    @Override
    public AdaptiveHPSOBehaviorProfileMeasurement getClone() {
        return this;
    }

    /**
     * Measure the behavior profile.
     *
     * @param algorithm the {@linkplain Algorithm}
     * @return  a {@link Vector} containing integer values.
     *          Each value corresponds to a behavior in the behavior pool and
     *          indicates how many particles are currently following that behavior.
     */
    @Override
    public Vector getValue(Algorithm algorithm) {
        PSO pso = (PSO) algorithm;
        fj.data.List<Particle> topology = pso.getTopology();
        HeterogeneousIterationStrategy strategy = (HeterogeneousIterationStrategy) pso.getIterationStrategy();
        HeterogeneousPopulationInitialisationStrategy initStrategy = (HeterogeneousPopulationInitialisationStrategy) pso.getInitialisationStrategy();
        List<ParticleBehavior> initialBehaviorPool = initStrategy.getBehaviorPool();
        List<ParticleBehavior> behaviorPool = strategy.getBehaviorPool();

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < behaviorPool.size(); i++) {
            builder.add(Int.valueOf(0));
        }

        Vector profile = builder.build();
        for (Particle p : topology) {
            for (int i = 0; i < profile.size(); i++) {
                if (p.getParticleBehavior() == behaviorPool.get(i) || p.getParticleBehavior() == initialBehaviorPool.get(i)) {
                    profile.setInt(i, profile.get(i).intValue() + 1);
                }
            }
        }

        return profile;
    }
}
