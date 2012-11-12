/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math.random.generator.seeder;

/**
 * A {@linkplain SeedSelectionStrategy} that returns 0 as the seed value. As a result this
 * class is rather pointless as the random number generators using this seed will
 * always produce the same sequence.
 */
public class ZeroSeederStrategy implements SeedSelectionStrategy {

    /**
     * {@inheritDoc}
     */
    public long getSeed() {
        return 0;
    }

}
