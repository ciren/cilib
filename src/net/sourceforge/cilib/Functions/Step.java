/*
 * Step.java
 *
 * Created on June 4, 2003, 1:46 PM
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
 *   
 */

package net.sourceforge.cilib.Functions;

import net.sourceforge.cilib.Type.Types.Vector;


/**
 * TODO: This function exhibits strange behaviour, don't use yet.
 *
 * @author  engel
 */
public class Step extends ContinuousFunction {
    
    /** Creates a new instance of Step */
    public Step() {
        setDomain("R(-5.12, 5.12)^5");
    }
    
    public Object getMinimum() {
        return new Double(0);
    }
    
    /** Each function must provide an implementation which returns the function value
     * at the given position. The length of the position array should be the same
     * as the function dimension.
     *
     * @param x The position
     *
     */
    public double evaluate(Vector x) {
        double tmp = 6 * getDimension();
        for (int i = 0; i < getDimension(); ++i) {
//            if (x[i] < domain.getLowerBound() || x[i] > domain.getUpperBound()) {
//                return Double.POSITIVE_INFINITY;
//            }
            tmp += Math.floor(x.getReal(i));
        }
        return tmp;
    }
    
}
