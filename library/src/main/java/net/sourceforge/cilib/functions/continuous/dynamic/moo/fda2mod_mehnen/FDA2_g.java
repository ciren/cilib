/**
 * Computational Intelligence Library (CIlib) Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP) Department of Computer
 * Science University of Pretoria South Africa
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda2mod_mehnen;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the g function of the FDA2_mod problem defined in the
 * following paper: J. Mehnen, T. Wagner and G. Rudolph. Evolutionary
 * optimization of dynamic multi-objective test functions, In Proceedings of the
 * seconed Italian Workshop on Evolutionary Computation, 2006.
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

        if (x.size() > 1) {
            for (int k = 0; k < 15; k++) {
                sum += Math.pow(x.doubleValueOf(k), 2);
            }

            for (int kk = 15; kk < x.size(); kk++) {
                sum += Math.pow(x.doubleValueOf(kk) + 1.0, 2);
            }
        } else {
            for (int k = 0; k < x.size(); k++) {
                sum += Math.pow(x.doubleValueOf(k), 2);

                for (int kk = 0; kk < x.size(); kk++) {
                    sum += Math.pow(x.doubleValueOf(kk) + 1.0, 2);
                }
            }
        }

        return sum;
    }
}
