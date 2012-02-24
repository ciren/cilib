/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.dimp2;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f1 function of the DIMP2 problem defined in the following paper:
 * W.T. Koo and C.K. Goh and K.C. Tan. A predictive gradien strategy for multiobjective
 * evolutionary algorithms in a fast changing environment, Memetic Computing, 2:87-110,
 * 2010.
 * 
 * @author Marde Greeff
 */

public class DIMP2_f1 implements ContinuousFunction {

    
    //Domain("R(0, 1)");
    
    /**
     * Evaluates the function.
     */
    @Override
    public Double apply(Vector x) {
        double value = Math.abs(x.doubleValueOf(0));
        if (value > 1.0)
            value = ((double)value) - 1.0;
        return value;
    }
}
