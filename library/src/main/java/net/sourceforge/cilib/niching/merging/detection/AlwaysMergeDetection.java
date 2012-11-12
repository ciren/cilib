/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.merging.detection;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

/**
 * 
 */
public class AlwaysMergeDetection extends MergeDetection {

    @Override
    public Boolean f(PopulationBasedAlgorithm a, PopulationBasedAlgorithm b) {
        return true;
    }
}
