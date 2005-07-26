/*
 * LumerFaitaClusteringAlgorithm.java
 *
 * Created on Jun 24, 2004
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

import net.sourceforge.cilib.Container.Matrix;
import net.sourceforge.cilib.Problem.Problem;
import net.sourceforge.cilib.ACO.ACO;

/**
 * @author gpampara
 */
public class LumerFaietaClusteringAlgorithm extends ACO {
	private double k1 = 0.1;
	private double k2 = 0.15;
	private double alpha = 0.5;
	private double squaredNeighbourhood = 9;
	private ClusteringOptimisationProblem clusteringOptimisationProblem;
	
	
	public LumerFaietaClusteringAlgorithm() {
		super();
		super.setPrototypeAnt(new ClusteringAntDecorator());
	}
	
	
	public void performInitialisation() { // Override this method to enable ACO based clustering algorithms
		if (clusteringOptimisationProblem == null) 
			throw new RuntimeException("Cannont perform algorithm on a non-existant problem");
		
		for (int i = 0; i < numberAnts; ++i) {
			Ant ant = null;
			try {
				ant = (Ant) super.getPrototypeAnt().clone();
			}
			catch (CloneNotSupportedException c) {
				throw new RuntimeException(c);
			}
			
			ant.initialise(clusteringOptimisationProblem, getRandomizer()); // Initialise the ants based on the problem
			ants.add(ant); // Add the ant to the list of ants
		}
	}
	
	// TODO: Move more of this code into the ClusteringAntDecorator
	public void performIteration() { // Override this method to perform the needed clustering algorithm
		Matrix<Object> grid = clusteringOptimisationProblem.getGrid();
		double randomNumber = -1.0;
		
		for (ListIterator l = ants.listIterator(); l.hasNext(); ) {
			ClusteringAntDecorator ant = (ClusteringAntDecorator) l.next();
			
			if (!ant.isLaden() && (grid.getItemAt(ant.getCurrentRow(), ant.getCurrentCol()) != null)) {
				// compute f(o_i) and p_p(o_i)
				// double f = 1.0;
				double pPickup = probabilityPickup(grid.getItemAt(ant.getCurrentRow(), ant.getCurrentCol()), ant);
								
				// Draw random number between 0 and 1
				randomNumber = getRandomizer().nextDouble();
				
				if (randomNumber <= pPickup) {
					// pick up the item o_i
					ClusterableObject object = (ClusterableObject) grid.getItemAt(ant.getCurrentRow(), ant.getCurrentCol());
					grid.setItemAt(ant.getCurrentRow(), ant.getCurrentCol(), null);
					ant.setCurrentObject(object);
				}
			}
			else if (ant.isLaden() && (grid.getItemAt(ant.getCurrentRow(), ant.getCurrentCol()) != null)) {
				// compute f(o_i) and p_p(o_i)
				// double f = 1.0;
				double pDrop = probabilityDrop(grid.getItemAt(ant.getCurrentRow(), ant.getCurrentCol()), ant);
				randomNumber = getRandomizer().nextDouble();
				
				if (randomNumber <= pDrop) { // drop the item
					grid.setItemAt(ant.getCurrentRow(), ant.getCurrentCol(), ant.getCurrentObject());
					ant.unloadCurrentObject();
				}
			}
					
			// FIXME: This needs to be optimised!!!
			// Move current agent to a randomly selected neighbouring site not containing another agent
			
			@SuppressWarnings("unused") int rootNumber = Double.valueOf(Math.sqrt(squaredNeighbourhood)).intValue();
			boolean moved = false;
			
			// TODO: What if the grid size is larger than 3 x 3? Should never be though
			int movementX [] = { -1,  0,  1, 1, 1, 0, -1, -1};
			int movementY [] = { -1, -1, -1, 0, 1, 1,  1,  0}; 
			
			while (!moved) {
				//FIXME: FIXME: System.out.println("FIXME FIXME FIXME");
				int randomBlock = getRandomizer().nextInt(8);
				int newX = ant.getCurrentCol() + movementX[randomBlock];
				int newY = ant.getCurrentRow() + movementY[randomBlock];
				
				moved = true;
				
				// Now see if any ants are in that block
				for (ListIterator<Ant> cl = ants.listIterator(); cl.hasNext(); ) {
					ClusteringAntDecorator testingAnt = (ClusteringAntDecorator) cl.next();
					
					if ( (testingAnt.getCurrentCol() == newX) && (testingAnt.getCurrentRow() == newY) ) {
						moved = false;
						break;
					}
				}
			}
		}
		
		// Print the location of the items
	}
	
	private double neighbourhoodFrequency(Object o, ClusteringAntDecorator ant) {
		double tmp = 0;
		
		tmp = (1/squaredNeighbourhood)*(neighbourhoodSum(ant));
		
		if (tmp > 0)
			return tmp;
		else return tmp;		
	}
	
	//FIXME: What happens if the squareNeighbourhood is more than 1?
	private double neighbourhoodSum(ClusteringAntDecorator ant) {
		Matrix grid = clusteringOptimisationProblem.getGrid();
		double localGridSize = Math.sqrt(squaredNeighbourhood);
		
		int currentX = ant.getCurrentRow();
		int currentY = ant.getCurrentCol();
		
		double sum = 0.0;
		
		for (int i = currentX-1; i < currentX+2*localGridSize; i++) {
			for (int j = currentY-1; j < currentY+2*localGridSize; j++) {
				sum += 1.0 - ( ant.distanceTo((ClusterableObject) grid.getItemAt(currentX, currentY)) / alpha);
			}
		}
		
		return sum;
	}
	
	private double probabilityPickup(Object object, ClusteringAntDecorator ant) {
		double tmp = k1 / (k1+neighbourhoodFrequency(object, ant));
		return (tmp*tmp);
	}
	
	private double probabilityDrop(Object object, ClusteringAntDecorator ant) {
		double f = neighbourhoodFrequency(object, ant);
		double result = 0.0;
		
		if (f < k2) {
			result = 2*f;
		}
		else if (f >= (k2*Math.sqrt(squaredNeighbourhood))) {
			result = 1.0;
		}
		
		return result;
	}
	
	public double getK1() {
		return k1;
	}
	
	public void setK1(double newValue) {
		k1 = newValue;
	}
	
	public double getK2() {
		return k2;
	}
	
	public void setK2(double newValue) {
		k2 = newValue;
	}
	
	public double getAlpha() {
		return alpha;
	}
	
	public void setAlpha(double newValue) {
		alpha = newValue;
	}
	
	public void setClusteringOptimisationProblem(ClusteringOptimisationProblem problem) {
		this.clusteringOptimisationProblem = problem;
	}
	
	public void setProblem(Problem p) {
		this.clusteringOptimisationProblem = (ClusteringOptimisationProblem) p;
	}
}