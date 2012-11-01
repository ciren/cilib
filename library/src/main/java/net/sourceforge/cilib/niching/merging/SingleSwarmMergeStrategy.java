/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.merging;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

/**
 * Returns a copy of the first sub-swarm.
 */
public class SingleSwarmMergeStrategy extends MergeStrategy {
    @Override
    public PopulationBasedAlgorithm f(PopulationBasedAlgorithm subSwarm1, PopulationBasedAlgorithm subSwarm2) {
        return subSwarm1.getClone();
    }
}
