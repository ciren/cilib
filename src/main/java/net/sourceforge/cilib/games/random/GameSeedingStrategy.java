/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.games.random;

import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.math.random.generator.Seeder;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This class is used to generate random numbers for {@linkplain Game}'s. By making sure the seed is only set once, and at the start of the game,
 * it becomes possible to duplicate games played by fixing the seed.
 *
 * @author leo
 *
 */
public abstract class GameSeedingStrategy implements Cloneable {

    protected Random generator;
    protected long currentSeed;

    public GameSeedingStrategy() {
        currentSeed = Seeder.getSeed();
        generator = new MersenneTwister(currentSeed);
    }

    /**
     * The copy constructor makes a shallow clone. The copy constructor of the Random class re-seeds the generator and that behavior is
     * unwanted. There should only be one instance of this class per game instance.
     * NOTE: I'm not convinced a thread local singleton should be used here, but it is debateable.
     * @param other
     */
    public GameSeedingStrategy(GameSeedingStrategy other) {
        generator = other.generator; //shallow copy
        currentSeed = other.currentSeed;
    }

    /**
     * {@inheritDoc}
     */
    public abstract GameSeedingStrategy getClone();

    /**
     * Get a seed value and seed the generator.
     */
    public abstract void seedGenerator();

    public Random getGenerator() {
        return generator;
    }

    public void setGenerator(Random generator) {
        this.generator = generator;
    }

    public long getCurrentSeed() {
        return currentSeed;
    }
}
