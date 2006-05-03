/*
 * InorderBitMutation.java
 * 
 * Created on Jun 22, 2005, 10:00:58 AM
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
package net.sourceforge.cilib.ec.mutationoperators;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Vector;

/**
 * @author otter
 * 
 * Two points within the Entity gets selected and the bits between these two points are mutated.
 * Entities must have a binary representation.
 * 
 */
@Deprecated
public class InorderBitMutation<E extends Entity> extends MutationOperator<E> {

    public void mutate(E entity) {
    	int bitpos1 = random.nextInt(entity.getDimension());
    	int bitpos2 = random.nextInt(entity.getDimension());
    	//re-order the bitpos from small to big, if necessary
    	if(bitpos1 > bitpos2) {
    		int tmp = bitpos1;
    		bitpos1 = bitpos2;
    		bitpos2 = tmp;
    	}
    	
        for(int i = bitpos1; i <= bitpos2; i++) {
            if(random.nextDouble() <= this.paramaterSelectionRate.getParameter()) //flip bit
                ((Vector)entity.get()).setBit(i, !((Vector)entity.get()).getBit(i));
        }
    }
}
