/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import com.google.common.base.Function;
import java.util.Iterator;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.pso.dynamic.DynamicParticle;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

public class PartialReinitialisationResponseStrategy<E extends PopulationBasedAlgorithm> extends ParticleReevaluationResponseStrategy<E> {

    private static final long serialVersionUID = 4619744183683905269L;
    private double reinitialisationRatio;

    public PartialReinitialisationResponseStrategy() {
        super();
        reinitialisationRatio = 0.5;
    }

    public PartialReinitialisationResponseStrategy(PartialReinitialisationResponseStrategy<E> copy) {
        this.reinitialisationRatio = copy.reinitialisationRatio;
    }

    @Override
    public PartialReinitialisationResponseStrategy<E> getClone() {
        return new PartialReinitialisationResponseStrategy<E>(this);
    }

    /**
     * Respond to environment change by re-evaluating each particle's position, personal best and neighbourhood best,
     * and reinitialising the positions of a specified percentage of particles.
     * @param algorithm PSO algorithm that has to respond to environment change
     */
    @Override
    public void respond(E algorithm) {

        Topology<? extends Entity> topology = algorithm.getTopology();

        // Reevaluate current position. Update personal best (done by reevaluate()).
        Iterator<? extends Entity> iterator = topology.iterator();
        int reinitCounter = 0;
        int keepCounter = 0;
        int populationSize = algorithm.getTopology().size();
        while (iterator.hasNext()) {
            DynamicParticle current = (DynamicParticle) iterator.next();
            ZeroTransformation zt = new ZeroTransformation();

            //makes sure the charged particles are randomly positioned across the topology
            if (reinitCounter < Math.floor(populationSize * reinitialisationRatio) && Rand.nextDouble() < reinitialisationRatio && current != Topologies.getBestEntity(algorithm.getTopology())) {
                current.getPosition().randomise();
                current.getProperties().put(EntityType.Particle.VELOCITY, Vectors.transform(current.getVelocity(), zt));
                current.getProperties().put(EntityType.Particle.BEST_POSITION, Vector.copyOf(current.getPosition()));
                ++reinitCounter;
            }//if
            else if (keepCounter > Math.floor(populationSize * (1.0 - reinitialisationRatio)) && current != Topologies.getBestEntity(algorithm.getTopology())) {
                current.getPosition().randomise();
                current.getProperties().put(EntityType.Particle.VELOCITY, Vectors.transform(current.getVelocity(), zt));
                current.getProperties().put(EntityType.Particle.BEST_POSITION, Vector.copyOf(current.getPosition()));
                ++reinitCounter;
            }//else if
            else {
                ++keepCounter;
            }//else
        }

        // Re-evaluate:
        reevaluateParticles(algorithm); // super class method
    }

    /**
     * @return the reinitialisationRatio
     */
    public double getReinitialisationRatio() {
        return reinitialisationRatio;
    }

    /**
     * @param reinitialisationRatio the reinitialisationRatio to set
     */
    public void setReinitialisationRatio(double reinitialisationRatio) {
        this.reinitialisationRatio = reinitialisationRatio;
    }

    private static class ZeroTransformation implements Function<Numeric, Number> {

        @Override
        public Number apply(Numeric from) {
            return 0.0;
        }
    }
}
