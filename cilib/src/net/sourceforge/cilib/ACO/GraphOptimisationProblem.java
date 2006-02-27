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

import net.sourceforge.cilib.Container.Graph.Graph;
import net.sourceforge.cilib.Domain.DomainComponent;
import net.sourceforge.cilib.Domain.Unknown;
import net.sourceforge.cilib.Problem.DataSet;

/**
 * @author gpampara
 */
public class GraphOptimisationProblem implements DiscreteOptimisationProblem {
	protected Graph graph;
	protected DataSet dataSet;
	
	public GraphOptimisationProblem() {
		graph = null;
		dataSet = null;
	}

	public DomainComponent getDomain() {
		return new Unknown();
	}
	
	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
		initialiseData();
	}
	
	protected void initialiseData() {
		GraphCreationFactory factory = new GraphCreationFactory();
		//graph = factory.createGraphFromDataSet(dataSet);
		graph = factory.createStaticGraph();
	}
	
	public Graph getGraph() {
		return graph;
	}

	public void degrade() {
	}
}
