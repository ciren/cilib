/*
 * ASTSP.java
 * 
 * Created on Apr 28, 2004
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

import java.util.ListIterator;

import net.sourceforge.cilib.container.graph.Edge;
import net.sourceforge.cilib.container.graph.Graph;
import net.sourceforge.cilib.container.graph.Vertex;

/**
 * An implementation of the AS Ant Colony Optimisation algorithm
 * 
 * @author Gary Pampara
 */ 
public class ASTSP extends ACO {
	// FIXME: Shouldn't the solutions be kept in the correct extended Solution class?
	private double tau;
		
	private Graph<Vertex, Edge> graph;
	
	public ASTSP() {
		super();
	}
	
	public void setTau(double tau) {
		this.tau = tau;
	}
	
	public double getTau() {
		return tau;
	}
	
	/**
	 * Perform the needed initialisation for the AS algorithm
	 */
	protected void performInitialisation() {
		super.performInitialisation();
		
		// Set the Edges to have an initial value of tau
		graph = ((GraphOptimisationProblem) super.getOptimisationProblem()).getGraph();
		for (ListIterator<Edge> l = graph.getEdges(); l.hasNext(); ) {
			Edge edge = l.next();
			edge.setWeight(tau);
		}
	}
}
