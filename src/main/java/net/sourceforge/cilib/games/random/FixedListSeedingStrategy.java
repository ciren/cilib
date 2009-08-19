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

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;

/**
 * This class is similar to the {@linkplain ListSeedingStrategy} but differs in the following way.
 * A list of specified seeds are stored. The same number of seeds in the list are used multiple times, and at a fixed interval
 * the next set of seeds in the list are used. This class is used to set a fixed number of seeds and to alternate
 * between them at fixed intervals.
 * @author leo
 *
 */
public class FixedListSeedingStrategy extends RandomListSeedingStrategy {
    private static final long serialVersionUID = -6160406783725056326L;
    int minIndex;
    int maxIndex;
    int useCount;

    public FixedListSeedingStrategy() {
        minIndex = 0;
        maxIndex = 1;
        useCount = 1;
    }

    /**
     * @param other
     */
    public FixedListSeedingStrategy(FixedListSeedingStrategy other) {
        super(other);
        minIndex = other.minIndex;
        maxIndex = other.maxIndex;
        useCount = other.useCount;
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
     * Update the index that is used to get a seed from the list. This index changes at
     * a specified interval.
     */
    @Override
    protected void updateIndex(){
        ++index;
        if(index >= maxIndex)
            index = minIndex;
        //index changes with algorithm iterations
        int currentIteration = AbstractAlgorithm.get().getIterations();
        if (currentIteration != changeIteration && currentIteration  % iterationModulus == 0) {
            changeIteration = currentIteration;
            minIndex += useCount;
            if(minIndex >= seeds.size())
                minIndex = 0;

            maxIndex = minIndex + useCount;
            index = minIndex;
        }
    }

    /**
     * This method sets the number of seeds to repeatedly use in the list for the specified interval
     * @param useCount The number of seeds to repeatedly use in the list
     */
    public void setUseCount(int useCount){
        this.useCount = useCount;
        minIndex = 0;
        maxIndex = useCount;
    }

}
