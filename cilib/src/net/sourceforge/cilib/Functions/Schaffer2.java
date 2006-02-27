/*
 * Schaffer2.java
 *
 * Created on June 4, 2003, 2:39 PM
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

import net.sourceforge.cilib.Problem.Domain;

/**
 *
 * @author  engel
 */
public class Schaffer2 extends Function {
    
    public static final int DEFAULT_DIMENSION = 2;
    public static final Domain DEFAULT_DOMAIN = new Domain(-100,100);
    
    /** Creates a new instance of Schaffer */
    public Schaffer2() {
        super(DEFAULT_DIMENSION, DEFAULT_DOMAIN, 0, Double.MAX_VALUE);
    }
    
    /** Each function must provide an implementation which returns the function value
     * at the given position. The length of the position array should be the same
     * as the function dimension.
     *
     * @param x The position
     *
     */
    public double evaluate(double[] x) {
        double sum_squares = x[0] * x[0] + x[1] * x[1];
        double term1 = Math.pow(sum_squares,0.25);
        double term2 = Math.pow(50 * Math.pow(sum_squares,0.1),2) + 1;
        return term1 * term2;
    }

    public void setDimension(int dimension) {
        throw new UnsupportedOperationException("setDimension(int) not supported by Schaffer2");
    }
}
