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
 *
 * switch (n) {
//            case 5:
//                return -10.15320;
//            case 7:
//                return -10.40294;
//            case 10:
//                return -10.53641;
//            default:
//                return super.getMinimum();
//        }
 *
 * R(0, 10)^4
 * 
 * @author  engel
 */
public class ShekelN implements ContinuousFunction {

    private static final long serialVersionUID = 4420382656606698465L;

    /**
     * Creates a new instance of Step. Default domain is set to R(0, 10)^4
     */
    public ShekelN() {
        n = 10;

        //constraint.add(new DimensionValidator(4));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShekelN getClone() {
        return new ShekelN();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sum = 0;
        for (int i = 0; i < n; ++i) {
            double innerSum = 0;
            for (int j = 0; j < 4; ++j) {
                innerSum += (input.doubleValueOf(j) - A[i][j]) * (input.doubleValueOf(j) - A[i][j]);
            }
            sum += 1 / (innerSum + C[i]);
        }
        return -sum;
    }

    /**
     * Set the N vaue for the function.
     * @param n The value to set.
     */
    public void setN(int n) {
        if (n != 5 && n != 7 && n != 10) {
            throw new IllegalArgumentException("invalid N value");
        }

        this.n = n;
    }
    private int n;
    private static final double[][] A = {{4.0, 4.0, 4.0, 4.0},
        {1.0, 1.0, 1.0, 1.0},
        {8.0, 8.0, 8.0, 8.0},
        {6.0, 6.0, 6.0, 6.0},
        {3.0, 7.0, 3.0, 7.0},
        {2.0, 9.0, 2.0, 9.0},
        {5.0, 5.0, 3.0, 3.0},
        {8.0, 1.0, 8.0, 1.0},
        {6.0, 2.0, 6.0, 2.0},
        {7.0, 3.6, 7.0, 3.6},};
    private static final double[] C = {0.1, 0.2, 0.2, 0.4, 0.4, 0.6, 0.3, 0.7, 0.5, 0.5};
}
