/*
 * GradientOptimisationAlgorithm.java
 *
 * Created on June 4, 2003, 4:55 PM
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

import net.sourceforge.cilib.Problem.OptimisationSolution;
import net.sourceforge.cilib.Problem.GradientOptimisationProblem;

/**
 *
 * @author  espeer
 */
public interface GradientOptimisationAlgorithm {
     /**
     * Sets the optimisation problem to be solved.
     *
     * @param problem The {@link net.sourceforge.cilib.Problem.OptimisationProblem} to be solved.
     */
    public void setGradientOptimisationProblem(GradientOptimisationProblem problem);
    
    /**
     * Accessor for the optimisation problem to be solved.
     *
     * @return The {@link net.sourceforge.cilib.Problem.OptimisationProblem} to be solved.
     */
    public GradientOptimisationProblem getGradientOptimisationProblem();
    

    public OptimisationSolution getBestSolution();
    
    public Collection getSolutions();    
    
    /**
     * Returns the number of times that the fitness function, specified by {@link net.sourceforge.cilib.Problem.OptimisationProblem#getFitness(double[])}, has been evaluated.
     *
     * @return The number of fitness evaluations.
     */
    public int getFitnessEvaluations();
    
    public int getGradientEvaluations();

}
