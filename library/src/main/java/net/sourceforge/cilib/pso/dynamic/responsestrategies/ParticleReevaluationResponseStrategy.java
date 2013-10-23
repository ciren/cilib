/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.pso.dynamic.DynamicParticle;
import net.sourceforge.cilib.pso.particle.Particle;

public class ParticleReevaluationResponseStrategy extends EnvironmentChangeResponseStrategy {

    private static final long serialVersionUID = -4389695103800841288L;

    public ParticleReevaluationResponseStrategy() {
    }

    public ParticleReevaluationResponseStrategy(ParticleReevaluationResponseStrategy copy) {
        super(copy);
    }

    public ParticleReevaluationResponseStrategy getClone() {
        return new ParticleReevaluationResponseStrategy(this);
    }

    /**
     * Respond to environment change by re-evaluating each particle's position, personal best and neighbourhood best.
     * @param algorithm PSO algorithm that has to respond to environment change
     */
    @Override
    public <P extends Particle, A extends SinglePopulationBasedAlgorithm<P>> void respond(A algorithm) {
        reevaluateParticles(algorithm);
    }

    /**
     * Re-evaluate each particle's position, personal best and neighbourhood best.
     * @param algorithm PSO algorithm that has to respond to environment change
     */
    protected <P extends Particle, A extends SinglePopulationBasedAlgorithm<P>> void reevaluateParticles(A algorithm) {
        fj.data.List<P> topology = algorithm.getTopology();

        // Reevaluate current position. Update personal best (done by reevaluate()).
        for (P current : topology) {
            ((DynamicParticle) current).reevaluate();
        }

        updateNeighbourhoodBestEntities(topology, algorithm.getNeighbourhood());
    }

    @Override
    protected <P extends Particle, A extends SinglePopulationBasedAlgorithm<P>> void performReaction(
                    A algorithm) {
            reevaluateParticles(algorithm);
    }
}
