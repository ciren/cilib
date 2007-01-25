/*
 * ParentKidSelection.java
 * 
 * Created on Jul 20, 2005
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

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityCollection;

/**
 * @author otter
 * 
 * Usually used in Differential Evolution and EP were the fittest Entity between the parent and it's resulting kid is selected to survive
 * to the next generation.
 */
@Deprecated
public class ReplacementSelection<E extends Entity> implements SelectionOperator<E> {
    /**
     * @precondition The entity-collection passed as a parameter, must be the result of an addition of the current generation with the resulting
     * offspring (where the number of offspring is equal to the size of current generation). 
     * @precondtion All the individuals within the passed entity-collection must have their fitnesses evaluated.
     * @precondition The selectionSize must be equal to the generation size.
     */
    public List<E> select(EntityCollection<E> popTop, int selectionSize) {
        ArrayList<E> selection = new ArrayList<E>();
        
        for(int i = 0; i < selectionSize; i++) {
            if(popTop.get(i).compareTo(popTop.get(i+selectionSize)) >= 0)
                selection.add(popTop.get(i));
            else
                selection.add(popTop.get(i+selectionSize));
        }
        return selection;
    }
}
