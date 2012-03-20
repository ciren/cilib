/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.coevolution.competitors;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This class contains the entity data, fitness and population id of a competitor used in a coevolution algorithm
 */
public class Competitor implements Cloneable {
	private static final long serialVersionUID = 2514848906149566022L;
	Type entityData;
	int populationID;
	Fitness entityFitness;
	public Competitor(Type entityData, Fitness entityFitness, int populationID) {
		this.populationID = populationID;
		this.entityFitness = entityFitness;
		this.entityData = entityData; //no clone, reference to the data
	}

	public Competitor(Type entityData, int populationID) {
		this.populationID = populationID;
		this.entityData = entityData; //no clone, reference to the data
	}

	public Competitor(Competitor other) {
		populationID = other.populationID;
		entityData = other.entityData;
	}

	public Type getEntityData(){
		return entityData;
	}

	public int getPopulationID(){
		return populationID;
	}

	/**
	 * {@inheritDoc}
	 */
	public Competitor getClone() {
		return new Competitor(this);
	}

	public Fitness getEntityFitness() {
		return entityFitness;
	}

}
