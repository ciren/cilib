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
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda2;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the g*h function of the FDA2 problem defined on page 428 in the following paper:
 * M.Farina, K.Deb, P.Amato. Dynamic multiobjective optimization problems: test cases, approximations
 * and applications, IEEE Transactions on Evolutionary Computation, 8(5): 425-442, 2003
 *
 */
public class FDA2_f2 implements ContinuousFunction {

    private static final long serialVersionUID = 7814549850032093196L;

    ContinuousFunction fda2_g;
    ContinuousFunction fda2_h;

    /**
     * Default constructor
     */
    public FDA2_f2() {
    }

    /**
     * copy constructor
     * @param copy
     */
    public FDA2_f2(FDA2_f2 copy) {
        this.fda2_g = copy.fda2_g;
        this.fda2_h = copy.fda2_h;
    }

    /**
     * Sets the g function that is used in the FDA2 problem
     * @param fda1_g
     */
    public void setFDA2_g(ContinuousFunction fda2_g) {
        this.fda2_g = fda2_g;
    }

    /**
     * Returns the g function that is used in the FDA2 problem
     * @return
     */
    public ContinuousFunction getFDA2_g() {
        return this.fda2_g;
    }

    /**
     * Sets the f1 function that is used in the FDA2 problem
     * @param fda1_f1
     */
    public void setFDA2_h(ContinuousFunction fda2_h) {
        this.fda2_h = fda2_h;
    }

    /**
     * Gets the f1 function that is used in the FDA2 problem
     * @return
     */
    public ContinuousFunction getFDA2_h() {
        return this.fda2_h;
    }

    /**
     * Evaluates the function
     * g*h
     */
    public Double apply(Vector input) {
//        Vector y = input;
//        if (input.getDimension() > 1) {
//            y = input.subList(1, fda2_g.getDimension()); //-1
//        }
//        double g = this.fda2_g.evaluate(y);
//        double h = this.fda2_h.evaluate(input);
//
//        double value = g * h;
//        return value;
        return 0.0;
    }
}
