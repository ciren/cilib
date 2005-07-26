/*
 * PheramoneUpdate.java
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
package net.sourceforge.cilib.ACO.Pheromone;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import net.sourceforge.cilib.ACO.ACO;
import net.sourceforge.cilib.ACO.Ant;
import net.sourceforge.cilib.ACO.TSPAnt;
import net.sourceforge.cilib.ACO.TSPProblem;
import net.sourceforge.cilib.Algorithm.Algorithm;
import net.sourceforge.cilib.Container.Graph.Edge;

/**
 * @author gpampara
 */
public class StandardPheromoneUpdate implements PheromoneUpdate {
	// TODO: Must find a better way of doing this - Some or other design pattern must be used to make this more generic
	private double rho;
	private double e;
	private double Q;
	
	/**
	 * Standard constructor. Defaults the parameters to "standard" (not sure) values
	 *
	 */
	public StandardPheromoneUpdate() {
		this.rho = 0.5;
		this.e = 5.0;
		this.Q = 100.0;
	}
	
	/**
	 * Determine the current pheromone level on the <code>Edge</code> specified
	 * @param edge The <code>Edge</code> requires
	 * @return The current pheromone level of the specified <code>Edge</code>
	 */
	private double currentPheromoneLevelOfEdge(Edge edge) {
		double result = 0.0;
		ACO aco = (ACO) (Algorithm.get());
		for (Iterator i = aco.getAntCollection().iterator(); i.hasNext(); ) {
			TSPAnt ant = (TSPAnt) i.next();
			ArrayList<Edge> tour = (ArrayList<Edge>) ant.getCurrentTour();
			
			if (tour.contains(edge)) {
				result += (Q/ant.getCurrentTourLength());
			}
		}
		
		return result;
	}
	
	/**
	 * Determine the average pheromone levels of the current shortest tour
	 * @param edge The current <code>Edge</code>
	 * @param shortestPath The current shortest path
	 * @param pathLength The current shortest path, path length
	 * @return The average pheromone of the shortest tour
	 */
	private double averagePheromoneOfShortestTour(Edge edge, ArrayList shortestPath, double pathLength) {
		double result = 0.0;
		
		if (shortestPath.contains(edge)) {
			result += (Q/pathLength);
		}
		
		return result;
	}
	
	/**
	 * 
	 */
	public void updatePheromoneTrail(Ant ant) {
		ArrayList tour = (ArrayList) ant.getCurrentTour();
		TSPProblem p = (TSPProblem) ((ACO) Algorithm.get()).getDiscreteOptimisationProblem();
		
		for (ListIterator l = tour.listIterator(); l.hasNext(); ) {
			Edge edge = (Edge) l.next();
			
//			double currentPheromone = edge.getWeight();
//			double tmp = (1-rho)*currentPheromone;
//			tmp += currentPheromoneLevelOfEdge(edge);
			double tmp = edge.getWeight();
			tmp += currentPheromoneLevelOfEdge(edge);
			tmp += e*averagePheromoneOfShortestTour(edge, (ArrayList) p.getBestSolution(), p.getBestSolutionLength());
			
			edge.setWeight(tmp);
		}
	}

	/**
	 * 
	 */
	public void evaporate(Edge edge) {
		double currentPheromone = edge.getWeight();
		double tmp = (1-rho) * currentPheromone;
		edge.setWeight(tmp);
	}
	
	/**
	 * 
	 * @param rho
	 */
	public void setRho(double rho) {
		this.rho = rho;
	}
	
	/**
	 *
	 * @return
	 */
	public double getRho() {
		return rho;
	}
	
	/**
	 * 
	 * @param e
	 */
	public void setE(double e) {
		this.e = e;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getE() {
		return e;
	}
	
	/**
	 * 
	 * @param Q
	 */
	public void setQ(double Q) {
		this.Q = Q;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getQ() {
		return Q;
	}
}
