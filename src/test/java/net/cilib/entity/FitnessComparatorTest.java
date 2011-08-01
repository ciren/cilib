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

import fj.*;
import fj.data.List;
import fj.data.Option;
import fj.function.Doubles;
import net.cilib.problem.Benchmarks;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 * @author gpampara
 */
public class FitnessComparatorTest {

    @Test
    public void otherObjectFitnessIsInferiror() {
        Option<Double> valid = Option.some(1.0);
        Option<Double> invalid = Option.<Double>none();
        FitnessComparator comparator = FitnessComparator.MIN;

        Assert.assertThat(comparator.compare(valid, invalid), is(Ordering.LT));
    }

    @Test
    public void currentObjectFitnessIsInferior() {
        Option<Double> valid = Option.some(1.0);
        Option<Double> invalid = Option.<Double>none();
        FitnessComparator comparator = FitnessComparator.MIN;

        Assert.assertThat(comparator.compare(invalid, valid), is(Ordering.GT));
    }

    /**
     * When both fitness values are inferior
     */
    @Test
    public void bothOptionValuesAreNoneMin() {
        Option<Double> a = Option.<Double>none();
        Option<Double> b = Option.<Double>none();
        FitnessComparator comparator = FitnessComparator.MIN;

        Assert.assertEquals(Ordering.EQ, comparator.compare(a, b));
    }

    @Test
    public void bothOptionValuesAreNoneMax() {
        Option<Double> a = Option.<Double>none();
        Option<Double> b = Option.<Double>none();
        FitnessComparator comparator = FitnessComparator.MAX;

        Assert.assertEquals(Ordering.EQ, comparator.compare(a, b));
    }
}