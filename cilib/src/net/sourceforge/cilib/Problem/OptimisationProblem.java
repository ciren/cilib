/*
 * OptimisationProblem.java
 *
 * Created on June 9, 2004
 *
 * 
 * Copyright (C) 2004 - CIRG@UP 
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

package net.sourceforge.cilib.Problem;

import net.sourceforge.cilib.Domain.Component;

/**
 * Optimisation problems are characterised by a domain that specifies the search space and
 * a fitness given a potential solution. This interface ensures that an 
 * {@link net.sourceforge.cilib.Algorithm.OptimisationAlgorithm} has 
 * all the information it needs to find a solution to a given optimisation problem. In addition, it is the
 * responsibility of an optimisation problem to keep track of the number of times the fitness has
 * been evaluated.
 * <p />
 * All optimisation problems must implement this interface.
 *
 * @author  espeer
 */
public interface OptimisationProblem extends Problem { 
    
    /**
     * Returns the fitness of a potential solution to this problem. The solution object is described 
     * by the domain of this problem, see {@link #getDomain()}. An instance of {@link InferiorFitness} 
     * should be returned if the solution falls outside the search space of this problem.
     * 
     * 
     * @param solution The potential solution found by the optimisation algorithm.
     * @param count True if this call should contribute to the fitness evaluation count, see {@link #getFitnessEvaluations()}.
     * @return The fitness of the solution. 
     */
    public Fitness getFitness(Object solution, boolean count);
    
    /**
     * Returns the number of times the underlying fitness function has been evaluated. 
     * 
     * @return The number fitness evaluations.
     */
    public int getFitnessEvaluations();
    
    /**
     * Returns the domain component that describes the search space for this problem.
     * 
     * @return A {@link net.sourceforge.cilib.Domain.Component} object representing the search space.
     */
    public Component getDomain();
    
}
