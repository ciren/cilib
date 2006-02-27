/*
 * Problem.java
 *
 * Created on January 11, 2003, 1:02 PM
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

package net.sourceforge.cilib.Problem;

/**
 * All optimisation problems must implement this interface. This interface
 * ensures that an optimisation algorithm has all the information it needs
 * to find a solution. 
 *
 * {@see net.sourceforge.cilib.Algorithm.OptimisationAlgorithm}
 *
 * @author  espeer
 */
public interface OptimisationProblem extends Problem { 
    
    /**
     * Returns the fitness of a potential solution to the problem. The solution
     * vector is an array of dimension {@link #getDimension()}. Higher fitness
     * values indicate a better solution.
     *
     * @param solution The potential solution found by the optimisation algorithm.
     * @return The fitness of the solution. Larger fitness values correspond to better solutions.
     */
    public double getFitness(double[] solution);
    
    /**
     * <p>
     * Returns the domain of a given component. The {@link Domain} class defines the 
     * upper and lower search bounds for each dimension of a multi-dimensional search 
     * space. By specifying the component, each dimension can have difference bounds.
     * </p>
     * <p>
     * Note that the {@link net.sourceforge.cilib.Algorithm.OptimisationAlgorithm} is not
     * necessarily contrained to strictly produce solutions within this domain. Typically, 
     * the optimisation will only use these bounds to initialise the algorithm within the 
     * search domain.
     * </p> 
     *
     * @param component The component to return the domain for.
     * @return A {@link Domain} object representing the search bounds.
     */
    public Domain getDomain(int component);
    
    /**
     * Returns the number of dimensions that the problem has.
     *
     * @return The problem dimension.
     */
    public int getDimension();
}
