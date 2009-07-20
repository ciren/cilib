/**
 * Copyright (C) 2003 - 2009
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
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda2;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f1 function of the FDA2 problem defined on page 429 in the following paper:
 * M.Farina, K.Deb, P.Amato. Dynamic multiobjective optimization problems: test cases, approximations
 * and applications, IEEE Transactions on Evolutionary Computation, 8(5): 425-442, 2003
 *
 * @author Marde Greeff
 */

public class FDA2_f1 extends ContinuousFunction {

    private static final long serialVersionUID = 3509865802519318920L;

    /**
     * Default Contructor
     */
    public FDA2_f1 () {
        super();
        setDomain("R(0, 1)");
    }

    /**
     * copy constructor
     * @param copy
     */
    public FDA2_f1(FDA2_f1 copy) {
        super(copy);
        this.setDomain(copy.getDomain());
    }

    /**
     * returns a clone
     */
    public FDA2_f1 getClone() {
        return new FDA2_f1(this);
    }

    /**
     * Evaluates the function
     * f1(X_I) = x_1
     */
    @Override
    public Double evaluate(Vector input) {
        return Math.abs(input.getReal(0));
    }
}
