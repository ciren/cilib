/*
 * ACO.java
 *
 * Created on March 18, 2004, 4:23 PM
 *
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
 *
 */

package net.sourceforge.cilib.ACO;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.cilib.ACO.Pheromone.StandardPheromoneUpdate;
import net.sourceforge.cilib.Algorithm.Algorithm;
import net.sourceforge.cilib.Problem.DataSet;

/**
 * @author gpampara
 */
// TODO: Get the needed data structure into memory for the algorithm to run against
public class ACO extends Algorithm /*implements GraphOptimisationAlgorithm*/  {
	protected int numberOfAnts;
	protected ArrayList shortestPath;
	protected double shortestPathLength;
	
	protected ArrayList ants; // FIXME: This should be abstracted further into a container?
	protected TransitionRuleFunction function;
	protected StandardPheromoneUpdate pheromoneUpdate;
	protected DataSet dataSet;
	protected GraphOptimisationProblem problem;
		
	public ACO() {
		//problem = null;
		function = null;
		pheromoneUpdate = null;
		//System.out.println("this is the ACO Test");
	}

	// FIXME: This should be part of the problem and not the algorithm
	public void setDataSet(DataSet dataSet) {
		// do whatever we want with the data
		System.out.println("Set the data set");
		this.dataSet = dataSet;
	}
	
	/**
	 * @see net.sourceforge.cilib.Algorithm.Algorithm#performIteration()
	 */
	protected void performIteration() {
		//System.out.println("Performing an iteration");		
	}
	
	protected void performInitialisation() {
		DataSet tmp = problem.getDataSet();
		// TODO do something with the dataset, maybe store it in a class member variable and use it in a subclass
	}
	
	/**
	 * Sets the number of ants in the current algorithm
	 * @param numberOfAnts The number of ants to use within the algorithm
	 */
	public void setNumberOfAnts(int numberOfAnts) {
		this.numberOfAnts = numberOfAnts;
	}
	
	/**
	 * Gets the current number of ants being used in the algorithm
	 * @return The number of ants being used within the algorithm
	 */
	public int getNumberOfAnts() {
		return numberOfAnts;
	}
	
	/**
	 * Set the current transition rule for the ant colony optimisation algorithm
	 * @param f The <code>TransitionRuleFunction</code> to be used in the algorithm
	 */
	public void setTransitionRuleFunction(TransitionRuleFunction f) {
		function = f;
	}
	
	/**
	 * Set the current pheromone update strategy to be used by the algorithm
	 * @param pheromoneUpdate The PheromoneUpdate object to be used to perform pheromone updates
	 */
	public void setPheromoneUpdate(StandardPheromoneUpdate pheromoneUpdate) {
		this.pheromoneUpdate = pheromoneUpdate;
	}
	
	/**
	 * Get the current PheromoneUpdate method used in the algorithm
	 * @return The PheromoneUpdate currently used
	 */
	public StandardPheromoneUpdate getPheromoneUpdate() {
		return pheromoneUpdate;
	}
	
	public Collection getSolution() {
		throw new RuntimeException("Cannot get a solution from an abstract interface");
	}
	
	/**
	 * Get the current collection of ants being used in the algorithm
	 * @return The collection of ants being used
	 */
	public Collection getAntList() {
		return ants;
	}
	
	/**
	 * Set the Current GraphOptimisationProblem
	 * @param problem The GraphOptimisationProblem to use
	 */
	public void setGraphOptimisationProblem(GraphOptimisationProblem problem) {
		this.problem = problem;
	}
		
	public GraphOptimisationProblem getGraphOptimisationProblem() {
		return problem;
	}
}
