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
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda1mod_zhou;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f1 function of the FDA1_mod problem defined in the following paper:
 * A. Zhou et al. Prediction-based population re-initialization for evolutionary dynamic
 * multi-objective optimization, In Proceedings of the International Conference on
 * Evolutionary Multi-Criterion Optimization (EMO), Lecture  * Notes in Computer Science,
 * 4403:832-846, Springer-Verlag Berlin/Heidelberg, 2007.
 *
 * @author Marde Greeff
 */

public class FDA1_f1 implements ContinuousFunction {

    private static final long serialVersionUID = 1914230427150406406L;

    //Domain("R(0, 1)")
    
    /**
     * Evaluates the function.
     */
    @Override
    public Double apply(Vector x) {
        double val = Math.abs(x.doubleValueOf(0));
        if (val > 1.0)
            val = 2.0 - (double)val;
        
        return val;
    }
}
