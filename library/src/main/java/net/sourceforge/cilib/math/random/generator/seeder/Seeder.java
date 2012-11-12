/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math.random.generator.seeder;


/**
 *
 * @deprecated This class needs to be replaced by some enum type solution.
 */
@Deprecated
public final class Seeder {
    private SeedSelectionStrategy seedSelectionStrategy;


    /**
     * Create a new instance of the {@linkplain Seeder}. The default {@linkplain SeedSelectionStrategy}
     * defined is the {@linkplain NetworkBasedSeederStrategy}.
     */
    private Seeder() {
        this.seedSelectionStrategy = new NetworkBasedSeedSelectionStrategy();
    }

    /**
     * Get the singleton instance.
     * @return The singleton instance.
     */
    private static Seeder getInstance() {
        return SeederHelper.INSTANCE;
    }


    /**
     * Get a seed value.
     * @return The seed value.
     */
    public static synchronized long getSeed() {
        return getInstance().seedSelectionStrategy.getSeed();
    }

    /**
     * Get the currently defined {@linkplain SeedSelectionStrategy}.
     * @return The current {@linkplain SeedSelectionStrategy}.
     */
    public static SeedSelectionStrategy getSeederStrategy() {
        return getInstance().seedSelectionStrategy;
    }

    /**
     * Set the {@linkplain SeedSelectionStrategy} to use.
     * @param seedSelectionStrategy The value to set.
     */
    public synchronized static void setSeederStrategy(SeedSelectionStrategy seedSelectionStrategy) {
        getInstance().seedSelectionStrategy = seedSelectionStrategy;
    }

    /**
     * This private class is an exploit to the manner in which Java creates instances.
     * As a result we can create an instance this way and ensure that there will not
     * be any concurrency problems as a result of multiple calls to getInstance() within
     * a lazy initialising Singleton.
     */
    private static class SeederHelper {
        public static final Seeder INSTANCE = new Seeder();
    }

}
