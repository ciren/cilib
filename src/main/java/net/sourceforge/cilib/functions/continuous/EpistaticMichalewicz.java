/*
 * EpistaticMichalewicz.java
 *
 * Copyright (C) 2003 - 2008
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
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * EpistaticMichalewicz funtion.
 * 
 * Characteristics:
 * <ul>
 * <li>Multi-modal</li>
 * </ul>
 * 
 * @TODO: Find the minimum!!!
 * 
 * @author  engel
 */
public class EpistaticMichalewicz extends ContinuousFunction {
	private static final long serialVersionUID = -4391269929189674709L;

	/** Creates a new instance of EpistaticMichalewicz. */
    public EpistaticMichalewicz() { 
        m = 10;
        setDomain("R(0, 3.141592653589793)^10");
    }
    
    /**
     * {@inheritDoc}
     */
    public EpistaticMichalewicz getClone() {
    	return new EpistaticMichalewicz();
    }
    
    public Object getMinimum() {
    	if (this.getDimension() == 5)
    		return new Double(-4.687);
    	else if (this.getDimension() == 10)
    		return new Double(-9.66);
    	
    	return new Double(-Double.MAX_VALUE);
    }
    
    /** Each function must provide an implementation which returns the function value
     * at the given position. The length of the position array should be the same
     * as the function dimension.
     *
     * @param x The position
     *
     */
    public double evaluate(Vector input) {
        /*double x = X.getReal(0);
        double y = X.getReal(1);

        double result = Math.sin(x)*Math.pow(Math.sin(x*x/Math.PI), 20);
        result += Math.sin(y)*Math.pow(Math.sin(y*y/Math.PI), 20);
        return result;*/
    	
    	double sumsq = 0.0;
    	
    	for (int i = 0; i < getDimension(); i++) {
    		double x = input.getReal(i);
    		sumsq += Math.sin(x) * Math.pow(Math.sin(((i+1) * x * x)/Math.PI), 2*m);
    	}
    	
    	return -sumsq;
    }
    
    public int getM() {
        return m;
    }
    
    public void setM(int m) {
        this.m = m;
    }
    
    private int m;
}
