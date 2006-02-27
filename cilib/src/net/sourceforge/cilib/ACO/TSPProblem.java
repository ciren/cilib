/*
 * TSPProblem.java
 *
 * Created on Sep 8, 2004
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
package net.sourceforge.cilib.ACO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;

import net.sourceforge.cilib.ACO.Pheromone.PheromoneUpdate;
import net.sourceforge.cilib.ACO.Pheromone.StandardPheromoneUpdate;
import net.sourceforge.cilib.Container.Graph.Graph;
import net.sourceforge.cilib.Container.Graph.Edge;

/**
 * @author gpampara
 */
public class TSPProblem extends GraphOptimisationProblem {
	protected ArrayList bestSolution;
	protected double bestSolutionLength;
	protected TransitionRuleFunction function;
	protected PheromoneUpdate pheromoneUpdate; 
	
	public TSPProblem() {
		super();
		
		bestSolution = null;
		bestSolutionLength = Double.MAX_VALUE;
		function = new StandardTransitionRuleFunction();
		pheromoneUpdate = new StandardPheromoneUpdate();
	}
	
	public TransitionRuleFunction getFunction() {
		return function;
	}
	
	public void setTranstionRuleFunction(TransitionRuleFunction function) {
		this.function = function;
	}
	
	public PheromoneUpdate getPheromoneUpdate() {
		return pheromoneUpdate;
	}
	
	public void setPheromoneUpdate(PheromoneUpdate pheromoneUpdate) {
		this.pheromoneUpdate = pheromoneUpdate;
	}
	
	public void setBestSolution(Collection bestSolution) {
		this.bestSolution = null;
		this.bestSolution = (ArrayList) bestSolution;
	}
	
	public Collection getBestSolution() {
		return bestSolution;
	}
	
	public void setBestSolutionLength(double length) {
		this.bestSolutionLength = length;
	}
	
	public double getBestSolutionLength() {
		return bestSolutionLength;
	}

	/**
	 * This method performs the needed degradation on the edges of the
	 * data structure to simulate the pheromone evaporation, after which
	 * the ants can traverse the graph and reinforce the needed paths they
	 * have constructed.
	 */
	public void degrade() {
		Graph g = super.getGraph();

		for (ListIterator<Edge> l = g.getEdges(); l.hasNext(); ) {
			Edge edge = l.next();
			pheromoneUpdate.evaporate(edge);
		}
	}
}
