/*
 * OptimisationAlgorithm.java
 *
 * Created on January 18, 2003, 3:48 PM
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

package net.sourceforge.cilib.Algorithm;

import java.util.Collection;

import net.sourceforge.cilib.Problem.OptimisationProblem;
import net.sourceforge.cilib.Problem.OptimisationSolution;

/**
 * Any algorithm that implements this interface can be used to solve an {@link net.sourceforge.cilib.Problem.OptimisationProblem}.
 *
 * @author  espeer
 */
public interface OptimisationAlgorithm {
    /**
     * Sets the optimisation problem to be solved.
     *
     * @param problem The {@link net.sourceforge.cilib.Problem.OptimisationProblem} to be solved.
     */
    public void setOptimisationProblem(OptimisationProblem problem);
    
    /**
     * Accessor for the optimisation problem to be solved.
     *
     * @return The {@link net.sourceforge.cilib.Problem.OptimisationProblem} to be solved.
     */
    public OptimisationProblem getOptimisationProblem();
    
    /**
     * Returns the best solution found by the algorithm so far.
     *
     * @return the best solution found.
     */
    public OptimisationSolution getBestSolution();
    
    /**
     * Returns a collection of solutions for multi-objective optimisation algorithms.
     * 
     * @return a collection of solutions.
     */
    public Collection getSolutions();
    
}
