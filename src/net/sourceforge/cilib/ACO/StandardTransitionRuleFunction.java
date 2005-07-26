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
 * This is the StandardTransitionRule used in the cyclic-AS
 *  
 * @author gpampara
 */
public class StandardTransitionRuleFunction implements TransitionRuleFunction {
	private double alpha;
	private double beta;
	
	/**
	 * Construct the <code>StandardTransitionRuleFunction</code> with the default values
	 */
	public StandardTransitionRuleFunction() {
		alpha = 1.0;
		beta = 5.0;
	}
	
	/**
	 * Construct the <code>StandardTransitionRuleFunction</code> with the specified values
	 * @param alpha The proportion for the pheromone levels
	 * @param beta The proportion for the visibility level
	 */
	public StandardTransitionRuleFunction(double alpha, double beta) {
		this.alpha = alpha;
		this.beta = beta;
	}
	
	/**
	 * Set the alpha value for the <code>TransitionRuleFunction</code>
	 * @param alpha The new value of alpha
	 */
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	
	/**
	 * Get the alpha value of the <code>TransitionRuleFunction</code>
	 * @return The alpha value of the <code>TransitionRuleFunction</code>
	 */
	public double getAlpha() {
		return alpha;
	}
	
	/**
	 * Set the beta value for the <code>TransitionRuleFunction</code>
	 * @param beta The new value of beta for the <code>TransitionRuleFunction</code>
	 */
	public void setBeta(double beta) {
		this.beta = beta;
	}
	
	/**
	 * Get the value of beta for the <code>TransitionRuleFunction</code>
	 * @return The value of beta for the <code>TransitionRuleFunction</code>
	 */
	public double getBeta() {
		return beta;
	}
	
	/**
	 * Get the TransitionalProbability for a ant to move from one <code>Vertex</code> to another
	 * 
	 * @param ant The <code>Ant</code> used in the calculation of the TransitionalProbability
	 * @param currentVertex The <code>Vertex</code> the <code>Ant</code> is currently at
	 * @param destinationVertex The target <code>Vertex</code>
	 * @see net.sourceforge.cilib.ACO.TransitionRuleFunction#getTransitionalProbability()
	 */
	public double getTransitionalProbability(Ant ant, Vertex currentVertex, Vertex destinationVertex) {
		double numerator = getNumerator(ant, currentVertex, destinationVertex);
		double denominator = getDenominator(ant, currentVertex, destinationVertex);
		
		if (denominator == 0.0) return 0.0; // Prevent a NaN as a result
		
		return (numerator / denominator);
	}
	
	/**
	 * Return the evaluation of the numerator of the function 
	 * @param ant The current <code>Ant</code>
	 * @param currentVertex The current starting <code>Vertex</code>
	 * @param destinationVertex The destination <code>Vertex</code> for the <code>Ant</code>
	 * @return double describing the value of the numerator of the funciton
	 */
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
	
	/**
	 * Get the denominator value for the transitional probability of the <code>Ant</code>
	 * @param ant The current <code>Ant</code>
	 * @param currentVertex The current <code>Vertex<code>
	 * @param destinationVertex The destination <code>Vertex</code>
	 * @return double representing the denominator of the function
	 */
	private double getDenominator(Ant ant, Vertex currentVertex, Vertex destinationVertex) {
		double counter = 0.0;
		
		for (ListIterator e = currentVertex.getEmanatingEdges(); e.hasNext(); ) {
			Edge edge = (Edge) e.next();
			
			if (edge.getSecondVertex() == destinationVertex) continue;
			
			counter += Math.pow(edge.getWeight(), alpha) * Math.pow(getVisibility(edge), beta);
		}
		
		return counter;
	}
	
	/**
	 * Get the visibility which is defined to be the inverse of the distance of the <code>Edge</code>
	 * @param edge The <code>Edge</code> to base the visiblity on
	 * @return The visibility which is the inverse of the distance of the <code>Edge</code>
	 */
	private double getVisibility(Edge edge) {
		return (1 / edge.getCost());
	}
}
