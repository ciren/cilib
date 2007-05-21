/*
 * ClusteringAntDecorator.java
 *
 * Created on Jun 30, 2004
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

import java.util.Collection;
import java.util.Random;

import net.sourceforge.cilib.aco.pheromone.PheromoneUpdate;
import net.sourceforge.cilib.container.graph.Edge;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.Type;


/**
 * @author Gary Pampara
 */
public class ClusteringAntDecorator extends Ant {
	private static final long serialVersionUID = -923470118462293377L;
	
	private ClusterableObject ladenObject;
	private boolean laden = false;
	private int currentX;
	private int currentY;
	
	/**
	 * 
	 *
	 */
	public ClusteringAntDecorator() {
		super();
		ladenObject = null;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public ClusteringAntDecorator(int x, int y) {
		super();
		currentX = x;
		currentY = y;
		ladenObject = null;
	}
	
	/**
	 * 
	 * @param object
	 * @param x
	 * @param y
	 */
	public ClusteringAntDecorator(ClusterableObject object, int x, int y) {
		super();
		ladenObject = object;
		currentX = x;
		currentY = y;
	}
	
	
	/**
	 * Copy construstor
	 * @param copy
	 */
	public ClusteringAntDecorator(ClusteringAntDecorator copy) {
		super();
		this.ladenObject = copy.ladenObject;
		this.laden = copy.laden;
		this.currentX = copy.currentX;
		this.currentY = copy.currentY;
	}
	
	/**
	 * The method is a simple clone() method to duplicate the current object
	 * @return An <code>Object</code> representing the cloned object
	 */
	public ClusteringAntDecorator clone() {
		return new ClusteringAntDecorator(this);
	}

	
	/**
	 * Sets the current item for the ClusteringAnt
	 * @param object The object that the ClusteringAnt will be carrying
	 */
	public void setCurrentObject(ClusterableObject object) {
		ladenObject = object;
	}
	
	/**
	 * Return the current <code>Object</object> the ClusteringAnt is carrying
	 * @return The current object that is being carried
	 */
	public Object getCurrentObject() {
		return ladenObject;
	}
	
	/**
	 * Returns a logical truth value for if the ClusteringAnt currently is laden with an object
	 * @return true - If the ClusteringAnt is currently carrying an object
	 * @return false - If the ClusteringAnt is not currently carrying an object
	 */
	public boolean isLaden() {
		return laden;
	}

	/**
	 * Set the current position on the surface for the ClusteringAnt
	 * @param x The x-coordinate on the grid
	 * @param y The y-coordinate on the grid
	 */
	public void setPosition(int x, int y) {
		currentX = x;
		currentY = y;		
	}
	
	/**
	 * Get the current row the ClusteringAnt is located on
	 * @return The current row value
	 */
	public int getCurrentRow() {
		return currentX;
	}
	
	/**
	 * Get the current column the ClusteringAnt is located on
	 * @return The current value for the column
	 */
	public int getCurrentCol() {
		return currentY;
	}

	/**
	 * Unload the ant from it's current object
	 */
	public void unloadCurrentObject() {
		ladenObject = null;
	}
	
	// FIXME: This should become a pluggable function!!!!!
	public double distanceTo(ClusterableObject object) {
		if (ladenObject.getType() == object.getType()) {
			return 0.0;
		}
		return 1.0;
	}
	

	
	//public void buildTour(DiscreteOptimisationProblem problem) {
	public void buildTour(OptimisationProblem problem) {
		// TODO Auto-generated method stub
		
	}

	public void calculateFitness(boolean count) {
		// TODO Auto-generated method stub
		
	}

	//public void updateBestSolution(DiscreteOptimisationProblem problem) {
	public void updateBestSolution(OptimisationProblem problem) {
		// TODO Auto-generated method stub
		
	}

	//public void updatePheromoneTrail(DiscreteOptimisationProblem problem) {
	public void updatePheromoneTrail(OptimisationProblem problem) {
		// TODO Auto-generated method stub
		
	}

	//public void initialise(DiscreteOptimisationProblem problem, Random randomizer) {
	public void initialise(OptimisationProblem problem, Random randomizer) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Return the current tour of the ant. This is not a valid method for a Clustering ant
	 */
	public Collection<Edge> getCurrentTour() {
		throw new RuntimeException("Cannot get a current Tour for a clustering ant");
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.ACO.Ant#getPheromoneUpdate()
	 */
	public PheromoneUpdate getPheromoneUpdate() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.ACO.Ant#getTransitionRuleFunction()
	 */
	public TransitionRuleFunction getTransitionRuleFunction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialise(OptimisationProblem problem) {
		// TODO Auto-generated method stub
		
	}

	public int compareTo(Entity o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Type get() {
		// TODO Auto-generated method stub
		return null;
	}

	public void set(Type type) {
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

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setId(String Id) {
		// TODO Auto-generated method stub
		
	}

	public void reinitialise() {
		// TODO Auto-generated method stub
		
	}
}
