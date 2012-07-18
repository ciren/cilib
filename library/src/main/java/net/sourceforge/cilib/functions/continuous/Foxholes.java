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
 * This is an implementation of the Foxholes function.
 *
 * The minimum of the function is located at <tt>f(-32, -32) ~= 1.0</tt>
 *
 * Default domain: R(-65.536,65.536)^2
 *
 */
public class Foxholes implements ContinuousFunction {

    private static final long serialVersionUID = 6407823129058106208L;
    private double[][] a = {
        {-32.0, -16.0, 0.0, 16.0, 32.0},
        {-32.0, -16.0, 0.0, 16.0, 32.0},};

    /**
     * {@inheritDoc}
     */
    // This impl is according to the function defined by Xin Yao in the FastEP and by the DE guys
    @Override
    public Double apply(Vector input) {
        double result = 0.002;
        double sum = 0.0;

        for (int j = 0; j < 25; j++) {
            double tmp = 0.0;
            double tmp_a = 0.0;

            for (int i = 0; i <= 1; i++) {
                if (i == 0) {
                    tmp_a = a[0][j % 5];
                } else {
                    tmp_a = a[1][j / 5];
                }

                tmp += Math.pow((input.doubleValueOf(i) - tmp_a), 6);
            }

            sum += 1.0 / (j + tmp);
        }

        return 1.0 / (result + sum);
    }
}
