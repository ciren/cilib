/*
 * RNAOptimisationSolution,java
 * 
 * Created on 2005/05/25
 *
 * Copyright (C) 2003, 2005 - CIRG@UP 
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
package net.sourceforge.cilib.bioinf.rnaprediction;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;

/**
 * @author mneethling
 *
 */
public class RNAOptimisationSolution extends OptimisationSolution {
	
	/**
	 * Contrucs a new instance of <code>RNAOptimisationSolution</code>.
	 * 
	 * @param problem The optimisation problem for which this is a solution.
	 * @param position The position of the solution within the search space of the problem.
	 */
    //public OptimisationSolution(OptimisationProblem problem, Object position) {
    public RNAOptimisationSolution(RNAOptimisationProblem problem, RNAConformation position) {
    	super(problem,position);
    }
    
    /**
     * Returns the position of this solution within the search space of the problem.
     * 
     * @return The position of this solution in search space.
     */
    public RNAConformation getPosition() {
        return position;
    }
    
    /**
     * Returns the fitness of this solution according to {@link OptimisationProblem#getFitness(Object position, boolean count)}. 
     * Calling this function does not contribute to the number of fitness evaulations maintained by {@link OptimisationProblem}.
     * 
     * @return The fitness of this solution.
     */
    public Fitness getFitness() {
        return problem.getFitness(position, false);
    }
    
    private RNAOptimisationProblem problem;
    private RNAConformation position;
}
