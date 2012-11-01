/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.merging.detection;

import fj.F2;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

/**
 * Merge detection strategies for Niching.
 * 
 * Used to merge two swarms into one.
 */
public abstract class MergeDetection extends F2<PopulationBasedAlgorithm, PopulationBasedAlgorithm, Boolean> {
}
