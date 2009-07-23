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
 *  <p><b>The Absolute Value Function.</b></p>
 *
 * <p>Minimum:
 * <ul>
 * <li> f(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x_i in [-100,100]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Unimodal</li>
 * <li>Seperable</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 *
 * @author Olusegun Olorunda
 */
public class AbsoluteValue extends ContinuousFunction {
    private static final long serialVersionUID = 1662988096338786773L;

    /**
     * Create an instance of {@linkplain AbsoluteValue}. Domain is defaulted to R(-100, 100)^30.
     */
    public AbsoluteValue() {
        setDomain("R(-100, 100)^30");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbsoluteValue getClone() {
        return new AbsoluteValue();
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
        double tmp = 0.0;
        for (int i = 0; i < getDimension(); ++i) {
            tmp += Math.abs(input.getReal(i));
        }
        return tmp;
    }

}
