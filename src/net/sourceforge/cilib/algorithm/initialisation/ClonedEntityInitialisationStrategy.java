/*
 * EntityCloneInitialisationBuilder.java
 *
 * Created on April 24, 2006, 2:26 PM
 *
 *
 * Copyright (C) 2003 - 2006 
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

import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.problem.OptimisationProblem;

/**
 * 
 * @author Gary Pampara
 */
public class ClonedEntityInitialisationStrategy extends InitialisationStrategy {

	private Entity prototypeEntity;
	
	public ClonedEntityInitialisationStrategy() {
		entities = 20;
		prototypeEntity = null; // This has to be manually set as Individuals are used in GAs etc...
	}


	/**
	 * Perform the required initialisation, using the provided <tt>Topology</tt>
	 * and <tt>Problem</tt>.
	 * 
	 * @param topology The given <tt>Topology</tt> to use in initialisation.
	 * @param problem The <tt>Problem</tt> to use in the initialisation of the topology.
	 * @throws InitialisationException if the initialisation cannot take place.
	 */
	@SuppressWarnings("unchecked")
	public <E extends Entity> void intialise(Topology<E> topology, OptimisationProblem problem) {
		
		 if (problem == null)
			 throw new InitialisationException("No problem has been specified");
		 
		 if (prototypeEntity == null)
			 throw new InitialisationException("No prototype Entity object has been defined for the clone operation in the entity constrution process.");
		 
		 for (int i = 0; i < entities; ++i) {
			 E entity = (E) prototypeEntity.clone();
		    
			 entity.initialise(problem);
			 topology.add(entity);
		 }
	}

	
	/**
	 * 
	 * @return
	 */
	public Entity getPrototypeEntity() {
		return prototypeEntity;
	}

	
	/**
	 * 
	 * @param prototypeEntity
	 */
	public void setPrototypeEntity(Entity prototypeEntity) {
		this.prototypeEntity = prototypeEntity;
	}


	/**
	 * 
	 */
	public void setEntityType(Entity entityType) {
		this.setPrototypeEntity(entityType);		
	}
	
}
