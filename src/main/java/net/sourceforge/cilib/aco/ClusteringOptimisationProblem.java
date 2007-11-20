/*
 * ClusteringOptimisationProblem.java
 *
 * Created on Jul 28, 2004
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
import net.sourceforge.cilib.container.Matrix;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.DomainRegistry;

/**
 * @author Gary Pampara
 */
public class ClusteringOptimisationProblem extends ACOOptimisationProblem {
	private static final long serialVersionUID = 3297883559877806563L;
	
	private DataSetBuilder dataSetBuilder;
	private Matrix<Object> grid;
	
	public ClusteringOptimisationProblem() {
		
	}
	
	public ClusteringOptimisationProblem(ClusteringOptimisationProblem copy) {
		this.dataSetBuilder = copy.dataSetBuilder.clone();
		this.grid = copy.grid.clone();
	}
	
	public ClusteringOptimisationProblem clone() {
		return new ClusteringOptimisationProblem(this);
	}

	protected Fitness calculateFitness(Object solution) { // FIXME: What do I do here?
		return null;
	}

	public Matrix<Object> getGrid() {
		return grid;
	}
	
	public void setDataSetBuilder(DataSetBuilder dataSetBuilder) {
		this.dataSetBuilder = dataSetBuilder;
		
		initialiseDataSet();
	}
	
	public DataSetBuilder getDataSetBuilder() {
		return dataSetBuilder;
	}
	
	private void initialiseDataSet() {
		System.out.println("Now initialising the data set for a ClusteringOptimisationProblem");		
	}

	// This is an empty implementation to comply with the template 
	// method design pattern
	public void degrade(PheromoneUpdate pheromoneUpdate) {
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Problem.OptimisationProblem#getFitness(java.lang.Object, boolean)
	 */
	public Fitness getFitness(Object solution, boolean count) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Problem.OptimisationProblem#getFitnessEvaluations()
	 */
	public int getFitnessEvaluations() {
		// TODO Auto-generated method stub
		return 0;
	}

	public DomainRegistry getDomain() {
		throw new RuntimeException("Get domain on ClusteringOptimisationProblem still needs to be defined!");
	}

	public DomainRegistry getBehaviouralDomain() {
		// TODO Auto-generated method stub
		return null;
	}
}
