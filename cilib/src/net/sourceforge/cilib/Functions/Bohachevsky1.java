/*
 * Bohachevsky1.java
 *
 * Created on June 4, 2003, 4:49 PM
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

import net.sourceforge.cilib.Domain.Validator.DimensionValidator;

/**
 *
 * @author  engel
 */
public class Bohachevsky1 extends ContinuousFunction {
    
    /** Creates a new instance of Bohachevsky1 */
    public Bohachevsky1() {
        constraint.add(new DimensionValidator(2));
        setDomain("R(-50, 50)^2");
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
    public double evaluate(double[] x) {
        return x[0]*x[0] + 2*x[1]*x[1] - 0.3*Math.cos(3*Math.PI*x[0]) - 0.4*Math.cos(4*Math.PI*x[1])+0.7;
    }
    
}
