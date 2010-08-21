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
package net.sourceforge.cilib.math;

import com.google.common.collect.Sets;

import java.util.Set;

import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

/**
 * @author Theuns Cloete
 */
public class StatsTest {
    private static Set<StandardPattern> set;
    private static Vector mean;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        set = Sets.newHashSet();
        set.add(new StandardPattern(Vector.of(1.0, 2.0, 3.0), new StringType("class0")));
        set.add(new StandardPattern(Vector.of(3.0, 2.0, 1.0), new StringType("class1")));
        set.add(new StandardPattern(Vector.of(1.0, 1.0, 1.0), new StringType("class2")));
        set.add(new StandardPattern(Vector.of(2.0, 2.0, 2.0), new StringType("class1")));
        set.add(new StandardPattern(Vector.of(3.0, 3.0, 3.0), new StringType("class0")));
        mean = Vector.of(2.0, 2.0, 2.0);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        set = null;
        mean = null;
    }

    @Test(expected = IllegalStateException.class)
    public void testEmptySetForMeanVector() {
        Stats.meanVector(Sets.<StandardPattern>newHashSet());
    }

    @Test(expected = IllegalStateException.class)
    public void testEmptySetForVarianceScalar() {
        Stats.variance(Sets.<StandardPattern>newHashSet(), mean);
    }

    @Test
    public void testMeanVector() {
        Vector calculated = Stats.meanVector(set);

        assertThat(calculated, equalTo(mean));
    }

    @Test
    public void testVarianceScalar() {
        assertThat(Stats.variance(set, mean), closeTo(1.2, Maths.EPSILON));
    }

    @Test
    public void testVarianceVector() {
        Vector varianceVector = Stats.varianceVector(set, mean);

        assertThat(varianceVector.doubleValueOf(0), closeTo(0.8, Maths.EPSILON));
        assertThat(varianceVector.doubleValueOf(1), closeTo(0.4, Maths.EPSILON));
        assertThat(varianceVector.doubleValueOf(2), closeTo(0.8, Maths.EPSILON));
        assertThat(varianceVector.norm(), closeTo(1.2, Maths.EPSILON));
    }

    @Test
    public void testStdDeviationScalar() {
        assertThat(Stats.stdDeviation(set, mean), closeTo(1.0954451150103323, Maths.EPSILON));
    }

    @Test
    public void testStdDeviationVector() {
        Vector stdDeviationVector = Stats.stdDeviationVector(set, mean);

        assertThat(stdDeviationVector.doubleValueOf(0), closeTo(0.894427190999916, Maths.EPSILON));
        assertThat(stdDeviationVector.doubleValueOf(1), closeTo(0.632455532033676, Maths.EPSILON));
        assertThat(stdDeviationVector.doubleValueOf(2), closeTo(0.894427190999916, Maths.EPSILON));
    }
}
