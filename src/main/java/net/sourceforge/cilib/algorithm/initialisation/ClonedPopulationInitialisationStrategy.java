/*
 * EntityCloneInitialisationBuilder.java
 *
 * Created on April 24, 2006, 2:26 PM
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
 */
package net.sourceforge.cilib.algorithm.initialisation;

import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.problem.OptimisationProblem;

/**
 * Create a collection of {@linkplain Entity} objects by cloning the given
 * prototype {@linkplain Entity}.
 * 
 * @author Gary Pampara
 */
public class ClonedPopulationInitialisationStrategy extends PopulationInitialisationStrategy {
	private static final long serialVersionUID = -7354579791235878648L;
	private Entity prototypeEntity;

	/**
	 * Create an instance of the {@linkplain ClonedPopulationInitialisationStrategy}.
	 */
	public ClonedPopulationInitialisationStrategy() {
		entityNumber = 20;
		prototypeEntity = null; // This has to be manually set as Individuals are used in GAs etc...
	}

	/**
	 * Copy constructor. Create a copy of the given instance.
	 * @param copy The instance to copy.
	 */
	public ClonedPopulationInitialisationStrategy(ClonedPopulationInitialisationStrategy copy) {
		this.entityNumber = copy.entityNumber;
		this.prototypeEntity = copy.prototypeEntity.getClone();
	}

	/**
	 * {@inheritDoc}
	 */
	public ClonedPopulationInitialisationStrategy getClone() {
		return new ClonedPopulationInitialisationStrategy(this);
	}

	/**
	 * Perform the required initialisation, using the provided <tt>Topology</tt> and
	 * <tt>Problem</tt>.
	 * @param topology The given <tt>Topology</tt> to use in initialisation.
	 * @param problem The <tt>Problem</tt> to use in the initialisation of the topology.
	 * @throws InitialisationException if the initialisation cannot take place.
	 */
	@SuppressWarnings("unchecked")
	public void initialise(Topology topology, OptimisationProblem problem) {
		if (problem == null)
			throw new InitialisationException("No problem has been specified");

		if (prototypeEntity == null)
			throw new InitialisationException("No prototype Entity object has been defined for the clone operation in the entity constrution process.");

		for (int i = 0; i < entityNumber; ++i) {
			Entity entity = (Entity) prototypeEntity.getClone();

			entity.initialise(problem);
			topology.add(entity);
		}
	}

	/**
	 * Get the {@linkplain Entity} that has been defined as the prototype to for
	 * the copies.
	 * @return The prototype {@linkplain Entity}.
	 */
	public Entity getPrototypeEntity() {
		return prototypeEntity;
	}

	/**
	 * Set the prototype {@linkplain Entity} for the copy process.
	 * @see ClonedPopulationInitialisationStrategy#setEntityType(Entity)
	 * @param prototypeEntity The {@linkplain Entity} to use for the cloning process.
	 */
	public void setPrototypeEntity(Entity prototypeEntity) {
		this.prototypeEntity = prototypeEntity;
	}

	/**
	 * Set the prototype {@linkplain Entity} for the copy process.
	 * @param prototypeEntity The {@linkplain Entity} to use for the cloning process.
	 */
	public void setEntityType(Entity entityType) {
		this.setPrototypeEntity(entityType);
	}

	/**
	 * Get the {@linkplain Entity} that has been defined as the prototype to for
	 * the copies.
	 * @see ClonedPopulationInitialisationStrategy#getPrototypeEntity()
	 * @return The prototype {@linkplain Entity}.
	 */
	public Entity getEntityType() {
		return this.prototypeEntity;
	}
}
