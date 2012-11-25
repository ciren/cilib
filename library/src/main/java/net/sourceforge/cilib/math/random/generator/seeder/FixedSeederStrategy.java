/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math.random.generator.seeder;

import fj.data.Stream;

/**
 * A seeder strategy that returns the values given to it as seeds.
 */
public class FixedSeederStrategy implements SeedSelectionStrategy {
    
    private Stream<Long> seeds;
    
    public FixedSeederStrategy() {
        this.seeds = Stream.<Long>nil();
    }

    public long getSeed() {
        seeds = Stream.cycle(seeds);
        Long seed = seeds.head();
        seeds = seeds.tail()._1();
        return seed;
    }
    
    public void setSeed(long seed) {
        this.seeds = seeds.snoc(seed);
    }
}
