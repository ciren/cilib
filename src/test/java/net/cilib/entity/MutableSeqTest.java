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
package net.cilib.entity;

import com.google.common.base.Supplier;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class MutableSeqTest {

    @Test
    public void plus() {
        CandidateSolution solution = CandidateSolution.of(1.0, 3.0);

        // z = x + y
        double[] result = solution.toMutableSeq().plus(solution).toArray();
        Assert.assertArrayEquals(new double[]{2, 6}, result, 0.001);
    }

    @Test
    public void subtract() {
        CandidateSolution solution = CandidateSolution.of(1.0, 3.0);

        // z = x - y
        double[] result = solution.toMutableSeq().subtract(solution).toArray();
        Assert.assertArrayEquals(new double[]{0.0, 0.0}, result, 0.001);
    }

    @Test
    public void multiply() {
        CandidateSolution solution = CandidateSolution.of(1.0, 3.0);

        // z = x * y
        double[] result = solution.toMutableSeq().multiply(2.0).toArray();
        Assert.assertArrayEquals(new double[]{2.0, 6.0}, result, 0.001);
    }

    @Test
    public void multiplySupplier() {
        CandidateSolution solution = CandidateSolution.of(1.0, 3.0);
        Supplier<Double> supplier = new Supplier<Double>() {
            private double value = 1.0;
            @Override
            public Double get() {
                value *= 2.0;
                return value;
            }
        };

        double[] result = solution.toMutableSeq().multiply(supplier).toArray();
        Assert.assertArrayEquals(new double[]{2.0, 12.0}, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalDivide() {
        CandidateSolution solution = CandidateSolution.of(new double[]{});
        solution.toMutableSeq().divide(0.0);
    }

    @Test
    public void divide() {
        CandidateSolution solution = CandidateSolution.of(1.0, 3.0);

        // z = x / y
        double[] result = solution.toMutableSeq().divide(1.0).toArray();
        Assert.assertArrayEquals(new double[]{1.0, 3.0}, result, 0.001);
    }

    @Test
    public void divideSupplier() {
        CandidateSolution solution = CandidateSolution.of(1.0, 3.0);

        // z = x / y
        double[] result = solution.toMutableSeq().divide(new Supplier<Double>() {
            @Override
            public Double get() {
                return 1.0;
            }

        }).toArray();
        Assert.assertArrayEquals(new double[]{1.0, 3.0}, result, 0.001);
    }

    @Test
    public void complexFunctionalOperation() {
        CandidateSolution solution = CandidateSolution.of(1.0, 2.0);

        double[] result = solution.toMutableSeq().multiply(4.0).plus(solution).toArray();
        Assert.assertArrayEquals(new double[]{5.0, 10.0}, result, 0.001);
    }
}
