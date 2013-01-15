/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.merging;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

/**
 * Takes all the entities of the second sub-swarm, reinitialises those entities
 * in the first sub-swarm and returns the merged sub-swarm.
 */
public class ScatterMergeStrategy extends MergeStrategy {
    @Override
    public PopulationBasedAlgorithm f(PopulationBasedAlgorithm subSwarm1, PopulationBasedAlgorithm subSwarm2) {
        PopulationBasedAlgorithm newSwarm = new StandardMergeStrategy().f(subSwarm1, subSwarm2);

        for (int i = subSwarm1.getTopology().size(); i < newSwarm.getTopology().size(); i++) {
            newSwarm.getTopology().get(i).reinitialise();
            newSwarm.getTopology().get(i).calculateFitness();
        }

        return newSwarm;
    }
}
