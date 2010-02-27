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

import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;

/**
 * @author Olusegun Olorunda
 */
public class ChebyshevDistanceMeasureTest {

    @Test(expected=IllegalArgumentException.class)
    public void testVectorDistance() {
        DistanceMeasure distanceMeasure = new ChebyshevDistanceMeasure();
        Vector lhs = Vector.of(5.0, 3.0, 1.0);
        Vector rhs = Vector.of(1.0, 3.0, 5.5);

        assertThat(distanceMeasure.distance(lhs, rhs), closeTo(4.5, Maths.EPSILON));

        lhs = Vector.of(5.0, 3.0, 1.0, 22.0);
        distanceMeasure.distance(lhs, rhs);
    }

//    @Test(expected=IllegalArgumentException.class)
//    public void testCollectionDistance() {
//        DistanceMeasure distanceMeasure = new ChebyshevDistanceMeasure();
//
//        List<Double> l1 = new ArrayList<Double>();
//        List<Double> l2 = new ArrayList<Double>();
//
//        l1.add(5.0);
//        l1.add(3.0);
//        l1.add(1.0);
//
//        l2.add(1.0);
//        l2.add(3.0);
//        l2.add(5.0);
//
//        assertEquals(4.0, distanceMeasure.distance(l1, l2), Double.MIN_NORMAL);
//
//        l1.add(11.0);
//
//        distanceMeasure.distance(l1, l2);
//    }

    @Test
    public void testSingleDimension() {
        DistanceMeasure distanceMeasure = new ChebyshevDistanceMeasure();
        Vector lhs = Vector.of(0.0);
        Vector rhs = Vector.of(1.0);

        assertThat(distanceMeasure.distance(lhs, rhs), closeTo(1.0, Maths.EPSILON));
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testSetAlpha() {
        ChebyshevDistanceMeasure distanceMeasure = new ChebyshevDistanceMeasure();

        distanceMeasure.setAlpha(5);
    }
}
