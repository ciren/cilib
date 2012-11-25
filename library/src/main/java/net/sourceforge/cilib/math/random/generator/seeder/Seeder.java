/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math.random.generator.seeder;

public final class Seeder {

    private SeedSelectionStrategy seedSelectionStrategy;

    private Seeder() {
        this.seedSelectionStrategy = new NetworkBasedSeedSelectionStrategy();
    }
    
    private void setSeedSelectionStrategy(SeedSelectionStrategy s) {
        this.seedSelectionStrategy = s;
    }

    public static void setSeederStrategy(SeedSelectionStrategy s) {
        SeederHelper.instance.setSeedSelectionStrategy(s);
    }

    public static synchronized long getSeed() {
        return SeederHelper.instance.seedSelectionStrategy.getSeed();
    }
    
    private static class SeederHelper {
        public static Seeder instance = new Seeder();
    }
}
