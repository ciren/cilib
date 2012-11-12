/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.creation;

import fj.F2;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;

/**
 * Identify any Entity instances that, based on the decision strategy,
 * have found niches. These entities are returned, with the expectation
 * that the callee will use them in some or other manner.
 */
public abstract class NicheDetection extends F2<PopulationBasedAlgorithm, Entity, Boolean> {
}
