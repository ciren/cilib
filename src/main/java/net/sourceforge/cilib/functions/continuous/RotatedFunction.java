/*
 * RotatedFunction.java
 *  
 * Created on February 24, 2003, 12:19 PM
 *
 *   
 * Copyright (C) 2003 - 2006 
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

package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author  Edwin Peer
 */
public class RotatedFunction extends ContinuousFunction {
    private static final long serialVersionUID = -5231187596635372920L;

	/** Creates a new instance of RotatedFunction */
    public RotatedFunction() {
        function = null;
    }
    
    public void setFunction(Function function) {
        this.function = function;
    }
    
    public Object getMinimum() {
    	// TODO: Adjust min for rotated function
        return function.getMinimum();
    }
    
    public Object getMaximum() {
    	// TODO: Adjust max for rotated function
        return function.getMaximum();
    }

    public void setDomain(String domain) {
        function.setDomain(domain);
    }
    
    /*public String getDomain() {
        return function.getDomain();
    }*/
    
    //public DomainComponent getDomainComponent() {
    //    return function.getDomainComponent();
    //}
    
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
    public double evaluate(Vector x) {
        // TODO: rotate the function here
   
        throw new UnsupportedOperationException();
        
    }

    private Function function;
    //private double[][] rotation;
}

