/*
 * Copyright (C) 2003 - 2008
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
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.OptimisationProblem;

/**
 * <code>Ant</code> implementation specifically designed for TSP like action.
 * 
 * @author Gary Pampara
 */
public class TSPAnt extends Ant {
	private static final long serialVersionUID = 3363231185310380644L;
	/*
	protected TransitionRuleFunction transitionRuleFunction;
	protected PheromoneUpdate pheromoneUpdate;
	
	private Vertex currentVertex = null;
	private ArrayList<Vertex> tabuList = null;
	private ArrayList<Edge> currentTour = null;
	private double currentTourLength = Double.MAX_VALUE;
	
	/**
	 * Create a <code>TSPAnt</code> with the default values
	 *
	public TSPAnt() {
		this.transitionRuleFunction = new StandardTransitionRuleFunction();
		this.pheromoneUpdate = new StandardPheromoneUpdate();
		
		tabuList = new ArrayList<Vertex>();
		currentTour = new ArrayList<Edge>();
		
		tabuList.clear();
		currentTour.clear();
	}
	
	
	/**
	 * Copy-constructor.
	 * @param copy
	 *
	public TSPAnt(TSPAnt copy) {
		this.transitionRuleFunction = copy.transitionRuleFunction.getClone();
		this.pheromoneUpdate = copy.pheromoneUpdate.getClone();
		
		this.currentTour = new ArrayList<Edge>();
		this.tabuList = new ArrayList<Vertex>();
		
		this.currentTour.clear();
		this.tabuList.clear();
	}
	
	/**
	 * This method overrides the basic method defined in the Object class to create a clone of the current ant
	 * @return A cloned <code>TSPAnt</code> object
	 * @author Patch by Francois Geldenhuys - fixed the issue with the clone referencing the same container
	 *
	public Ant getClone() {
		return new TSPAnt(this);
	}
	
	/**
	 * Create a <code>TSPAnt</code> with the required initialised varaible and specify that
	 * the <code>Ant</code> should be located at a specific <code>Vertex</code>
	 * @param currentNode The <code>Vertex</code> where the <code>Ant</code> is to be located
	 *
	public TSPAnt(Vertex currentNode) {
		this(); // Call the default constructor		
		currentVertex = currentNode;
	}
	
	/**
	 * Set the current <code>Vertex</code> to a specific value
	 * @param v The <code>Vertex</code> to be used
	 *
	public void setCurrentNode(Vertex v) {
		currentVertex = v;
	}
	
	/**
	 * Return the current <code>Vertex</code>
	 * @return The current <code>Vertex</code> the <code>Ant</code> is occupying
	 *
	public Vertex getCurrentNode() {
		return currentVertex;
	}
	
	/**
	 * Create the Tabu List. This list contains all the <code>Vertex</code>(s) that still
	 * need to be visited by the <code>Ant</code> except for the current <code>Vertex</code>
	 * @param graph The <code>Graph</code> on which the traversal will be made
	 *
	public void setTabuList(Graph<Vertex, Edge> graph) {
		for (ListIterator<Vertex> l = graph.getVertices(); l.hasNext(); )
			tabuList.add(l.next());
		
		tabuList.remove(currentVertex);
	}
	
	/**
	 * Get the current Tabu list associated with the <code>TSPAnt</code> 
	 * @return The <code>ArrayList</code> representing the current Tabu list
	 *
	public ArrayList<Vertex> getTabuList() {
		return tabuList;
	}
	
	/**
	 * Remove the specified <code>Vertex</code> from the TabuList. This implies that
	 * the <code>Vertex</code> has already been visited.
	 * @param v The <code>Vertex</code> to remove from the tabu list
	 *
	public void removeFromTabuList(Vertex v) {
		if (tabuList.contains(v)) {
			tabuList.remove(v);
		}
	}
	
	/**
	 * Return the current tour associated with this ant
	 * @return A <code>Collection</code> representing the current tour of this ant
	 *
	public Collection<Edge> getCurrentTour() {
		return currentTour;
	}
	
	/**
	 * Get the current length of the tour associated with this ant
	 * @return The length of the tour associated with this ant
	 *	
	public double getCurrentTourLength() {
		if (currentTourLength > -1.0)
		  return currentTourLength;
		else throw new RuntimeException("Error - the currrentTourLengthmust be calculated first!");
	}
	
	/**
	 * Add the specified <code>Edge</code> to the current tour the <code>Ant</code> is building
	 * @param e The <code>Edge</code> to be added
	 *
	public void addToCurrentTour(Edge e) {
		currentTour.add(e);
	}
	
	/**
	 * Empty the current tour as well as remove all memory that the <code>Ant</code> has.
	 *
	 *
	public void emptyCurrentTour() {
		tabuList.clear();
		currentTour.clear();
		
		tabuList = new ArrayList<Vertex>();
		currentTour = new ArrayList<Edge>();
		
		currentTourLength = Double.MAX_VALUE;
	}
	
	/**
	 * Calculate the current tour length. This is the length of the tour that the <code>Ant</code>
	 * has built up.
	 *
	public void calculateCurrentTourLength() {
		this.currentTourLength = 0.0;
		for (ListIterator<Edge> l = currentTour.listIterator(); l.hasNext(); ) {
			Edge edge = l.next();
			this.currentTourLength += edge.getCost();
		}
	}

	public String toString() {
		return "TSPAnt located on vertex: " + currentVertex;
	}
	
	
	
	/**
	 * Build the tour of the <code>Ant</code> based on the manner in which the problem is defined
	 * @param problem The <code>OptimisationProblem</code> to perform the path walking on
	 *
	public void buildTour(OptimisationProblem problem) {
		TSPProblem prob = (TSPProblem) problem;
		Graph<Vertex, Edge> graph = prob.getGraph();
		
		Edge bestEdge = null;
		double probability = -1;
		Vertex vertex = getCurrentNode();
		if (vertex.getDegree() == 0)
			throw new RuntimeException("Cannot work with graphs that have isolated verticies, please connect all verticies with edges");

		setTabuList(graph);
		//ArrayList<Vertex> antMemory = getTabuList();
		
		for (int i = 0; i < graph.getVertexCount()-1; i++) {
			for (ListIterator<Edge> edges = vertex.getEmanatingEdges(); edges.hasNext(); ) {
				Edge edge = edges.next();
			
				//double localProbability = prob.getFunction().getTransitionalProbability(this, vertex, edge.getOtherAssociatedVertex(vertex));
				double localProbability = this.getTransitionRuleFunction().getTransitionalProbability(this, vertex, edge.getOtherAssociatedVertex(vertex));
				
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
	
	/**
	 * Caluclate the current fitness of the <code>Ant</code>. The current fitness is the same as
	 * the length of the tour generated.
	 *
	public void calculateFitness(boolean count) {
		calculateCurrentTourLength();
	}

	/**
	 * Changes the /home/gary/workspacecurrent best solution, if needed, and sets the solution within the problem 
	 * @param problem The problem for the ant to update the best solution on
	 
	public void updateBestSolution(OptimisationProblem problem) {
		TSPProblem tspProblem = (TSPProblem) problem;
		
		if (currentTourLength < tspProblem.getBestSolutionLength()) {
			tspProblem.setBestSolution(currentTour);
			tspProblem.setBestSolutionLength(currentTourLength);
		}
	}

	/**
	 * Update the pheromone trail for this <code>Ant</code>
	 * @param problem The <code>DiscreteOptimistaionProblem</code> to update the pheromone levels on
	 *
	public void updatePheromoneTrail(OptimisationProblem problem) {
		pheromoneUpdate.updatePheromoneTrail(this);
		emptyCurrentTour();	// Ensure that the ants forget all learned information
	}

	/**
	 * Set the current Ant to a random Node within the data structure defined in <code>problem</code> and using the
	 * random generator <code>randomizer</code>
	 * 
	 * @param problem The <code>GraphOptimistaionProblem</code> the TSPAnt has referance to
	 * @param randomizer The random number generator to be used
	 *
	public void initialise(OptimisationProblem problem) {
		Random randomizer = new MersenneTwister();
		GraphOptimisationProblem prob = (GraphOptimisationProblem) problem;

		int numberOfNodes = prob.getGraph().vertices();
		if (numberOfNodes <= 0) throw new RuntimeException("Cannot work with numberOfNodes <= 0");

		currentVertex = prob.getGraph().getVertex(randomizer.nextInt(numberOfNodes));
		currentTourLength = Double.MAX_VALUE;
	}
	
	
	/**
	 * Get the associated <code>TransitionRuleFunction</code> for the problem
	 * @return The associated <code>TransitionRuleFunction</code> object
	 *
	public TransitionRuleFunction getTransitionRuleFunction() {
		return transitionRuleFunction;
	}
	
	/**
	 * Set the <code>TransitionRuleFunction</code> to the specified object
	 * @param function The desired <code>TransitionRuleFunction</code> to be used
	 *
	public void setTransitionRuleFunction(TransitionRuleFunction function) {
		this.transitionRuleFunction = function;
	}
	
	/**
	 * Get the associated <code>PheromoneUpdate</code> for the problem
	 * @return The associated <code>PheromoneUpdate</code> object
	 *
	public PheromoneUpdate getPheromoneUpdate() {
		return pheromoneUpdate;
	}
	
	/**
	 * Set the <code>PheromoneUpdate</code> the the specified object
	 * @param pheromoneUpdate The desired <code>PheromoneUpdate</code> to be used
	 *
	public void setPheromoneUpdate(PheromoneUpdate pheromoneUpdate) {
		this.pheromoneUpdate = pheromoneUpdate;
	}

	

	
	public int compareTo(Entity o) {
		// TODO Auto-generated method stub
		return 0;
	}


	public Type getContents() {
		// TODO Auto-generated method stub
		return null;
	}


	public void setContents(Type type) {
		// TODO Auto-generated method stub
		
	}


	public void setFitness(Fitness fitness) {
		// TODO Auto-generated method stub
		
	}


	public Fitness getFitness() {
		// TODO Auto-generated method stub
		return null;
	}


	public int getDimension() {
		// TODO Auto-generated method stub
		return 0;
	}


	public void setDimension(int dim) {
		// TODO Auto-generated method stub
		
	}


	public Type getBehaviouralParameters() {
		// TODO Auto-generated method stub
		return null;
	}


	public void setBehaviouralParameters(Type behaviouralParameters) {
		// TODO Auto-generated method stub
		
	}


	public void reinitialise() {
		// TODO Auto-generated method stub
		
	}
	
	*/

	@Override
	public void buildTour(OptimisationProblem problem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void calculateFitness(boolean count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Ant getClone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

//	@Override
//	public Collection<Edge> getCurrentTour() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public boolean equals(Object object) {
		throw new UnsupportedOperationException("Method is not implemented");
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("Method is not implemented");
	}

	@Override
	public PheromoneUpdate getPheromoneUpdate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransitionRuleFunction getTransitionRuleFunction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialise(OptimisationProblem problem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateBestSolution(OptimisationProblem problem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePheromoneTrail(OptimisationProblem problem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int compareTo(Entity o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDimension() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void reinitialise() {
		// TODO Auto-generated method stub
		
	}
}
