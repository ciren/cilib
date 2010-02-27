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
package net.sourceforge.cilib.util;

import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Types;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class VectorsTest {

    private Vector vector;

    @Before
    public void initialise() {
        Vector.Builder vectorBuilder = Vector.newBuilder();

        for(int i = 1; i < 5; i++) {
            Numeric element = Real.valueOf(i, new Bounds(i*-2, i*2));
            vectorBuilder.add(element);
        }

        vector = vectorBuilder.build();
    }

    @Test
    public void testUpperBounds() {
        int i = 1;
        for (Type element : Vectors.upperBoundVector(vector)) {
            Numeric numeric = (Numeric) element;
            assertTrue(Types.isInsideBounds(numeric));
            assertEquals(i++ * 2, numeric.doubleValue(), 0.0);
        }
    }

    @Test
    public void testLowerBounds() {
        int i = 1;
        for (Type element : Vectors.lowerBoundVector(vector)) {
            Numeric numeric = (Numeric) element;
            assertTrue(Types.isInsideBounds(numeric));
            assertEquals(i++ * -2, numeric.doubleValue(), 0.0);
        }
    }

    @Test
    public void vectorSum() {
        Vector v1 = Vector.of(1.0);
        Vector v2 = Vector.of(1.0);
        Vector v3 = Vector.of(1.0);
        Vector v4 = Vector.of(1.0);
        Vector v5 = Vector.of(1.0);

        Vector result = Vectors.sumOf(v1, v2, v3, v4, v5);

        Assert.assertThat(result.doubleValueOf(0), is(5.0));
    }

    @Test
    public void testToString() {
        assertThat(Vectors.toString(vector, "[", "]", ","), is("[1.0,2.0,3.0,4.0]"));
        assertThat(Vectors.toString(vector, "", "", "\t"), is("1.0\t2.0\t3.0\t4.0"));
    }

    @Test
    public void testJitter() {
        Vector jittered = Vectors.jitter(vector, 0.01);

        assertThat(jittered.size(), is(vector.size()));
        assertThat(jittered.doubleValueOf(0), closeTo(1.0, 0.04));
        assertThat(jittered.doubleValueOf(1), closeTo(2.0, 0.08));
        assertThat(jittered.doubleValueOf(2), closeTo(3.0, 1.02));
        assertThat(jittered.doubleValueOf(3), closeTo(4.0, 1.06));

        jittered = Vectors.jitter(vector, 0.001);
        assertThat(jittered.size(), is(vector.size()));
        assertThat(jittered.doubleValueOf(0), closeTo(1.0, 0.004));
        assertThat(jittered.doubleValueOf(1), closeTo(2.0, 0.008));
        assertThat(jittered.doubleValueOf(2), closeTo(3.0, 1.002));
        assertThat(jittered.doubleValueOf(3), closeTo(4.0, 1.006));
    }

    @Test
    public void testJitterZeroRatio() {
        Vector jittered = Vectors.jitter(vector, 0.0);

        assertThat(jittered.size(), is(vector.size()));
        assertThat(jittered.doubleValueOf(0), is(1.0));
        assertThat(jittered.doubleValueOf(1), is(2.0));
        assertThat(jittered.doubleValueOf(2), is(3.0));
        assertThat(jittered.doubleValueOf(3), is(4.0));
    }

    @Test
    public void testZeroVector() {
        Vector zeroed = Vectors.zeroVector(vector);

        assertThat(zeroed.size(), is(vector.size()));

        for (Numeric numeric : zeroed) {
            assertThat(numeric.doubleValue(), is(0.0));
        }
    }
}
