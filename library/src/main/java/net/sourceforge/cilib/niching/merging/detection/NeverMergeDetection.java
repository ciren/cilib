/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.merging.detection;

import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;

public class NeverMergeDetection extends MergeDetection {
    @Override
    public Boolean f(SinglePopulationBasedAlgorithm a, SinglePopulationBasedAlgorithm b) {
        return false;
    }
}
