/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import fj.F;
import java.util.Iterator;

import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.pso.dynamic.DynamicParticle;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;


public class PartialReinitialisationResponseStrategy extends ParticleReevaluationResponseStrategy {

    private static final long serialVersionUID = 4619744183683905269L;
    private double reinitialisationRatio;

    public PartialReinitialisationResponseStrategy() {
        super();
        reinitialisationRatio = 0.5;
    }

    public PartialReinitialisationResponseStrategy(PartialReinitialisationResponseStrategy copy) {
        this.reinitialisationRatio = copy.reinitialisationRatio;
    }

    @Override
    public PartialReinitialisationResponseStrategy getClone() {
        return new PartialReinitialisationResponseStrategy(this);
    }

    /**
     * Respond to environment change by re-evaluating each particle's position, personal best and neighbourhood best,
     * and reinitialising the positions of a specified percentage of particles.
     * @param algorithm PSO algorithm that has to respond to environment change
     */
    @Override
	protected <P extends Particle, A extends SinglePopulationBasedAlgorithm<P>> void performReaction(
			A algorithm) {

        fj.data.List<? extends Entity> topology = algorithm.getTopology();

        // Reevaluate current position. Update personal best (done by reevaluate()).
        Iterator<? extends Entity> iterator = topology.iterator();
        int reinitCounter = 0;
        int keepCounter = 0;
        int populationSize = algorithm.getTopology().length();
        while (iterator.hasNext()) {
            DynamicParticle current = (DynamicParticle) iterator.next();
            ZeroTransformation zt = new ZeroTransformation();

            //makes sure the charged particles are randomly positioned across the topology
            if (reinitCounter < Math.floor(populationSize * reinitialisationRatio) && Rand.nextDouble() < reinitialisationRatio && current != Topologies.getBestEntity(algorithm.getTopology())) {
                current.getPosition().randomise();
                current.put(Property.VELOCITY, Vectors.transform(current.getVelocity(), zt));
                current.put(Property.BEST_POSITION, Vector.copyOf((Vector) current.getPosition()));
                ++reinitCounter;
            }//if
            else if (keepCounter > Math.floor(populationSize * (1.0 - reinitialisationRatio)) && current != Topologies.getBestEntity(algorithm.getTopology())) {
                current.getPosition().randomise();
                current.put(Property.VELOCITY, Vectors.transform(current.getVelocity(), zt));
                current.put(Property.BEST_POSITION, Vector.copyOf((Vector) current.getPosition()));
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

    private static class ZeroTransformation extends F<Numeric, Number> {
        @Override
        public Number f(Numeric from) {
            return 0.0;
        }
    }
}
