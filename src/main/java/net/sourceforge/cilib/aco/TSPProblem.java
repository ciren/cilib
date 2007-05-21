/*
 * TSPProblem.java
 *
 * Created on Sep 8, 2004
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
package net.sourceforge.cilib.aco;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import net.sourceforge.cilib.aco.pheromone.PheromoneUpdate;
import net.sourceforge.cilib.container.graph.Edge;
import net.sourceforge.cilib.container.graph.Graph;
import net.sourceforge.cilib.container.graph.Vertex;

/**
 * An implementation of a TSP (Travelling Salesman) based problem. This problem is a <code>DiscreteOptimisationProblem</code>
 * that is based on a <code>GraphOptimisationProblem</code>
 *  
 * @author Gary Pampara
 */
public class TSPProblem extends GraphOptimisationProblem {
	private static final long serialVersionUID = 7470281924795385913L;
	
	protected Collection<Edge> bestSolution;
	protected double bestSolutionLength;
	
	/**
	 * Initialise the object with the default values. The default values make sense for the manner
	 * in which the problem is defined and used. Also initialise the manner in which the function and
	 * the pheromoneUpdates would occour
	 */
	public TSPProblem() {
		super();
		
		bestSolution = null;
		bestSolutionLength = Double.MAX_VALUE;
	}
	
	/**
	 * Set the best solution for the problem
	 * @param bestSolution The solution to be used as the current best solution
	 */
	public void setBestSolution(Collection<Edge> bestSolution) {
		Collection<Edge> clone = new ArrayList<Edge>();
		
		for (Iterator<Edge> it = bestSolution.iterator(); it.hasNext(); ) {
			clone.add(it.next()); // Should not clone!!! The Edge between Verticies remains constant al all times! 
		}
		
		this.bestSolution = clone;
	}
	
	/**
	 * Get the current best solution for the problem
	 * @return The best solution
	 */
	public Collection<Edge> getBestSolution() {
		return bestSolution;
	}
	
	/**
	 * Set the length of the best solution for the problem
	 * @param length The length of the best solution
	 */
	public void setBestSolutionLength(double length) {
		this.bestSolutionLength = length;
	}
	
	/**
	 * Get the problem's current best solution's length 
	 * @return The length of the current best solution for the problem
	 */
	public double getBestSolutionLength() {
		return bestSolutionLength;
	}

	/**
	 * This method performs the needed degradation on the edges of the
	 * data structure to simulate the pheromone evaporation, after which
	 * the ants can traverse the graph and reinforce the needed paths they
	 * have constructed.
	 */
	public void degrade(PheromoneUpdate pheromoneUpdate) {
		Graph<Vertex, Edge> g = super.getGraph();
		
		for (ListIterator<Edge> l = g.getEdges(); l.hasNext(); ) {
			Edge edge = l.next();
			pheromoneUpdate.evaporate(edge);
		}
	}
}
