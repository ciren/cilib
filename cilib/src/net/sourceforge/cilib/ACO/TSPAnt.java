/*
 * TSPAntDecorator.java
 *
 * Created on Jul 25, 2004
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;
import java.util.Random;

import net.sourceforge.cilib.ACO.Pheromone.PheromoneUpdate;
import net.sourceforge.cilib.Algorithm.Algorithm;
import net.sourceforge.cilib.Container.Graph.Edge;
import net.sourceforge.cilib.Container.Graph.Graph;
import net.sourceforge.cilib.Container.Graph.Vertex;

/**
 * @author gpampara
 */
public class TSPAnt implements Ant {
	private Vertex currentVertex = null;
	private ArrayList<Vertex> tabuList = null;
	private ArrayList<Edge> currentTour = null;
	private double currentTourLength = Double.MAX_VALUE;
	
	public TSPAnt() {
		tabuList = new ArrayList<Vertex>();
		currentTour = new ArrayList<Edge>();
		
		tabuList.clear();
		currentTour.clear();
	}
	
	public TSPAnt(Vertex currentNode) {
		this.currentVertex = currentNode;
		tabuList = new ArrayList<Vertex>();
		currentTour = new ArrayList<Edge>();
		
		tabuList.clear();
		currentTour.clear();
	}
	
	public void setCurrentNode(Vertex v) {
		currentVertex = v;
	}
	
	public Vertex getCurrentNode() {
		return currentVertex;
	}
	
	/**
	 * 
	 * @param graph
	 */
	public void setTabuList(Graph graph) {
		for (ListIterator<Vertex> l = graph.getVertices(); l.hasNext(); )
			tabuList.add(l.next());
		
		tabuList.remove(currentVertex);
	}
	
	/**
	 * Get the current Tabu list associated with the <code>TSPAnt</code> 
	 * @return The <code>ArrayList</code> representing the current Tabu list
	 */
	public ArrayList getTabuList() {
		return tabuList;
	}
	
	public void removeFromTabuList(Vertex v) {
		if (tabuList.contains(v)) {
			tabuList.remove(v);
		}
	}
	
	/**
	 * Return the current tour associated with this ant
	 * @return A <code>Collection</code> representing the current tour of this ant
	 */
	public Collection getCurrentTour() {
		return currentTour;
	}
	
	/**
	 * Get the current length of the tour associated with this ant
	 * @return The length of the tour associated with this ant
	 */	
	public double getCurrentTourLength() {
		if (currentTourLength > -1.0)
		  return currentTourLength;
		else throw new RuntimeException("Error - the currrentTourLengthmust be calculated first!");
	}
	
	public void addToCurrentTour(Edge e) {
		currentTour.add(e);
	}
	
	public void emptyCurrentTour() {
		tabuList.clear();
		currentTour.clear();
		
		tabuList = new ArrayList<Vertex>();
		currentTour = new ArrayList<Edge>();
		
		currentTourLength = Double.MAX_VALUE;
	}
	
	public void calculateCurrentTourLength() {
		currentTourLength = 0.0;
		for (ListIterator<Edge> l = currentTour.listIterator(); l.hasNext(); ) {
			Edge edge = l.next();
			currentTourLength += edge.getCost();
		}
	}

	public String toString() {
		return "TSPAnt: currentVertex: " + currentVertex;
	}
	
	/**
	 * This method overrides the basic method defined in the Object class to create a clone of the current ant
	 * @return A cloned <code>TSPAnt</code> object 
	 */
	public Object clone() throws CloneNotSupportedException {
		TSPAnt clone = (TSPAnt) super.clone();
		return clone;
	}
	
