/*
 * InitialisationStrategy.java
 *
 * Created on April 24, 2006, 2:26 PM
 *
 * Copyright (C) 2003 - 2007
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
package net.sourceforge.cilib.algorithm.initialisation;

import java.io.Serializable;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.problem.OptimisationProblem;

/**
 * Interface for the InitialisationStrategy.
 * @author Gary Pampara
 */
public abstract class PopulationInitialisationStrategy implements Serializable {
	protected int entityNumber;

	public abstract PopulationInitialisationStrategy clone();

	/**
	 * Set the number of entities that are required.
	 * @param entityNumber The number of entities to set
	 */
	public void setEntityNumber(int entityNumber) {
		this.entityNumber = entityNumber;
	}

	/**
	 * Set the entity type to use
	 * @param entityType The entity type to use
	 */
	public abstract void setEntityType(Entity entity);

	/**
	 * Get the current entity type
	 * @return The entity being used.
	 */
	public abstract Entity getEntityType();

	/**
	 * Initialise the {@see net.sourceforge.cilib.entity.Entity} collection based on the given
	 * Topology and Problem.
	 * @param topology The topology to initialise with Entity objects
	 * @param problem The Problem to based the initialisation on
	 */
	public abstract void initialise(Topology<? extends Entity> topology, OptimisationProblem problem);

	/**
	 * Get the number of entities specified to be created by the <code>InitialisationStrategy</code>
	 * @return The number of entities to construct
	 */
	public int getEntityNumber() {
		return this.entityNumber;
	}
}
