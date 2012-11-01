/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.diversity.normalisation;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

/**
 * A strategy to normalise a diversity measurement.
 * 
 */
public interface DiversityNormalisation {
    double getNormalisationParameter(PopulationBasedAlgorithm algorithm);
}
