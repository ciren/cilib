/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math.random.generator.seeder;

/**
 * The defined manner within which a seed value is generated and provided to a pseudo-random
 * number generator.
 */
public interface SeedSelectionStrategy {

    /**
     * Obtain the desired seed value based.
     * @return The seed value to use.
     */
    long getSeed();

}
