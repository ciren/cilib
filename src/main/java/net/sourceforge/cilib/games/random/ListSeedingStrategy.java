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

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a list of seed values to use. When a seed is requested the
 * current index in the list is returned, and the index moves to the next item in the list.
 * When the index reaches the end of the list, it goes back to the beginning.
 * @author leo
 */
public class ListSeedingStrategy extends GameSeedingStrategy {
    private static final long serialVersionUID = 7525722649417922902L;
    protected List<Long> seeds;
    protected int index;
    public ListSeedingStrategy() {
        seeds = new ArrayList<Long>();
        index = -1;
    }

    /**
     * @param other
     */
    public ListSeedingStrategy(ListSeedingStrategy other) {
        super(other);
        seeds = new ArrayList<Long>();
        for(Long seed: other.seeds){
            seeds.add(seed);
        }
        index = other.index;
    }

    /**
     * Change the index that is used in the list of seeds.
     *
     */
    protected void updateIndex(){
        ++index;
        if(index >= seeds.size())
            index = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void seedGenerator() {
        updateIndex();
        currentSeed = seeds.get(index);
        generator.setSeed(currentSeed);
    }

    /**
     * Set a seed value to use
     * @param seed The seed value
     */
    public void setSeed(long seed){
        seeds.add(seed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameSeedingStrategy getClone() {
        return new ListSeedingStrategy(this);
    }

}
