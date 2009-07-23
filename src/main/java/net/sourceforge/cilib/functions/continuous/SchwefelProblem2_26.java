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
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * SchwefelProblem 2_26.
 *
 * Characteristics:
 *
 * f(x) = -12569.5, x = (420.9687,...,420.9687);
 *
 * x e [-500,500]
 *
 * @author  Andries Engelbrecht
 */
// TODO: Check discontinuous / continuous

public class SchwefelProblem2_26 extends ContinuousFunction {
    private static final long serialVersionUID = -4483598483574144341L;

    public SchwefelProblem2_26() {
        setDomain("R(-500, 500)^30");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SchwefelProblem2_26 getClone() {
        return new SchwefelProblem2_26();
    }

    public Double getMinimum() {
        return -12569.5;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        double sum = 0.0;

        for (int i = 0; i < getDimension(); i++) {
            sum += input.getReal(i)*Math.sin(Math.sqrt(Math.abs(input.getReal(i))));
        }
        return -sum;
    }
}

