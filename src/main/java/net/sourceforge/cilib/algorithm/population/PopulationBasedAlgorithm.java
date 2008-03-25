/*
 * PopulationBasedAlgorithm.java
 *
 * Created on Feb 8, 2006
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
package net.sourceforge.cilib.algorithm.population;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;

/**
 * Base <tt>Algorithm</tt> class for algorithms that focus on Populations of entities. These
 * include PSO , EC, ACO etc.
 * @author Gary Pampara
 */
public abstract class PopulationBasedAlgorithm extends Algorithm {
	protected PopulationInitialisationStrategy initialisationStrategy;

	/**
	 * Create an empty {@linkplain PopulationBasedAlgorithm}.
	 */
	public PopulationBasedAlgorithm() {
	}

	/**
	 * Copy costructor. Duplicate the state of the current instance into
	 * a new instance.
	 * @param copy The {@linkplain PopulationBasedAlgorithm} to copy.
	 */
	public PopulationBasedAlgorithm(PopulationBasedAlgorithm copy) {
		super(copy);
	}

	/**
	 * {@inheritDoc}
	 */
	public abstract PopulationBasedAlgorithm getClone();

	/**
	 * Perform the iteration within the algorithm.
	 */
	protected abstract void algorithmIteration();

	/**
	 * Get the size of the current population within the algorithm.
	 * @return The size of the current Population.
	 */
	public int getPopulationSize() {
		return this.initialisationStrategy.getEntityNumber();
	}

	/**
	 * Get the currently associated topology for the algorithm.
	 * @return The currently associated topology.
	 */
	public abstract Topology<? extends Entity> getTopology();
	
	/**
	 * Set the <tt>Topology</tt> for the population-based algorithm.
	 * @param topology The {@linkplain Topology} to be set.
	 */
	public abstract void setTopology(Topology<? extends Entity> topology);

	/**
	 * Get the currently set {@linkplain PopulationInitialisationStrategy}.
	 * @return The current {@linkplain PopulationInitialisationStrategy}.
	 */
	public PopulationInitialisationStrategy getInitialisationStrategy() {
		return initialisationStrategy;
	}

	/**
	 * Set the {@linkplain PopulationInitialisationStrategy} to be used.
	 * @param initialisationStrategy The {@linkplain PopulationInitialisationStrategy} to use.
	 */
	public void setInitialisationStrategy(PopulationInitialisationStrategy initialisationStrategy) {
		this.initialisationStrategy = initialisationStrategy;
	}

}
