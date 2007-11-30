/*
 * Bohachevsky3.java
 *
 * Created on Nveomber 12, 2007, 10:30 AM
 *
 * 
 * Copyright (C) 2003 - 2007 
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

package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p><b>Bohachevsky 3</b></p>
 * 
 * <p><b>Reference:</b> Global Optimization Meta-Heuristics Website,
 * http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/go.htm</p>
 * 
 * <p>Minimum:
 * <ul>
 * <li> f(<b>x</b>*) = 0 </li>
 * <li> <b>x</b>* = (0, 0)</li>
 * <li> for x_1, x_2 in [-100, 100]</li>
 * </ul>
 * </p>
 * 
 * <p>Characteristics:
 * <ul>
 * <li>Only defined for 2 dimensions</li>
 * <li>Unimodal</li>
 * <li>Seperable</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 * 
 * @author Andries Engelbrecht
 */

public class Bohachevsky3 extends ContinuousFunction {
	private static final long serialVersionUID = -1572998736995724677L;

	/** Creates a new instance of Bohachevsky2 */
    public Bohachevsky3() {
        //constraint.add(new DimensionValidator(2));
        setDomain("R(-100, 100)^2");
    }
    
    public Bohachevsky3 getClone() {
    	return new Bohachevsky3();
    }
    
    /* (non-Javadoc)
	 * @see net.sourceforge.cilib.functions.redux.ContinuousFunction#evaluate(net.sourceforge.cilib.type.types.container.Vector)
	 */
	@Override
    public double evaluate(Vector x) {
        return x.getReal(0)*x.getReal(0) + 2*x.getReal(1)*x.getReal(1) - 0.3*Math.cos(3*Math.PI*x.getReal(0) + 4*Math.PI*x.getReal(1))+0.3;
    }
}
