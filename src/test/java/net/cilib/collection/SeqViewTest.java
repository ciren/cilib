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
package net.cilib.collection;

import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.cilib.collection.immutable.CandidateSolution;
import org.junit.Assert;
import org.junit.Test;

import static net.cilib.collection.SeqView.divide;
import static net.cilib.collection.SeqView.multiply;

/**
 * @author gpampara
 */
public class SeqViewTest {

    @Test
    public void plus() {
        CandidateSolution solution = CandidateSolution.solution(1.0, 3.0);
        Seq result = solution.plus(solution); // z = x + y

        Assert.assertTrue(Iterables.elementsEqual(result, Lists.newArrayList(2.0, 6.0)));
    }

    @Test
    public void subtract() {
        CandidateSolution solution = CandidateSolution.solution(1.0, 3.0);
        Seq result = solution.subtract(solution); // z = x - y

        Assert.assertTrue(Iterables.elementsEqual(result, Lists.newArrayList(0.0, 0.0)));
    }

    @Test
    public void multiplication() {
        CandidateSolution solution = CandidateSolution.solution(1.0, 3.0);
        Seq result = multiply(2.0, solution); // z = x * y

        Assert.assertTrue(Iterables.elementsEqual(Lists.newArrayList(2.0, 6.0), result));
    }

    @Test
    public void multiplySupplier() {
        CandidateSolution solution = CandidateSolution.solution(1.0, 3.0);
        Supplier<Double> supplier = new Supplier<Double>() {
            private double value = 1.0;

            @Override
            public Double get() {
                value *= 2.0;
                return value;
            }
        };

        Seq result = multiply(supplier, solution);

        Assert.assertTrue(Iterables.elementsEqual(Lists.newArrayList(2.0, 12.0), result));
    }

    @Test(expected = ArithmeticException.class)
    public void illegalDivide() {
        CandidateSolution solution = CandidateSolution.solution(1.0);
        divide(0.0, solution);
    }

    @Test
    public void division() {
        CandidateSolution solution = CandidateSolution.solution(1.0, 3.0);
        SeqView result = divide(1.0, solution); // z = x / y

        Assert.assertTrue(Iterables.elementsEqual(Lists.newArrayList(1.0, 3.0), result));
    }

    @Test
    public void divideSupplier() {
        CandidateSolution solution = CandidateSolution.solution(1.0, 3.0);
        SeqView result = divide(new Supplier<Double>() {
                    @Override
                    public Double get() {
                        return 1.0;
                    }
                }, solution); // z = x / y

        Assert.assertTrue(Iterables.elementsEqual(Lists.newArrayList(1.0, 3.0), result));
    }

    @Test
    public void complexFunctionalOperation() {
        CandidateSolution solution = CandidateSolution.solution(1.0, 2.0);

        Seq result = multiply(4.0, solution).plus(solution);

        Assert.assertTrue(Iterables.elementsEqual(Lists.newArrayList(5.0, 10.0), result));
    }

    @Test
    public void performance() throws InterruptedException {
//        Thread.sleep(40000);
        System.out.println("starting");
        long start = System.currentTimeMillis();
        Seq m = CandidateSolution.replicate(50, 1.0);
        for (int i = 0; i < 1000000; i++) {
            m = multiply(2.0, m.plus(CandidateSolution.solution(i, 50)));
        }
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        Double[] d = new Double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
                1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
                1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
                1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
                1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        for (int i = 0; i < 1000000; i++) {
            Double[]d1  = new Double[50];
            for (int j = 0; j < 50; j++) {
                d1[j] = 2 * (d[j] + i);
            }
//            m = multiply(2.0, m.plus(CandidateSolution.of(i, i)));
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}
