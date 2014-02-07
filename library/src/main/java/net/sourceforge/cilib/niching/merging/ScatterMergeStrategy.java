/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.merging;

import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * Takes all the entities of the second sub-swarm, reinitialises those entities
 * in the first sub-swarm and returns the merged sub-swarm.
 */
public class ScatterMergeStrategy extends MergeStrategy {
    @Override
    public SinglePopulationBasedAlgorithm f(SinglePopulationBasedAlgorithm subSwarm1, SinglePopulationBasedAlgorithm subSwarm2) {
        SinglePopulationBasedAlgorithm<Particle> newSwarm = new StandardMergeStrategy().f(subSwarm1, subSwarm2);

        for (int i = subSwarm1.getTopology().length(); i < newSwarm.getTopology().length(); i++) {
            newSwarm.getTopology().index(i).reinitialise();
            newSwarm.getTopology().index(i).updateFitness(newSwarm.getTopology().index(i).getBehaviour().getFitnessCalculator().getFitness(newSwarm.getTopology().index(i)));
        }

        return newSwarm;
    }
}
