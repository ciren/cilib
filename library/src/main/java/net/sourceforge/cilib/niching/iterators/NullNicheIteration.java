/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.iterators;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

public class NullNicheIteration extends NicheIteration {
    @Override
    public PopulationBasedAlgorithm f(PopulationBasedAlgorithm a) {
        return a;
    }
}
