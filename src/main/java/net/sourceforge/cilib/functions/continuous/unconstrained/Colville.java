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
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p><b>The Colville Function.</b></p>
 *
 * <p><b>Reference:</b> Doo-Hyun Choi, Cooperative mutation based evolutionary programming for continuous function optimization, Operations Research Letters, Volume 30, Issue 3, June 2002, Pages 195-201/p>
 *
 * <p>Minimum:
 * <ul>
 * <li> f(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (1, 1, 1, 1)</li>
 * <li> for x_i in [-10, 10]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Unimodal</li>
 * <li>Non-seperable</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 *
 * @author  engel
 */
public class Colville extends ContinuousFunction {
   private static final long serialVersionUID = 4561331100889232057L;

    /**
     * Creates a new instance of Colville. Sets the domain to R(-10.0, 10.0)^4 by default.
     */
    public Colville() {
        //constraint.add(new DimensionValidator(4));
        setDomain("R(-10, 10)^4");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Colville getClone() {
        return new Colville();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getMinimum() {
        return 0.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        double a = input.getReal(0);
        double b = input.getReal(1);
        double c = input.getReal(2);
        double d = input.getReal(3);

        return 100 * (b-a*a) * (b-a*a) +
            (1-a) * (1-a) +
            90 * (d-c*c) * (d-c*c) +
            (1-c) * (1-c) +
            10.1 * ((c-1) * (c-1) + (d-1) * (d-1)) +
            19.8 * (b-1) * (d-1);
    }

}

