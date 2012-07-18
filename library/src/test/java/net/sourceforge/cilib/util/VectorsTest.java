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

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.type.types.Bounds;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Types;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;

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

        Vector result1 = Vectors.sumOf(v1, v2, v3, v4, v5);
        Vector result2 = Vectors.sumOf(Arrays.asList(v1, v2, v3, v4, v5));

        Assert.assertThat(result1.doubleValueOf(0), is(5.0));
        Assert.assertThat(result2.doubleValueOf(0), is(5.0));
    }

    @Test
    public void vectorMean() {
        Vector v1 = Vector.of(1.0);
        Vector v2 = Vector.of(1.0);
        Vector v3 = Vector.of(1.0);
        Vector v4 = Vector.of(1.0);
        Vector v5 = Vector.of(1.0);

        Vector result1 = Vectors.mean(v1, v2, v3, v4, v5);
        Vector result2 = Vectors.mean(Arrays.asList(v1, v2, v3, v4, v5));

        Assert.assertThat(result1.doubleValueOf(0), is(1.0));
        Assert.assertThat(result2.doubleValueOf(0), is(1.0));
    }
    
    @Test
    public void testOrthonormalize() {
        List<Vector> vectors = Arrays.asList(Vector.of(3.0, 1.0), Vector.of(2.0, 2.0));
        List<Vector> ortho = Vectors.orthonormalize(vectors);
        
        assertEquals(ortho.get(0).doubleValueOf(0), 3.0 / Math.sqrt(10), 0.00000001);
        assertEquals(ortho.get(0).doubleValueOf(1), 1.0 / Math.sqrt(10), 0.00000001);
        assertEquals(ortho.get(1).doubleValueOf(0), -2.0 / 5.0 / Math.sqrt(40.0 / 25.0), 0.00000001);
        assertEquals(ortho.get(1).doubleValueOf(1), 6.0 / 5.0 / Math.sqrt(40.0 / 25.0), 0.00000001);
    }
}
