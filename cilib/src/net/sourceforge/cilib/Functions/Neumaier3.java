/*
 * Neumaier.java
 *
 * Created on June 4, 2003, 1:56 PM
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
public class Neumaier3 extends Function {
    
    public static final int DEFAULT_DIMENSION = 30;
    public static final Domain DEFAULT_DOMAIN = new Domain(-900,900);
    
    /** Creates a new instance of Neumaier */
    public Neumaier3() {
        super(DEFAULT_DIMENSION, DEFAULT_DOMAIN, (DEFAULT_DIMENSION * (DEFAULT_DIMENSION + 4) * (DEFAULT_DIMENSION - 1)) / 6, Double.MAX_VALUE);
    }
    
    /** Each function must provide an implementation which returns the function value
     * at the given position. The length of the position array should be the same
     * as the function dimension.
     *
     * @param x The position
     *
     */
    public double evaluate(double[] x) {
        double tmp1 = 0;
        double tmp2 = 0;
        for (int i = 0; i < dimension; ++i) {
            tmp1 += (x[i] - 1) * (x[i] - 1);
        }
        for (int i = 1; i < dimension; ++i) {
            tmp2 += x[i] * x[i - 1];
        }
        return tmp1 - tmp2;
    }
    
    public void setDimension(int dimension) {
        super.setDimension(dimension);
        domain.setLowerBound(- dimension * dimension);
        domain.setUpperBound(dimension * dimension);
        minimum = (dimension * (dimension + 4) * (dimension - 1)) / 6;
    }
}
