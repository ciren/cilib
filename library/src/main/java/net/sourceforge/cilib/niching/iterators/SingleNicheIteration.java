/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.iterators;

import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.util.functions.Algorithms;

public class SingleNicheIteration extends NicheIteration {

    @Override
    public SinglePopulationBasedAlgorithm f(SinglePopulationBasedAlgorithm a) {
        return Algorithms.<SinglePopulationBasedAlgorithm>performIteration().f(a);
    }

}
