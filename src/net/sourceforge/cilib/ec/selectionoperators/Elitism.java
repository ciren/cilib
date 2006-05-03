/*
 * Elitism.java
 * 
 * Created on Jun 23, 2005, 9:08:58 AM
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
import java.util.Collections;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityCollection;

/**
 * @author otter
 * 
 * Elitism - select the n most best (elite) entities from the supplied EntityCollection.
 */
@Deprecated
public class Elitism<E extends Entity> implements SelectionOperator<E> {
	
	public Collection<E> select(EntityCollection<E> ecol, int selectionSize) {
        if (ecol.size() < selectionSize) {
            throw new RuntimeException("Elitism operator usage Error : Selection size("+ selectionSize+") can not be greater than the population size("+ ecol.size()+").");
        }
        //copy the whole population to a candidate list.
        ArrayList<E> candidates = new ArrayList<E>(ecol.getAll());
        //sort candidate list according to their fitnesses. Will do this automatically, because Entities is Comparable based on their fitness.
      	Collections.sort(candidates);	
        // The Collections.sort method sort the collection in ascending order, bad to good.
      	// Thus the best n [ n (n=selectionSize)] best entities must be taken of the back of the sorted collection, not the front...
        ArrayList<E> selected = new ArrayList<E>();
        for(int i = 1; i <= selectionSize; i++) {
        	selected.add(candidates.get(candidates.size()-i));        	
        }
        
        return selected;
    }
}
