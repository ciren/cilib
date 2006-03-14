/*
 * Bohachevsky2.java
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
 *   
 */

package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Vector;

/**
 *
 * @author  engel
 */
public class Bohachevsky2 extends ContinuousFunction {
    
    /** Creates a new instance of Bohachevsky2 */
    public Bohachevsky2() {
        //constraint.add(new DimensionValidator(2));
        setDomain("R(-50, 50)^2");
    }
    
    /** Each function must provide an implementation which returns the function value
     * at the given position. The length of the position array should be the same
     * as the function dimension.
     *
     * @param x The position
     *
     */
    public double evaluate(Vector x) {
        return x.getReal(0)*x.getReal(0) + 2*x.getReal(1)*x.getReal(1) - 0.3*Math.cos(3*Math.PI*x.getReal(0)) * Math.cos(4*Math.PI*x.getReal(1))+0.3;
    }
    
}
