/*
 * Copyright (C) 2003 - 2008
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

import net.sourceforge.cilib.algorithm.Algorithm;

/**
 * This class is similar to the {@linkplain RandomListSeedingStrategy} but differs in the following way.
 * A list of specified seeds are stored. A seed in the list is used for a specified interval, and then
 * the next seed in the list is used. This class is used to set a fixed number of seeds and to alternate 
 * between them at fixed intervals.
 * @author leo
 *
 */
public class FixedListSeedingStrategy extends RandomListSeedingStrategy {

    private static final long serialVersionUID = -6160406783725056326L;

    public FixedListSeedingStrategy() {
    }

    /**
     * @param other
     */
    public FixedListSeedingStrategy(RandomListSeedingStrategy other) {
        super(other);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public FixedListSeedingStrategy getClone(){
        return new FixedListSeedingStrategy(this);
    }
    /**
     * The seeds are never changed
     */
    @Override
    protected void updateSeed(){
        //seeds dont change
    }
    /**
     * Set a seed value to use
     * @param seed the value
     */
    public void setSeed(long seed){
        seeds.add(seed);
    }
    /**
     * Update the index that is used to get a seed from the list. This index changes at
     * a specified interval.
     */
    @Override
    protected void updateIndex(){    
        //index changes with algorithm iterations
        int currentIteration = Algorithm.get().getIterations();
        if(currentIteration != changeIteration && currentIteration  % iterationModulus == 0){
            changeIteration = currentIteration;
            ++index;
            if(index >= seeds.size())
                index = 0;            
            System.out.println("Index changed. Iteration: " + currentIteration + " new Seed: " + seeds.get(index));
        }
    }

}
