/*
 * Ackley.java
 *
 * Created on January 11, 2003, 2:09 PM
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
 * The Ackley function
 * 
 * Minimum: f(x) = 0;  x = (0, 0, ...., 0)
 * 
 * Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Not seperable</li>
 * <li>Regular</li>
 * </ul>
 * 
 * @author  Edwin Peer
 */
public class Ackley extends ContinuousFunction {
  
	private static final long serialVersionUID = -9165773388976245103L;

	public Ackley() {
        setDomain("R(-30, 30)^30");
    }
    
    public Object getMinimum() {
        return new Double(0);
    }
    
    public double evaluate(Vector x) {
        double sumsq = 0.0;
        double sumcos = 0.0;
        for (int i = 0; i < getDimension(); ++i) {
            sumsq += x.getReal(i) * x.getReal(i);
            sumcos += Math.cos(2 * Math.PI * x.getReal(i));
        }
        return - 20.0 * Math.exp(-0.2 * Math.sqrt(sumsq / getDimension())) - Math.exp(sumcos / getDimension()) + 20 + Math.E;
    }
}

