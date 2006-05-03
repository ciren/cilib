/*
 * RandomSelection.java
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
import java.util.Collection;
import java.util.Random;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityCollection;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;

/**
 * @author otter
 *
 * Select individuals randomly from the supplied EntityCollection instance.
 * This selection operator has the added constraint that no entity can be selected twice.
 */
@Deprecated
public class RandomSelection<E extends Entity> implements SelectionOperator<E> {
    
    private Random random;
    
    public RandomSelection() {
        random = new MersenneTwister();        
    }
    
    public Collection<E> select(EntityCollection<E> ecol, int selectionSize) {
        if (ecol.size() < selectionSize) {
        	throw new RuntimeException("RandomSelection operator usage Error : Selection size("+ selectionSize+") can not be greater than the population size("+ ecol.size()+").");
        }

        // MARK PHASE = mark the individuals to be selected. 
        int[] mark = new int[ecol.size()];
        for (int i = 0; i < mark.length; i++) {
            mark[i] = 0;
        }
        
        int r = 0;
        for(int i = 0; i < selectionSize; i++) {
            //choose a random number which has not been chosen...
            while(true) {
                r = random.nextInt(ecol.size());
                    if(mark[r] == 0) //it has not been marked.
                        break;
            }
            mark[r] = 1;  //mark the individuals offset
        }
        
        // SWEEP PHASE = sweep up all the ones. 
        ArrayList<E> selection = new ArrayList<E>();
        for(int index = 0; index < mark.length; index++) {
            if (mark[index] == 1) {
                selection.add(ecol.get(index));
            }
        }
        return selection;        
    }
    
    public Random getRandom() {
        return random;
    }
    public void setRandom(Random random) {
        this.random = random;
    }
}
