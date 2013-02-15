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
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda2mod_deb;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the g function of the FDA2_mod problem defined in the following paper:
 * K. Deb, U. Rao N and S. Karthik. Dynamic Multi-objective optimization and decision making
 * using  modified NSGA-II: A case study on hydro-thermal power scheduling, In Proceedings of
 * the International Conference on Evolutionary Multi-Criterion Optimization (EMO), Lecture
 * Notes in Computer Science, 4403:803-817, Springer-Verlag Berlin/Heidelberg, 2007.
 *
 * @author Marde Greeff
 */

public class FDA2_g implements ContinuousFunction {

    private static final long serialVersionUID = 8726700022515610264L;

    //setDomain("R(-1, 1)^15")

    /**
     * Evaluates the function.
     */
    @Override
    public Double apply(Vector x) {

        double sum = 1.0;

        for (int k=0; k < x.size(); k++) {
            sum += Math.pow(x.doubleValueOf(k), 2);
        }

        return sum;
    }

}
