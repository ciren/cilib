/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types;

public interface Randomizable {

    /**
     * Apply a randomization using the provided {@code Random}.
     * @param random The {@code Random} to use for the randomization.
     */
    void randomize();

}
