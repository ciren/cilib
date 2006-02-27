/*
 * FunctionMinimisationProblem.java
 *
 * Created on January 12, 2003, 2:10 PM
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

import net.sourceforge.cilib.Functions.*;

/**
 * An implementation of {@link OptimisationProblem} that can be used to find
 * the minimum of any {@link net.sourceforge.cilib.Functions.Function}.
 *
 * @author  espeer
 */
public class FunctionMinimisationProblem implements OptimisationProblem {
    
    /** 
     * Creates a new instance of <code>FunctionMinimisationProblem</code> with <code>null</code> function.
     * Remember to always set a {@link net.sourceforge.cilib.Functions.Function} before attempting to apply 
     * an {@link net.sourceforge.cilib.Algorithm.OptimisationAlgorithm} to this problem.
     *
     * {@see #setFunction(net.sourceforge.cilib.Functions.Function}
     */
    public FunctionMinimisationProblem() {
        function = null;
    }
    
    /**
     * Creates a new instance of <code>FunctionMinimisationProblem</code> with a given {@link net.sourceforge.cilib.Functions.Function}.
     */
    public FunctionMinimisationProblem(Function function) {
        this.function = function;
    }
    
    public int getDimension() {
        return function.getDimension();
    }
    
    public Domain getDomain(int component) {
        return function.getDomain();
    }
    
    public double getFitness(double[] solution) {
        return - function.evaluate(solution);
    }
    
    /**
     * Sets the function that is to be minimised.
     *
     * @param function The function.
     */
    public void setFunction(Function function) {
        this.function = function;
    }
    
    /**
     * Accessor for the function that is to be minimised.
     *
     * @return The function
     */
    public Function getFunction() {
        return function;
    }
    
    /**
     * <p>
     * Returns the error for the given solution. That is, a lower error value
     * is returned if the given solution is a better minimiser for the function.
     * </p>
     * <p>
     * The lowest possible error (corresponding to the best solution) should be 0. However,
     * if the function incorrectly reports its minimum value then it is possible for error
     * values to be negative.
     * </p>
     * 
     * @param The solution for which an error is saught.
     * @return The error.
     */
    public double getError(double[] solution) {
        return function.evaluate(solution) - function.getMinimum();
    }
    
    private Function function;
}
