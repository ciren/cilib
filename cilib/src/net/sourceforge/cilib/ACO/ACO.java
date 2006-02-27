/*
 * ACO.java
 *
 * Created on March 18, 2004, 4:23 PM
 *
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
 *
 */

package net.sourceforge.cilib.ACO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;
import java.util.Random;

import net.sourceforge.cilib.Algorithm.Algorithm;
import net.sourceforge.cilib.Problem.Problem;
import net.sourceforge.cilib.Random.MersenneTwister;

/**
 * @author gpampara
 */
// TODO: Get the needed data structure into memory for the algorithm to run against
public class ACO extends Algorithm /*implements GraphOptimisationAlgorithm*/  {
	protected int numberAnts;
	
	private DiscreteOptimisationProblem problem;
	private Random randomizer;
	
	private Ant prototypeAnt;
	protected ArrayList<Ant> ants;

	public ACO() {
		ants = new ArrayList<Ant>();
		numberAnts = 20;
		
		prototypeAnt = new TSPAnt();		
		randomizer = new MersenneTwister();
	}
	
	protected void performInitialisation() {
		if (problem == null) 
			throw new RuntimeException("Cannont perform algorithm on a non-existant problem");
		
		for (int i = 0; i < numberAnts; ++i) {
			Ant ant = null;
			try {
				ant = (Ant) prototypeAnt.clone();
			}
			catch (CloneNotSupportedException c) {
				throw new RuntimeException(c);
			}
			
			ant.initialise(problem, randomizer); // Initialise the ants based on the problem
			ants.add(ant); // Add the ant to the list of ants
		}
	}
	
	protected void performIteration() {
		for (ListIterator<Ant> l = ants.listIterator(); l.hasNext(); ) {
			Ant a = l.next();
			a.buildTour(problem);
			a.calculateFitness(); // Get the cost of the tour the ant's "Fitness"
		}
		
		for (ListIterator<Ant> l = ants.listIterator(); l.hasNext(); ) {
			Ant a = l.next();
			a.updateBestSolution(problem);
		}

		// Degrade the pheromones
		problem.degrade();
		
		for (ListIterator<Ant> l = ants.listIterator(); l.hasNext(); ) {
			Ant a = l.next();
			a.updatePheromoneTrail(problem);
		}
	}
	
	/**
	 * Get the number of ants being used in the algorithm 
	 * @return The number of ants being used
	 */
	public int getNumberAnts() {
		return numberAnts;
	}
	
	/**
	 * Accessor method that sets the number of ants in the algorithm
	 * @param number The nunber of ants to use
	 */
	public void setNumberAnts(int number) {
		numberAnts = number;
	}
	
	/**
	 * Get a container containing all the ant instances
	 * @return A collection containing all the ant object instances
	 */
	public Collection getAntCollection() {
		return ants;
	}
	
	/**
	 * Get the type of ant used within this algorithm
	 * @return The object representing the type of ant
	 */
	public Ant getPrototypeAnt() {
		return prototypeAnt;
	}
	
	/**
	 * Set the type of ant to use in this algorithm
	 * @param prototypeAnt The object instance to use in the algorithm
	 */
	public void setPrototypeAnt(Ant prototypeAnt) {
		this.prototypeAnt = prototypeAnt;
	}
	
	/**
	 * Set the randomizer for this algorithm
	 * @param randomizer The randomizer object to use
	 */
	public void setRandomizer(Random randomizer) {
		this.randomizer = randomizer;
	}
	
	/**
	 * Get the associated randomizer object for this algorithm
	 * @return The associated randomizer for this algorithm
	 */
	public Random getRandomizer() {
		return randomizer;
	}
	
	/**
	 * Return a reference to the DiscreteOptimisiationProblem associated with this algorithm
	 * @return Reference to the DiscreteOptimisationProblem, <code>NULL</code> if none is associated
	 */
	public DiscreteOptimisationProblem getDiscreteOptimisationProblem() {
		return problem;
	}
	
	/**
	 * Set the DiscreteOptimisationProblem needed for this algorithm
	 * @param problem The DiscreteOptimisiationProblem to use
	 */
	public void setDiscreteOptimisationProblem(DiscreteOptimisationProblem problem) {
		this.problem = problem;
	}
	
	// TODO: Why does this method only work? setDiscreteOptimistationProblem should work
	public void setProblem(Problem problem) {
		this.problem = (DiscreteOptimisationProblem) problem;
	}
}
