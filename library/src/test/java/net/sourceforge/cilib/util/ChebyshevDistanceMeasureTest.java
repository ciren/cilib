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

import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ChebyshevDistanceMeasureTest {

    @Test(expected=IllegalArgumentException.class)
    public void testVectorDistance() {
        DistanceMeasure distanceMeasure = new ChebyshevDistanceMeasure();

        Vector v1 = Vector.of(5,3,1);
        Vector v2 = Vector.of(1,3,5.5);
        assertEquals(4.5, distanceMeasure.distance(v1, v2), Double.MIN_NORMAL);

        v1 = Vector.newBuilder().copyOf(v1).add(Real.valueOf(22.0)).build();
        distanceMeasure.distance(v1, v2);
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

//    @Test
//    public void testSingleDimension() {
//        DistanceMeasure distanceMeasure = new ChebyshevDistanceMeasure();
//
//        List<Double> list1 = new ArrayList<Double>(1);
//        List<Double> list2 = new ArrayList<Double>(1);
//
//        list1.add(0.0);
//        list2.add(1.0);
//
//        assertEquals(1.0, distanceMeasure.distance(list1, list2), Double.MIN_NORMAL);
//    }
}
