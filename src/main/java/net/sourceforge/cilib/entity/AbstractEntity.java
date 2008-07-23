/*
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.entity;

import java.util.Map;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.positionupdatestrategies.IterationNeighbourhoodBestUpdateStrategy;
import net.sourceforge.cilib.pso.positionupdatestrategies.NeighbourhoodBestUpdateStrategy;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Type;

/**
 * Abstract class definition for all concrete {@linkplain Entity} objects.
 * This class defines the {@linkplain Entity} main data structure for the
 * values stored within the {@linkplain Entity} itself.
 */
public abstract class AbstractEntity implements Entity, CandidateSolution {
	protected Blackboard<Enum<?>, Type> properties = new Blackboard<Enum<?>, Type>();
	private final CandidateSolution candidateSolution;
	protected NeighbourhoodBestUpdateStrategy neighbourhoodBestUpdateStrategy;

	/**
	 * Initialise the candidate solution of the {@linkplain Entity}.
	 */
	protected AbstractEntity() {
		this.candidateSolution = new CandidateSolutionMixin(properties);
		this.neighbourhoodBestUpdateStrategy = new IterationNeighbourhoodBestUpdateStrategy();
	}
	
	/**
	 * Copy constructor. Instantiate and copy the given instance. 
	 * @param copy The instance to copy.
	 */
	protected AbstractEntity(AbstractEntity copy) {
		this();
		
		for (Map.Entry<Enum<?>, Type> entry : copy.properties.entrySet()) {
    		this.properties.put(entry.getKey(), entry.getValue().getClone());
        }
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		
		if ((object == null) || (this.getClass() != object.getClass()))
			return false;
		
		AbstractEntity other = (AbstractEntity) object;
		return this.candidateSolution.equals(other.candidateSolution);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (this.candidateSolution == null ? 0 : this.candidateSolution.hashCode());
		hash = 31 * hash + (this.properties == null ? 0 : this.properties.hashCode());
		return hash;
	}
	
	/**
	 * Get the properties associate with the <code>Entity</code>.
	 * @return The properties within a {@linkplain Blackboard}.
	 */
	public Blackboard<Enum<?>, Type> getProperties() {
		return properties;
	}

	/**
	 * Set the properties for the current <code>Entity</code>.
	 * @param properties The {@linkplain Blackboard} containing the new properties.
	 */
	public void setProperties(Blackboard<Enum<?>, Type> properties) {
		this.properties = properties;
	}

	/**
	 * Get the value of the {@linkplain CandidateSolution} maintained by this 
	 * {@linkplain Entity}.
	 * @return The candidate solution as a {@linkplain Type}.
	 */
	public Type getCandidateSolution() {
		return this.candidateSolution.getCandidateSolution();
	}

	/**
	 * Get the fitness of the {@linkplain CandidateSolution} maintained by this 
	 * {@linkplain Entity}.
	 * @return The {@linkplain Fitness} of the candidate solution.
	 */
	public Fitness getFitness() {
		return this.candidateSolution.getFitness();
	}

	/**
	 * Set the {@linkplain Type} maintained by this {@linkplain Entity}s
	 * {@linkplain CandidateSolution}.
	 * @param candidateSolution The {@linkplain Type} that will be the new value of the
	 *        {@linkplain Entity} {@linkplain CandidateSolution}.
	 */
	public void setCandidateSolution(Type candidateSolution) {
		this.candidateSolution.setCandidateSolution(candidateSolution);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final Fitness getSocialBestFitness() {
		return this.neighbourhoodBestUpdateStrategy.getSocialBestFitness(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Fitness getBestFitness() {
		return getFitness();
	}
	
	/**
	 * Get the reference to the currently employed <code>NeighbourhoodBestUpdateStrategy</code>.
	 * @return A reference to the current <code>NeighbourhoodBestUpdateStrategy</code> object
	 */
	public NeighbourhoodBestUpdateStrategy getNeighbourhoodBestUpdateStrategy() {
		return this.neighbourhoodBestUpdateStrategy;
	}
	
	/**
	 * Set the <code>NeighbourhoodBestUpdateStrategy</code> to be used by the {@linkplain Entity}.
	 * @param neighbourhoodBestUpdateStrategy The <code>NeighbourhoodBestUpdateStrategy</code> to be used
	 */
	public void setNeighbourhoodBestUpdateStrategy(NeighbourhoodBestUpdateStrategy neighbourhoodBestUpdateStrategy) {
		this.neighbourhoodBestUpdateStrategy = neighbourhoodBestUpdateStrategy;
	}
	
}
