/*
 * GaussianMutation.java
 * 
 * Created on Aug 20, 2005
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
 * Mutation operator, which mutate by adding small values to the entities paramaters, based on a probability,
 * were the small values are sampled from a gaussian distribution.
 */
@Deprecated
public class GaussianMutation<E extends Entity> extends ContinousRSDMutation<E> {

    public void mutate(E entity) {
        for(int i = 0; i < entity.getDimension(); i++) {
            if(this.random.nextDouble() <= this.paramaterSelectionRate.getParameter()) { 
                ((Vector)entity.get()).setReal(i,((Vector)entity.get()).getReal(i) + randomSample.getGaussian());
            }
        }
    }
}
