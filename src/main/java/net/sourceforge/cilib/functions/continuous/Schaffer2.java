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
 *
 * @author  engel
 */
public class Schaffer2 extends ContinuousFunction {
    private static final long serialVersionUID = 7289010453718555694L;

    /** Creates a new instance of Schaffer. Domain defaults to R(-100, 100)^2 */
    public Schaffer2() {
        //constraint.add(new DimensionValidator(2));
        setDomain("R(-100, 100)^2");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schaffer2 getClone() {
        return new Schaffer2();
    }

    /**
     * {@inheritDoc}
     */
    public Double getMinimum() {
        return 0.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        double sum_squares = input.getReal(0) * input.getReal(0) + input.getReal(1) * input.getReal(1);
        double term1 = Math.pow(sum_squares, 0.25);
        double term2 = Math.pow(50 * Math.pow(sum_squares, 0.1), 2) + 1;
        return term1 * term2;
    }

}
