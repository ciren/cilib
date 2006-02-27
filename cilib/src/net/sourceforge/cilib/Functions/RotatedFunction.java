/*
 * RotatedFunction.java
 *  
 * Created on February 24, 2003, 12:19 PM
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
 */

package net.sourceforge.cilib.Functions;

import net.sourceforge.cilib.Problem.Domain;

/**
 *
 * @author  espeer
 */
public class RotatedFunction extends Function {
    
    /** Creates a new instance of RotatedFunction */
    public RotatedFunction() {
        super(0, null, -Double.MAX_VALUE, Double.MAX_VALUE);
        function = null;
    }
    
    public void setFunction(Function function) {
        this.function = function;
        
    }
    
    public double getMinimum() {
        return function.getMinimum();
    }

    public void setDomain(Domain domain) {
        function.setDomain(domain);
    }
    
    public Domain getDomain() {
        return function.getDomain();
    }
    
    public void setDimension(int Dimension) {
        function.setDimension(dimension);
    }
    
    public int getDimension() {
        return function.getDimension();
    }
    
    /** Each function must provide an implementation which returns the function value
     * at the given position. The length of the position array should be the same
     * as the function dimension.
     *
     * @param x The position
     *
     */
    public double evaluate(double[] x) {
        // TODO: rotate the function here
        
        return function.evaluate(x);
    }

    private Function function;
    private double[][] rotation;
}

