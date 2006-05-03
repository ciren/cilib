/*
 * SelectionOperator.java
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

import java.util.Collection;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityCollection;

/**
 * @author otter
 *
 * Defines a common interface for all selection operators.
 * Every Selection Operator must implement this interface. 
 */
@Deprecated
public interface SelectionOperator<E extends Entity> {
	/**
	 * Receives a EntityCollection (population(EC), topology(PSO), etc and the number of Entities that one wants
	 * to select out of the EntityCollection.
	 * Result is send back using the java Collection hierarchy.
	 * @param ecol - EntityCollection
	 * @param selectionSize - number of Entities to select
	 * @return Collection containing the selected Entities
	 */
    public Collection<E> select(EntityCollection<E> ecol, int selectionSize);
}