	public void buildTour(DiscreteOptimisationProblem problem) {
		TSPProblem prob = (TSPProblem) problem;
		Graph graph = prob.getGraph();
		
		Edge bestEdge = null;
		double probability = -1;
		Vertex vertex = getCurrentNode();
		if (vertex.getDegree() == 0)
			throw new RuntimeException("Cannot work with graphs that have isolated verticies, please connect all verticies with edges");

		setTabuList(graph);
		ArrayList antMemory = getTabuList();
		
		for (int i = 0; i < graph.getVertexCount()-1; i++) {
			//System.out.println("Vertex eminating edge #: " + vertex.getEminatingEdgeCount());
			for (ListIterator<Edge> edges = vertex.getEmanatingEdges(); edges.hasNext(); ) {
				//System.out.println("Current Vertex: " + vertex);
				Edge edge = edges.next();
				//System.out.println(edge);
				double localProbability = prob.getFunction().getTransitionalProbability(this, vertex, edge.getOtherAssociatedVertex(vertex));
				
				//if (antMemory.contains(edge.getOtherAssociatedVertex(vertex))) {
					if (localProbability > probability) {
						probability = localProbability;
						bestEdge = edge;
					}
				//}
			}
			
			addToCurrentTour(bestEdge);
			removeFromTabuList(vertex);
			
			vertex = bestEdge.getOtherAssociatedVertex(vertex);
			if (vertex == null) System.out.println("ERROR");
			probability = -1;
			bestEdge = null;
		}
	}
	
	public void calculateFitness() {
		calculateCurrentTourLength();
	}

	/**
	 * Changes the /home/gary/workspacecurrent best solution, if needed, and sets the solution within the problem 
	 * @param problem The problem for the ant to update the best solution on
	 */
	public void updateBestSolution(DiscreteOptimisationProblem problem) {
		TSPProblem tspProblem = (TSPProblem) problem;
		
		// FIXME: Possible bug when the best solution is too large... Why should this happen? Only in KnightsTourOptimisationProblem???
		//if (tspProblem.getBestSolutionLength() > tspProblem.getGraph().getVertexCount())
		//{
		//	throw new RuntimeException("Error in the size of the solution: " + tspProblem.bestSolutionLength + " > " + tspProblem.getGraph().getVertexCount());
		//}
		if (tspProblem.getBestSolutionLength() > currentTourLength) {
//			System.out.println("currentTourLength: " + currentTourLength);
			
			ArrayList newClone = (ArrayList) currentTour.clone();
//			System.out.println("Size of update: " + newClone.size());
			
			tspProblem.setBestSolution(newClone);
			tspProblem.setBestSolutionLength(currentTourLength);
		}
		
		//solution.update(this, problem);
	}

	/**
	 * Update the pheromone trail for this <code>Ant</code>
	 * @param problem The <code>DiscreteOptimistaionProblem</code> to update the pheromone levels on
	 */
	public void updatePheromoneTrail(DiscreteOptimisationProblem problem) {
		TSPProblem tspProblem = (TSPProblem) problem;
		PheromoneUpdate update = tspProblem.getPheromoneUpdate();
		update.updatePheromoneTrail(this, (ArrayList) ((ACO) Algorithm.get()).getAntCollection());
		
		emptyCurrentTour();	// Ensure that the ants forget all learned information
	}

	/**
	 * Set the current Ant to a random Node within the data structure defined in <code>problem</code> and using the
	 * random generator <code>randomizer</code>
	 * 
	 * @param problem The <code>GraphOptimistaionProblem</code> the TSPAnt has referance to
	 * @param randomizer The random number generator to be used
	 */
	public void initialise(DiscreteOptimisationProblem problem, Random randomizer) {
		GraphOptimisationProblem prob = (GraphOptimisationProblem) problem;

		int numberOfNodes = prob.getGraph().getVertexCount();
		if (numberOfNodes <= 0) throw new RuntimeException("Cannot work with numberOfNodes <= 0");

		currentVertex = prob.getGraph().getVertexAt(randomizer.nextInt(numberOfNodes));
		
		currentTourLength = Double.MAX_VALUE;
	}
}
