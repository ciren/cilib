/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm;

/**
 * This class is the abstract base for all algorithms that do not
 * fall within the normal Computational Intelligence type of algorithm.
 * <p>
 * Examples of such algorithms will include <tt>KMeans</tt>, <tt>Gradient Decent</tt> etc.
 *
 */
public interface SingularAlgorithm extends Algorithm, Stoppable {

}
