/* MultimodalFunction2.java
 *
 * Created on June 24, 2003, 21:00 PM
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
 */

package net.sourceforge.cilib.Functions;

import net.sourceforge.cilib.Type.Types.Vector;


public class MultimodalFunction2 extends ContinuousFunction { 

    public MultimodalFunction2() {
        setDomain("R(0, 1)^1");
    }

    public Object getMinimum() {
        return new Double(0);
    }
    
    public double evaluate(Vector parm1) {
        double dResult = 0.0;
        for (int i = 0; i < getDimension(); i++) {
            double x = Math.pow(Math.sin(5.0 * Math.PI * parm1.getReal(i)), 6.0);
            double exp1 = -2.0 * Math.log(2);
            double exp2 = Math.pow((parm1.getReal(i) - 0.1) / 0.8, 2.0);
            double y = Math.exp(exp1 * exp2);
            dResult += x * y;
        }
        return dResult;
    }
}