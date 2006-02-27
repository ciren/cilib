/*
 * OptimisationAlgorithm.java
 *
 * Created on January 18, 2003, 3:48 PM
 *
 * 
 * Copyright (C) 2003 - Edwin S. Peer
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


import net.sourceforge.cilib.Problem.*;

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
     * @return A double array of length {@link net.sourceforge.cilib.Problem.OptimisationProblem#getDimension()} that represents the best solution found.
     */
    public double[] getSolution();
    
    /**
     * Returns the fitness of the best solution found by the algorithm.
     *
     * @return The best fitness.
     */
    public double getSolutionFitness();
    
    /**
     * Returns the number of times that the fitness function, specified by {@link net.sourceforge.cilib.Problem.OptimisationProblem#getFitness(double[])}, has been evaluated.
     *
     * @return The number of fitness evaluations.
     */
    public int getFitnessEvaluations();
}
