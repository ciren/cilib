/*
 * CloneCrossOver.java
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
package net.sourceforge.cilib.ec.crossoveroperators;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.cilib.entity.Entity;

/**
 * @author otter
 * 
 * Makes a clone of the parent supplied.
 * One parent needed, returns one cloned offspring.
 */
public class CloneCrossOver<E extends Entity> extends CrossOverOperator<E> {
    
    public CloneCrossOver() {
        this.offspring = 1;
        this.parents = 1;
    }

    public Collection<E> reproduce(Collection<? extends E> parents) {
        if(parents.size() != this.numberOffParentsNeeded())
            throw new RuntimeException("Usage error : CloneCrossOver need on and only one parent. Received " + parents.size()+ " parents");

        ArrayList<E> offspring = new ArrayList<E>(); 
        
        //can safely do the following, because the size of collection is checked previously
        //try {   //clone the parents
        	offspring.add((E)((ArrayList<E>)parents).get(0).clone());
        //} catch (CloneNotSupportedException ex) {
         //   throw new RuntimeException("Could not clone parent Entity");
        //}
        return offspring;
    }
}
