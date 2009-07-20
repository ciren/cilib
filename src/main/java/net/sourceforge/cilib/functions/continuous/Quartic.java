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
 * The Quartic function.
 * Source: Evolutionary Programming Made Faster (Xin Yao)
 * @author Gary Pampara
 */
public class Quartic extends ContinuousFunction {
    private static final long serialVersionUID = 1830591489569513511L;

    /**
     * Create a new instance of {@linkplain Quadric}. Domain defaults to R(-1.28, 1.28)^30.
     */
    public Quartic() {
        setDomain("R(-1.28, 1.28)^30");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Quartic getClone() {
        return new Quartic();
    }

    /**
     * {@inheritDoc}
     *
     * This is extra stuff i hope is added.
     */
    public Double getMinimum() {
        return 0.0;
    }

    /**
     * {@inheritDoc}
     */
    public Double getMaximum() {
        return 1248.2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        double result = 0.0;

        for (int i = 0; i < input.getDimension(); i++) {
            double square = input.getReal(i) * input.getReal(i);
            double square2 = square * square;

            result += i * square2;
        }

        return result;
    }
}
