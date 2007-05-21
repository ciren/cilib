/*
 * EpistaticMichalewicz.java
 *
 * Created on June 4, 2003, 5:14 PM
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
 *   
 */

package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Vector;


/**
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

	/** Creates a new instance of EpistaticMichalewicz */
    public EpistaticMichalewicz() { 
        m = 10;
        setDomain("R(0, 3.141592653589793)^10");
    }
    
    /** Each function must provide an implementation which returns the function value
     * at the given position. The length of the position array should be the same
     * as the function dimension.
     *
     * @param x The position
     *
     */
    public double evaluate(Vector x) {
        double sum = 0;
        
        double cosTerm = Math.cos(Math.PI / 6);
        double sinTerm = Math.sin(Math.PI / 6);
        
        for (int i = 0; i < getDimension(); ++i) {
            double y;
            if ( (i % 2) == 0) {
                y = x.getReal(i) * cosTerm - x.getReal(i+1) * sinTerm;
            }
            else if (i  == (getDimension() - 1)) {
                y = x.getReal(getDimension() - 1);                
            }
            else {
                y = x.getReal(i-1) * sinTerm + x.getReal(i) * cosTerm;
            }
            sum += Math.sin(y) * Math.pow(Math.sin((i+1) * y * y / Math.PI), 2 * m);
        }
        return - sum;               
    }
    
    public int getM() {
        return m;
    }
    
    public void setM(int m) {
        this.m = m;
    }
    
    private int m;
}
