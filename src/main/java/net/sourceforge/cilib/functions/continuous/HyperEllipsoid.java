/*
 * HyperEllipsoid.java
 *
 * Created on June 4, 2003, 1:20 PM
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
 * Characteristics:
 * <ul>
 * <li>Unimodal</li>
 * <li>Continuous</li>
 * <li>Convex</li>
 * </ul>
 * 
 * f(x) = 0; x = (0,0,...,0)
 * 
 * @author  engel
 */
public class HyperEllipsoid extends ContinuousFunction {
    
    /** Creates a new instance of HyperEllipsoid */
    public HyperEllipsoid() {
        setDomain("R(-5.12,5.12)^30");
    }
    
    public Object getMinimum() {
        return new Double(0);
    }
    
    public double evaluate(Vector x) {
        double tmp = 0;
        for (int i = 0; i < getDimension(); ++i) {
            tmp += (i + 1) * x.getReal(i) * x.getReal(i);
        }
        return tmp;
    }
    
}
