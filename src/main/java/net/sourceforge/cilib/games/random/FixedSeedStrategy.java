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

/**
 * This seeding strategy always uses the same seed.
 * @author leo
 */
public class FixedSeedStrategy extends GameSeedingStrategy {
    private static final long serialVersionUID = -4840550863563767935L;

    public FixedSeedStrategy() {
    }
    public FixedSeedStrategy(GameSeedingStrategy other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameSeedingStrategy getClone() {
        return new FixedSeedStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void seedGenerator() {
        generator.setSeed(currentSeed);
    }
    /**
     * set the specified seed value to use
     * @param seed
     */
    public void setSeedValue(long seed){
        currentSeed = seed;
    }

}
