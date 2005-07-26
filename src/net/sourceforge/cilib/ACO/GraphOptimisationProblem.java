/*
 * GraphOptimisationProblem.java
 *
 * Created on Jun 11, 2004
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

import net.sourceforge.cilib.ACO.Pheromone.PheromoneUpdate;
import net.sourceforge.cilib.Container.Graph.Graph;
import net.sourceforge.cilib.Problem.DataSet;
import net.sourceforge.cilib.Problem.Fitness;

/**
 * A specialised form of a <code>DiscreteOptimisationProblem</code> is a problem defined
 * to work on a <code>Graph</code>.
 * 
 * @author gpampara
 */
public class GraphOptimisationProblem implements DiscreteOptimisationProblem {
	protected Graph graph;
	protected DataSet dataSet;
	
	/**
	 * 
	 *
	 */
	public GraphOptimisationProblem() {
		graph = null;
		dataSet = null;
	}

/*	public Domain getDomain() {
		return null;
	}*/
	
	/**
	 * 
	 * @param dataSet
	 */
	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
		initialiseData();
	}
	
	/**
	 * 
	 *
	 */
	protected void initialiseData() {
		GraphCreationFactory factory = new GraphCreationFactory();
		//graph = factory.createGraphFromDataSet(dataSet);
		graph = factory.createStaticGraph();
	}
	
	/**
	 * 
	 * @return
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * 
	 */
	public void degrade(PheromoneUpdate pheromoneUpdate) {
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Problem.OptimisationProblem#getFitness(java.lang.Object, boolean)
	 */
	public Fitness getFitness(Object solution, boolean count) {
		throw new UnsupportedOperationException("getFitness() is not supported by a GrahOptimisationProblem");
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Problem.OptimisationProblem#getFitnessEvaluations()
	 */
	public int getFitnessEvaluations() {
		throw new UnsupportedOperationException("getFitnessEvaluations() is not supported by a GrahOptimisationProblem");
	}
}
