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
import java.util.Collection;
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
	private ArrayList ants;
	private double rho;
	private double e;
	private double Q;
	
	public StandardPheromoneUpdate() {
		this.rho = 0.5;
		this.e = 5.0;
		this.Q = 100.0;
	}
	
	public void updatePheromoneTrail(ArrayList shortestPath, double shortestPathLength) {
		/*ArrayList antTour = (ArrayList) ant.getCurrentTour();
		
		for (ListIterator l = antTour.listIterator(); l.hasNext(); ) {
			Edge edge = (Edge) l.next();
			double currentPheramone = edge.getWeight();
			
			double tmp = (1-rho)*currentPheramone;
			tmp += currentPheromoneLevelOfEdge(edge);
			tmp += e*averagePheromoneOfShortestTour(edge, ant.geshortestPath, shortestPathLength);
			
			edge.setWeight(tmp);
		}*/
	}
	
	public StandardPheromoneUpdate(ArrayList ants, double rho, double e, double Q) {
		//this.ants = ants;
		this.rho = rho;
		this.e = e;
		this.Q = Q;
	}
	
	public void updatePheromoneTrails(ArrayList shortestPath, double shortestPathLength) {
		//System.out.println("Performing pheromone updates on the edges...");
		/*for (ListIterator i = ants.listIterator(); i.hasNext(); ) {
			TSPAnt ant = (TSPAnt) i.next();
			ArrayList antTour = (ArrayList) ant.getCurrentTour();
			
			for (ListIterator l = antTour.listIterator(); l.hasNext(); ) {
				Edge edge = (Edge) l.next();
				double currentPheramone = edge.getWeight();
				
				double tmp = (1-rho)*currentPheramone;
				tmp += currentPheromoneLevelOfEdge(edge);
				tmp += 	e*averagePheromoneOfShortestTour(edge, shortestPath, shortestPathLength);
				
				edge.setWeight(tmp);
			}
		}*/
	}
	
	private double currentPheromoneLevelOfEdge(Edge edge) {
		double result = 0.0;
		for (ListIterator i = ants.listIterator(); i.hasNext(); ) {
			TSPAnt ant = (TSPAnt) i.next();
			ArrayList<Edge> tour = (ArrayList<Edge>) ant.getCurrentTour();
			
			if (tour.contains(edge)) {
				result += (Q/ant.getCurrentTourLength());
			}
		}
		
		return result;
	}
	
	private double averagePheromoneOfShortestTour(Edge edge, ArrayList shortestPath, double pathLength) {
		double result = 0.0;
		
		if (shortestPath.contains(edge)) {
			result += (Q/pathLength);
		}
		
		return result;
	}

	public void updatePheromoneTrails(Collection edges, double length) {
		// TODO Auto-generated method stub
		
	}

	public void updatePheromoneTrail(Ant ant, ArrayList ants) { // FIXME: Move the list of ants to a more generic location
		this.ants = ants;
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

	public void evaporate(Edge edge) {
		double currentPheromone = edge.getWeight();
		double tmp = (1-rho) * currentPheromone;
		edge.setWeight(tmp);
	}
}
