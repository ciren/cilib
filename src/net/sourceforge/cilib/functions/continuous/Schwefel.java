/*
 * Schwefel.java
 *
 * Created on January 12, 2003, 3:58 PM
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

package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Vector;


/**
 * Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Separable</li>
 * <li>Discontinuous</li>
 * </ul>
 * 
 * f(x) = 0; x = (-420.9687,...,-420.9687);
 * 
 * x e [-512.03,511.97]
 * 
 * @author  Edwin Peer
 */
// TODO: Check discontinuous / continuous
public class Schwefel extends ContinuousFunction { // ?
    
    public Schwefel() {
        setDomain("R(-512.03, 511.97)^30");
    }
    
    public Object getMinimum() {
        return new Double(0);
    }
    
    public double evaluate(Vector x) {
        double sum = 0;
        for (int i = 0; i < getDimension(); ++i) {
            sum += x.getReal(i) * Math.sin(Math.sqrt(Math.abs(x.getReal(i))));
        }
        sum += getDimension() * 4.18982887272434686131e+02;
        return sum;
    }
    
}
