/*
 * ASTSP.java
 * 
 * Created on Apr 28, 2004
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
import net.sourceforge.cilib.Container.Graph.Graph;

/**
 * An implementation of the ASTSP Ant Colony Optimisation algorithm
 * 
 * @author gpampara
 */ 
public class ASTSP extends ACO {
	// FIXME: Shouldn't the solutions be kept in the correct extended Solution class?
	private double alpha;
	private double beta;
	private double rho;
	private double Q;
	private double tau;
	private double e;
	
	private Graph graph;
	
	public ASTSP() {
		super();
	}
	
	protected void performInitialisation() {
		super.performInitialisation();
		
		alpha = 1;
		beta = 5;
		rho = 0.5;
		Q = 100;
		tau = 0.000001;
		e = 5;
		
		//setTransitionRuleFunction(new StandardTransitionRuleFunction(alpha, beta));
		//setPheromoneUpdate(new StandardPheromoneUpdate(ants, rho, e, Q));
		
		// Set the Edges to have an initial value of tau
		graph = ((GraphOptimisationProblem) super.getDiscreteOptimisationProblem()).getGraph();
		for (ListIterator l = graph.getEdges(); l.hasNext(); ) {
			Edge edge = (Edge) l.next();
			edge.setWeight(tau);
		}
	}
	
/*	public ASTSP() {
		super();
		//System.out.println("Creating....");
				
		// Initial initialisation
		shortestPath = new ArrayList();
		shortestPathLength = Double.MAX_VALUE; // Really large number!!!
		ants = new ArrayList();
		
		GraphCreationFactory fac = new GraphCreationFactory();
		//graph = fac.createStaticGraph();
		//graph = fac.createGraphFromFile("/home/gary/development/varsity/spe780-special-project-ACO/data-sets/tsp-data/uncompressed/pr107.tsp");
		//graph = fac.createGraphFromDataSet(dataSet);
		fac = null;
		
		antPlacementRandomizer = new MersenneTwister();
	}
		
	public void performInitialisation() {
		
		alpha = 1;
		beta = 5;
		rho = 0.5;
		Q = 100;
		tau = 0.000001;
		e = 5;
		
		setTransitionRuleFunction(new StandardTransitionRuleFunction(alpha, beta));
		setPheromoneUpdate(new StandardPheromoneUpdate(ants, rho, e, Q));
		
		initialiseEdges();
		placeAnts();
	}
	
	/**
	 * This is the main loop of the algorithm
	 *
	protected void performIteration() {
		// For each Ant generate the needed tour on the graph
		for (ListIterator i = ants.listIterator(); i.hasNext(); ) {
			TSPAnt ant = (TSPAnt) i.next();
			buildTour(ant);
			ant.calculateCurrentTourLength();
		}
		
		for (ListIterator i = ants.listIterator(); i.hasNext(); ) {
			TSPAnt ant = (TSPAnt) i.next();
			
			if (ant.getCurrentTourLength() < shortestPathLength) {
				// Have to clone the object, otherwise references are used and data is lost
				shortestPath = (ArrayList) (ant.getCurrentTour()).clone();
				shortestPathLength = ant.getCurrentTourLength();
			}
		}
		
		pheromoneUpdate.updatePheromoneTrails(shortestPath, shortestPathLength);
		
		// Make all the ants stupid again
		for (ListIterator i = ants.listIterator(); i.hasNext(); ) {
			TSPAnt ant = (TSPAnt) i.next();
			ant.emptyCurrentTour();
		}
	}
	
	/**
	 * This method initialises all the needed edges on the graph to a weight of the variable tau.
	 * This is the initial pheromone content of the edges
	 *
	private void initialiseEdges() {
		for (ListIterator i = graph.getEdges(); i.hasNext(); ) {
			Edge e = (Edge) i.next();
			e.setWeight(tau); // Set the initial weight of the edge to tau
		}
	}
	
	private void placeAnts() {
		for (int i = 0 ; i < numberOfAnts; i++) {
			TSPAnt a = new TSPAnt(new Integer(i));
			int randomValue = antPlacementRandomizer.nextInt(numberOfAnts);
			//System.out.println("randomValue: " + randomValue);
			a.setCurrentNode((Vertex) graph.getVertexAt(randomValue));
			ants.add(a);
		}
	}
	
	private void buildTour(TSPAnt a) {
		//System.out.println(a);
		Edge bestEdge = null;
		double probability = -1;
		Vertex vertex = a.getCurrentNode();
		
		a.setTabuList(graph);
		ArrayList antMemory = a.getTabuList();
		
		for (int i = 0; i < numberOfAnts-1; i++) {
			//System.out.println("Vertex eminating edge #: " + vertex.getEminatingEdgeCount());
			for (ListIterator edges = vertex.getEmanatingEdges(); edges.hasNext(); ) {
				//System.out.println("Current Vertex: " + vertex);
				Edge edge = (Edge) edges.next();
				//System.out.println(edge);
				double prob = function.getTransitionalProbability(a, vertex, edge.getOtherAssociatedVertex(vertex));
				
//		if (antMemory.contains(edge.getOtherAssociatedVertex(vertex))) {
				if (prob > probability) {
					probability = prob;
					bestEdge = edge;
				}
//				}
			}
			
			a.addToCurrentTour(bestEdge);
			a.removeFromTabuList(vertex);
			
			//System.out.println("BestEdge: " + bestEdge);
			// Now associcate the temporary vertex with the next vertex of the best edge
			vertex = bestEdge.getOtherAssociatedVertex(vertex);
			if (vertex == null) System.out.println("ERROR");
			//System.out.println("Moved to next vertex: " + vertex);
			probability = -1;
			bestEdge = null;
		}
	}
		
	public Collection getSolution() {
		return shortestPath;
	}
	
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	
	public double getAlpha() {
		return alpha;
	}
	
	public void setBeta(double beta) {
		this.beta = beta;
	}
	
	public double getBeta() {
		return beta;
	}
	
	/**
	 * Set the initial amount of pheromone to be used in the algorithm
	 * @param initialPheramone The amount of initial pheromone to be placed on the edges
	 *
	public void setInitialPheramone(double initialPheramone) {
		this.tau = initialPheramone;
	}
	
	/**
	 * Get the initial amount of initial pheromone used in the algorithm
	 * @return The initial amount of pheromone
	 *
	public double getInitialPheramone() {
		return tau;
	}
	
	public void setQ(double Q) {
		this.Q = Q;
	}
	
	public double getQ() {
		return Q;
	}
	
	public void setE(double e) {
		this.e = e;
	}
	
	public double getE() {
		return e;
	}
	
	/**
	 * Set the randomizer to be used in the algorithm
	 * @param randomizer The randomizer to be set
	 *
	public void setAntPlacementRandomizer(Random randomizer) {
		this.antPlacementRandomizer = randomizer;
	}
	
	/**
	 * Get the currently set randomizer for the algorithm
	 * @return The currently set <code>Random</code>-izing object
	 *
	public Random getAntPlacementRandomizer() {
		return antPlacementRandomizer;
	}
	
	/**
	 * Get the length of the best solution
	 * @return An <code>Object</code> representing the length of the solution
	 *
	public Object getSolutionLength() {
		return new Double(shortestPathLength);
	}
	
	public void setGraphOptimisationProblem(Problem problem) {
		this.problem = (GraphOptimisationProblem) problem;
	}
	
	public GraphOptimisationProblem getGraphOptimisationProblem() {
		return (GraphOptimisationProblem) problem;
	}
	
	//?????????????????????
	public void setProblem(Problem problem){
		this.problem = (GraphOptimisationProblem) problem;
	}
	//?????????????????????

	public void setGraphOptimisationProblem(GraphOptimisationProblem problem) {
		this.problem = problem;
	}
	*/
}
