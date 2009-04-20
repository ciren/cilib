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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.math.random.generator.Seeder;

/**
 * @author leo
 * This is a seeding strategy that does the following.
 * At a specified interval a specified number of seeds are generated. These seeds are stored in a list.
 * Every time the {@code seedGenerator} method is called a new seed from this list is used.
 * Therefore this class generates a fixed number of seeds and alternates between them. And this list of seeds can be 
 * re-generated at fixed intervals
 */
public class RandomListSeedingStrategy extends GameSeedingStrategy {
    private static final long serialVersionUID = 4624862599194808370L;
    List<Long> seeds;
    int index;
    
    int iterationModulus;
    int changeIteration;
    public RandomListSeedingStrategy() {
        seeds = new ArrayList<Long>();
        index = 0;
        iterationModulus = 1;
        changeIteration = 0;
    }

    /**
     * @param other
     */
    public RandomListSeedingStrategy(RandomListSeedingStrategy other) {
        super(other);
        seeds = new ArrayList<Long>();
        for(Long seed: other.seeds){
            seeds.add(seed);
        }
        index = other.index;
        iterationModulus = other.iterationModulus;
        changeIteration = other.changeIteration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameSeedingStrategy getClone() {
        return new RandomListSeedingStrategy(this);
    }
    /**
     * Check if the seed list needs to be randomised. If so, generate a new list of seeds    
     */
    protected void updateSeed(){
        int currentIteration = Algorithm.get().getIterations();
        if(currentIteration != changeIteration && currentIteration  % iterationModulus == 0){
            changeIteration = currentIteration;
            randomizeSeeds(seeds.size());                
        }
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
        updateSeed();
        currentSeed = seeds.get(index);
        updateIndex();
        generator.setSeed(currentSeed);
    }
    /**
     * Generate a list of seeds with the specified length
     * @param size
     */
    public void randomizeSeeds(int size){
        seeds = new ArrayList<Long>();
        for(int i = 0; i < size; ++i){
            seeds.add(i, Seeder.getSeed());
        }        
    }

    /**
     * set the iteration at which the seeds will be regenerated.
     * @param iterationModulus
     */
    public void setIterationModulus(int iterationModulus) {
        this.iterationModulus = iterationModulus;
    }
    
    
}
