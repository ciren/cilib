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
 *
 * @author  espeer
 */
public class FunctionMinimisationProblem implements OptimisationProblem {
    
    public FunctionMinimisationProblem() {
        function = null;
    }
    
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
    
    public void setFunction(Function function) {
        this.function = function;
    }
    
    public Function getFunction() {
        return function;
    }
    
    public double getError(double[] solution) {
        return function.evaluate(solution) - function.getMinimum();
    }
    
    private Function function;
}
