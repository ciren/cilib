/*
 * Ant.java
 * 
 * Created on Apr 28, 2004
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
package net.sourceforge.cilib.aco;

import java.util.Collection;

import net.sourceforge.cilib.aco.pheromone.PheromoneUpdate;
import net.sourceforge.cilib.container.graph.Edge;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.OptimisationProblem;


/**
 * This is the basic <code>Ant</code> interface for all the implemented <code>Ant</code>s to be
 * based on.
 * 
 * @author Gary Pampara
 */
public abstract class Ant implements Entity {
	/**
	 * Return a clone of the current Ant object instance
	 * @return An <code>Object</code> representing a clone of the current <code>Ant</code> object
	 * @throws CloneNotSupportedException
	 */
	public abstract Ant clone();

	/**
	 * Build a valid tour based on the current <code>DiscreteOptimisationProblem</code> 
	 * @param problem The problem to construct a tour on
	 */
	public abstract void buildTour(OptimisationProblem problem);

	/**
	 * Calculate the fitness of the <code>Ant</code>
	 */
	public abstract void calculateFitness();

	/**
	 * Update the best solution based on the current <code>Problem</code> based on the premis
	 * that the current solution is better. If not, do not update the best solution.
	 * 
	 * @param problem The <code>Problem</code> to have the best solution derived from
	 */
	public abstract void updateBestSolution(OptimisationProblem problem);

	/**
	 * Update the <code>Pheromone</code> levels
	 * @param problem The <code>Problem</code> to have the pheromone levels adjusted
	 */
	public abstract void updatePheromoneTrail(OptimisationProblem problem);

	/**
	 * Initialise the state of the <code>Ant</code> based on the supplied <code>Problem</code>
	 * and <code>Randomiser</code>
	 * 
	 * @param problem The <code>Problem</code> to be used for the <code>Ant</code> intialisation
	 */
	public abstract void initialise(OptimisationProblem problem);
	
	/**
	 * Return the current tour created by the <code>Ant</code>
	 * @return The current generated tour.
	 */
	public abstract Collection<Edge> getCurrentTour();
	
	/**
	 * Get a reference to the associated <code>PheromoneUpdate</code> for the <code>Ant</code>
	 * @return The <code>PheromoneUpdate</code> required for updates
	 */
	public abstract PheromoneUpdate getPheromoneUpdate();
	
	/**
	 * Get the associated <code>TransitionRuleFunction</code> for this <code>Ant</code>
	 * @return The associated <code>TransitionRuleFunction</code>
	 */
	public abstract TransitionRuleFunction getTransitionRuleFunction();
}
