/*
 * Ant.java
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

import java.util.Collection;
import java.util.Random;


/**
 * @author gpampara
 */
public interface Ant extends Cloneable {
	/**
	 * Return a clone of the current Ant object instance
	 * @return An <code>Object</code> representing a clone of the current <code>Ant</code> object
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException;

	/**
	 * Build a valid tour based on the current <code>DiscreteOptimisationProblem</code> 
	 * @param problem The problem to construct a tour on
	 */
	public void buildTour(DiscreteOptimisationProblem problem);

	/**
	 * 
	 */
	public void calculateFitness();

	/**
	 * @param problem
	 */
	public void updateBestSolution(DiscreteOptimisationProblem problem);

	/**
	 * @param problem
	 */
	public void updatePheromoneTrail(DiscreteOptimisationProblem problem);

	/**
	 * @param problem
	 * @param randomizer
	 */
	public void initialise(DiscreteOptimisationProblem problem, Random randomizer);
	
	/**
	 * 
	 * @return
	 */
	public Collection getCurrentTour();
}
