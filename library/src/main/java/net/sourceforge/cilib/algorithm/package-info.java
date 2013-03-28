/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

package net.sourceforge.cilib.algorithm;

/**
 * The <it>Algorithm</it> package provides the generic algorithm foundation
 * for all {@link Algorithm} instances.
 *
 * All {@link Algorithm}s are defined to execute as a separate thread within
 * the Java VM. The the contained components within the algorithm class are
 * pluggable, resulting in each algorithm providing a generic skeleton that is
 * filled in during construction of the algorithm instance.
 *
 * <p>
 * The algorithms can be classified into 3 distinct categories:
 * </p>
 * <ul>
 *   <li>{@linkplain net.sourceforge.cilib.algorithm.SingularAlgorithm Singular Algorithms}</li>
 *   <li>{@linkplain net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm Single Population Based Algorithms}</li>
 *   <li>{@linkplain net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm Multi Population Based Algorithms}</li>
 * </ul>
 *
 * <p>
 * Please refer to the individual algorithm classes for more information
 * on what properties are exposed and for what is available.
 * </p>
 */
