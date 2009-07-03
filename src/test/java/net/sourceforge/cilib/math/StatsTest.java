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
        Vector tmp = new Vector(SIZE);
        set = new ArrayList<Pattern>();

        for (int i = 1; i <= SIZE; i++) {
            tmp.add(new Real(i));
        }
        set.add(new Pattern("class0", tmp));

        tmp = new Vector(SIZE);
        for (int i = SIZE; i > 0; i--) {
            tmp.add(new Real(i));
        }
        set.add(new Pattern("class1", tmp));

        tmp = new Vector(SIZE, new Real(1));
        set.add(new Pattern("class2", tmp));

        tmp = new Vector(SIZE, new Real(2));
        set.add(new Pattern("class1", tmp));

        tmp = new Vector(SIZE, new Real(3));
        set.add(new Pattern("class0", tmp));
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
        Vector calculated = new Vector(SIZE, new Real(2));

        mean = Stats.meanVector(set);
        assertThat(mean, equalTo(calculated));
    }

    @Test
    public void testVarianceScalar() {
        assertThat(Stats.variance(set, mean), equalTo(1.2));    // more accurate than Stats.varianceVector()
    }
}
