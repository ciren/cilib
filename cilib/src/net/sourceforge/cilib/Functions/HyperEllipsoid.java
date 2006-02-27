/*
 * HyperEllipsoid.java
 *
 * Created on June 4, 2003, 1:20 PM
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
public class HyperEllipsoid extends Function {
    
    public static final int DEFAULT_DIMENSION = 30;
    public static final Domain DEFAULT_DOMAIN = new Domain(-1,1);
    
    /** Creates a new instance of HyperEllipsoid */
    public HyperEllipsoid() {
        super(DEFAULT_DIMENSION, DEFAULT_DOMAIN, 0, Double.MAX_VALUE);
    }
    
    
    public double evaluate(double[] x) {
        double tmp = 0;
        for (int i = 0; i < dimension; ++i) {
            tmp += (i + 1) * (i + 1)  * x[i] * x[i];
        }
        return tmp;
    }
    
}
