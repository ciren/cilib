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

import fj.F3;
import fj.F2;
import fj.Monoid;
import net.cilib.problem.Benchmarks;
import net.cilib.problem.Evaluatable;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static net.cilib.predef.Predef.solution;

/**
 *
 * @author gpampara
 */
public class FitnessProviderTest {
    @Test
    public void testFinalize() {
        FitnessProvider provider = new FitnessProvider(Evaluatable.lift(Benchmarks.square, Monoid.doubleAdditionMonoid));

        Assert.assertThat(provider.evaluate(solution(1.0, 2.0)).some(), equalTo(5.0));
    }

    @Test
    public void curriedF2() {
        F2<Double, Double, Double> f = new F2<Double, Double, Double>() {
            @Override
            public Double f(Double a, Double b) {
                return a + b;
            }
        };
        FitnessProvider provider = new FitnessProvider(Evaluatable.lift(f, Monoid.doubleAdditionMonoid));

        Assert.assertThat(provider.evaluate(solution(1.0, 2.0)).some(), equalTo(3.0));
    }

    @Test
    public void curriedF3() {
        F3<Double, Double, Double, Double> f = new F3<Double, Double, Double, Double>() {
            @Override
            public Double f(Double a, Double b, Double c) {
                return a + b + c;
            }
        };
        FitnessProvider provider = new FitnessProvider(Evaluatable.lift(f, Monoid.doubleAdditionMonoid));

        Assert.assertThat(provider.evaluate(solution(1.0, 2.0, 3.0)).some(), equalTo(6.0));
    }
}