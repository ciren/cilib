/*
 * TournamentSelection.java
 * 
 * Created on Jun 21, 2005
 *
 * Copyright (C) 2003, 2004, 2005 - CIRG@UP 
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
 * 
 */
package net.sourceforge.cilib.ec.selectionoperators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityCollection;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;

/**
 * @author otter
 *
 * TournamentSelection - a number of entities, according to the defined tournament size, gets picked randomly from supplied entity-collection.
 * The Entity with the best fitness, from the selected tournament competitors, is declared the winner and selected.
 */
@Deprecated
public class TournamentSelection<E extends Entity> implements SelectionOperator<E> {

    private Random random;
    private int tournamentSize = 2;
    
    public TournamentSelection() {
        random = new MersenneTwister();
    }
    
    public List<E> select(EntityCollection<E> ecol, int selectionSize) {

        if (ecol.size() < selectionSize) {
            throw new RuntimeException("Usage Error : Selection size("+ selectionSize+") can not be greater than the population size("+ ecol.size()+").");
        }//Tournament size must be at least one.
        if(tournamentSize < 1 || tournamentSize > ecol.size()) {
            throw new RuntimeException("Usage error : The tournament size must be >= 1 and <= the genertion size.");
        }
        
        ArrayList<E> selected = new ArrayList<E>();
        ArrayList<Integer> competitorsIndex = new ArrayList<Integer>();
        E best;
        
    	//The selectionSize is equal to the number of tournaments which must be held. Repeat for number of selected individuals needed...
        for(int i = 0; i < selectionSize; i++) {
        	//first get the competitors
        	competitorsIndex.clear();
        	for(int c = 0; c < tournamentSize; c++) {
        		competitorsIndex.add(random.nextInt(ecol.size()));
        	}
        	//hold the tournament
            best = ecol.get(competitorsIndex.get(0));
            for(Integer index : competitorsIndex) {
            	if(best.compareTo(ecol.get(index)) < 0) {  //fitness of competitor is superior to the best thus far
            		best = ecol.get(index);
            	}
            }
            //add the winner to the selected list
            selected.add(best);
        }
        return selected;
    }
    
    public Random getRandom() {
        return random;
    }
    public void setRandom(Random random) {
        this.random = random;
    }
    public int getTournamentSize() {
        return tournamentSize;
    }
    public void setTournamentSize(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }
}
