/*
 * GraphOptimisationProblem.java
 *
 * Created on Jun 11, 2004
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

import net.sourceforge.cilib.aco.pheromone.PheromoneUpdate;
import net.sourceforge.cilib.container.graph.Edge;
import net.sourceforge.cilib.container.graph.Graph;
import net.sourceforge.cilib.container.graph.Vertex;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.DomainRegistry;

/**
 * A specialised form of a <code>DiscreteOptimisationProblem</code> is a problem defined
 * to work on a <code>Graph</code>.
 * 
 * @author Gary Pampara
 */
public class GraphOptimisationProblem extends ACOOptimisationProblem {
	private static final long serialVersionUID = 1L;
	
	protected Graph<Vertex, Edge> graph;
	protected DataSetBuilder dataSetBuilder;
	
	/**
	 * 
	 *
	 */
	public GraphOptimisationProblem() {
		graph = null;
		dataSetBuilder = null;
	}
	
	public GraphOptimisationProblem(GraphOptimisationProblem copy) {
		
	}
	
	public GraphOptimisationProblem clone() {
		return new GraphOptimisationProblem(this);
	}

/*	public Domain getDomain() {
		return null;
	}*/
	
	/**
	 * 
	 * @param dataSetBuilder
	 */
	public void setDataSetBuilder(DataSetBuilder dataSetBuilder) {
		this.dataSetBuilder = dataSetBuilder;
		initialiseData();
	}
	
	
	/**
	 * @return Returns the dataSet.
	 */
	public DataSetBuilder getDataSetBuilder() {
		return dataSetBuilder;
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
	public Graph<Vertex, Edge> getGraph() {
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

	public DomainRegistry getDomain() {
		throw new RuntimeException("Get domain on GraphOptimisationProblem still needs to be defined!");
	}

	public DomainRegistry getBehaviouralDomain() {
		throw new RuntimeException("Get behavioural domain on GraphOptimisationProblem still needs to be defined!");
	}

	
}
