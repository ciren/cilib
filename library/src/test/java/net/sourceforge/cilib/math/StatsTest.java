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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class StatsTest {

    private static ArrayList<Pattern> set;
    private static Vector mean = null;
    private static final int SIZE = 3;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Vector.Builder tmp = Vector.newBuilder();
        set = new ArrayList<Pattern>();

        for (int i = 1; i <= SIZE; i++) {
            tmp.add(Real.valueOf(i));
        }
        set.add(new Pattern("class0", tmp.build()));

        tmp = Vector.newBuilder();
        for (int i = SIZE; i > 0; i--) {
            tmp.add(Real.valueOf(i));
        }
        set.add(new Pattern("class1", tmp.build()));
        set.add(new Pattern("class2", Vector.of(1.0, 1.0, 1.0)));
        set.add(new Pattern("class1", Vector.of(2.0, 2.0, 2.0)));
        set.add(new Pattern("class0", Vector.of(3.0, 3.0, 3.0)));
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        set = null;
        mean = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptySetForMeanVector() {
        Stats.meanVector(new ArrayList<Pattern>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptySetForVarianceScalar() {
        Stats.variance(new ArrayList<Pattern>(), mean);
    }

    @Test
    public void testMeanVector() {
        Vector calculated = Vector.of(2.0, 2.0, 2.0);

        mean = Stats.meanVector(set);
        assertThat(mean, equalTo(calculated));
    }

    @Test
    public void testVarianceScalar() {
        assertThat(Stats.variance(set, mean), equalTo(1.2)); // more accurate than Stats.varianceVector()
    }
}
