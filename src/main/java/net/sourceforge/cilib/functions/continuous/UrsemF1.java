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
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * UrsemF1 function.
 *
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 *
 * R(-2.5, 3)^2
 * Minimum: 4.81681
 * 
 * @author Clive Naicker
 * @version 1.0
 */
public class UrsemF1 implements ContinuousFunction {

    private static final long serialVersionUID = -2595919942608678319L;

    public UrsemF1() {
        //constraint.add(new DimensionValidator(2));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UrsemF1 getClone() {
        return new UrsemF1();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);

        return Math.sin(2.0 * x - 0.5 * Math.PI) + 3.0 * Math.cos(y) + 0.5 * x;
    }
}
