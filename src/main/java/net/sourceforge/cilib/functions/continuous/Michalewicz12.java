/* 
 * Michalewicz.java
 *
 * Created on June 4, 2003, 4:57 PM 
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
import net.sourceforge.cilib.type.types.Vector;

/**
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * 
 * Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Continuous</li>
 * <li>Separable</li>
 * </ul>
 * 
 * f(x) = -4.687 if n = 5
 * f(x) = -9.66  if n = 10
 * 
 * @author Clive Naicker
 * @version 1.0
 */

public class Michalewicz12 extends ContinuousFunction {
	private static final long serialVersionUID = -2885728189740122807L;
	
	private int m;

    public Michalewicz12() {        
        //constraint.add(new DimensionValidator(2));
        setDomain("R(0, 3.141592653589793)^30");
        
        m = 10;
    }
    
    public Object getMinimum() {
    	if (this.getDimension() == 5)
    		return new Double(-4.687);
    	else if (this.getDimension() == 10)
    		return new Double(-9.66);
    	
    	return new Double(- Double.MAX_VALUE);
    }

    public double evaluate(Vector X) {
        /*double x = X.getReal(0);
        double y = X.getReal(1);

        double result = Math.sin(x)*Math.pow(Math.sin(x*x/Math.PI), 20);
        result += Math.sin(y)*Math.pow(Math.sin(y*y/Math.PI), 20);
        return result;*/
    	
    	double sumsq = 0.0;
    	
    	for (int i = 0; i < getDimension(); i++) {
    		double x = X.getReal(i);
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

}