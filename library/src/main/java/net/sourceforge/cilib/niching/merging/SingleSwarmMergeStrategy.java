/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.merging;

import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;

/**
 * Returns a copy of the first sub-swarm.
 */
public class SingleSwarmMergeStrategy extends MergeStrategy {
    @Override
    public SinglePopulationBasedAlgorithm f(SinglePopulationBasedAlgorithm subSwarm1, SinglePopulationBasedAlgorithm subSwarm2) {
        return subSwarm1.getClone();
    }
}
