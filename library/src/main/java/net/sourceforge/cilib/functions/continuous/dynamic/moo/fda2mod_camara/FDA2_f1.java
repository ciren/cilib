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
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda2mod_camara;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f1 function of the FDA2_mod problem defined in the following paper:
 * M. Camara, J. Ortega and F. de Toro. Approaching dynamic multi-objective optimization
 * problems by using parallel evolutionary algorithms, Advances in Multi-Objective Nature 
 * Inspired Computing, Studies in Computational Intelligence, vol. 272, pp. 63-86,
 * Springer Berlin/Heidelberg, 2010.
 *
 * @author Marde Greeff
 */

public class FDA2_f1 implements ContinuousFunction {

    private static final long serialVersionUID = 3509865802519318920L;

    //Domain("R(0, 1)")

    /**
     * Evaluates the function.
     */
    @Override
    public Double apply(Vector x) {
        return (double)(Math.abs(x.doubleValueOf(0)));
    }
}
