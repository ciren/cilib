/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.math.random.generator;


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
