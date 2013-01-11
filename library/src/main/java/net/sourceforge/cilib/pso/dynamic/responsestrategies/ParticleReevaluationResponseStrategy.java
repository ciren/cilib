/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.dynamic.DynamicParticle;

public class ParticleReevaluationResponseStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeResponseStrategy<E> {

    private static final long serialVersionUID = -4389695103800841288L;

    public ParticleReevaluationResponseStrategy() {
    }

    public ParticleReevaluationResponseStrategy(ParticleReevaluationResponseStrategy<E> copy) {
        super(copy);
    }

    public ParticleReevaluationResponseStrategy<E> getClone() {
        return new ParticleReevaluationResponseStrategy<E>(this);
    }

    /**
     * Respond to environment change by re-evaluating each particle's position, personal best and neighbourhood best.
     * @param algorithm PSO algorithm that has to respond to environment change
     */
    @Override
    public void respond(E algorithm) {
        reevaluateParticles(algorithm);
    }

    /**
     * Re-evaluate each particle's position, personal best and neighbourhood best.
     * @param algorithm PSO algorithm that has to respond to environment change
     */
    protected void reevaluateParticles(E algorithm) {
        Topology<DynamicParticle> topology = (Topology<DynamicParticle>) algorithm.getTopology();

        // Reevaluate current position. Update personal best (done by reevaluate()).
        for (DynamicParticle current : topology) {
            current.reevaluate();
        }

        updateNeighbourhoodBestEntities(topology);
    }

    @Override
    protected void performReaction(E algorithm) {
        reevaluateParticles(algorithm);
    }
}
