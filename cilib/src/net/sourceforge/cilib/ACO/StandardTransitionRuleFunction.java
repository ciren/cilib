/*
 * StandardTransitionRuleFunction.java
 * 
 * Created on Apr 24, 2004
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

import java.util.ListIterator;

import net.sourceforge.cilib.Container.Graph.Edge;
import net.sourceforge.cilib.Container.Graph.Vertex;

/**
 * @author gpampara
 */
public class StandardTransitionRuleFunction implements TransitionRuleFunction {
	private double alpha;
	private double beta;
	
	public StandardTransitionRuleFunction() {
		alpha = 1.0;
		beta = 5.0;
	}
	
	public StandardTransitionRuleFunction(double alpha, double beta) {
		this.alpha = alpha;
		this.beta = beta;
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.ACO.TransitionRuleFunction#getTransitionalProbability()
	 */
	public double getTransitionalProbability(Ant ant, Vertex currentVertex, Vertex destinationVertex) {
		double numerator = getNumerator(ant, currentVertex, destinationVertex);
		double denominator = getDenominator(ant, currentVertex, destinationVertex);
		
		if (denominator == 0.0) return 0.0; // Prevent a NaN as a result
		
		return (numerator / denominator);
	}
	
	private double getNumerator(Ant ant, Vertex currentVertex, Vertex destinationVertex) {
		if (currentVertex == destinationVertex) return 0.0;
		if (!((TSPAnt)ant).getTabuList().contains(destinationVertex)) return 0.0;
		
		Edge betweenEdge = null;
		for (ListIterator e = currentVertex.getEmanatingEdges(); e.hasNext();) {
			Edge edge = (Edge) e.next();
			if (edge.getOtherAssociatedVertex(currentVertex) == destinationVertex)
				betweenEdge = edge;
		}
		
		if (betweenEdge == null) return 0.0;
		
		return Math.pow(betweenEdge.getWeight(), alpha) * Math.pow(getVisibility(betweenEdge), beta);
	}
	
	
	private double getDenominator(Ant k, Vertex currentVertex, Vertex destinationVertex) {
		double counter = 0.0;
		
		for (ListIterator e = currentVertex.getEmanatingEdges(); e.hasNext(); ) {
			Edge edge = (Edge) e.next();
			
			if (edge.getSecondVertex() == destinationVertex) continue;
			
			counter += Math.pow(edge.getWeight(), alpha) * Math.pow(getVisibility(edge), beta);
		}
		
		return counter;
	}
	
	private double getVisibility(Edge edge) {
		return (1 / edge.getCost());
	}
}
