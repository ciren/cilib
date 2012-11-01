/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.merging;

import fj.F2;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

/**
 * Merge strategies for Niching.
 * 
 * Combines two PopulationBasedAlgorithms into one, returns the combined one.
 */
public abstract class MergeStrategy extends F2<PopulationBasedAlgorithm, PopulationBasedAlgorithm, PopulationBasedAlgorithm> {
}
