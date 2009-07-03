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
package net.sourceforge.cilib.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Test;

/**
 * @author Theuns Cloete
 */
public class MinkowskiMetricTest {

    @Test(expected = IllegalArgumentException.class)
    public void testVectorDistance() {
        DistanceMeasure distanceMeasure = new MinkowskiMetric(3);

        Vector v1 = new Vector();
        Vector v2 = new Vector();

        v1.add(new Real(4.0));
        v1.add(new Real(3.0));
        v1.add(new Real(2.0));

        v2.add(new Real(2.0));
        v2.add(new Real(3.0));
        v2.add(new Real(4.0));

        assertEquals(Math.pow(16, 1/3.0), distanceMeasure.distance(v1, v2), Double.MIN_NORMAL);

        v1.add(new Real(22.0));

        distanceMeasure.distance(v1, v2);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testCollectionDistance() {
        DistanceMeasure distanceMeasure = new MinkowskiMetric(3);

        List<Double> l1 = new ArrayList<Double>();
        List<Double> l2 = new ArrayList<Double>();

        l1.add(4.0);
        l1.add(3.0);
        l1.add(2.0);

        l2.add(2.0);
        l2.add(3.0);
        l2.add(4.0);

        assertEquals(Math.pow(16, 1/3.0), distanceMeasure.distance(l1, l2), Double.MIN_NORMAL);

        l1.add(11.0);

        distanceMeasure.distance(l1, l2);
    }

    @Test
    public void testSingleDimension() {
        DistanceMeasure distanceMeasure = new MinkowskiMetric(3);

        List<Double> list1 = new ArrayList<Double>(1);
        List<Double> list2 = new ArrayList<Double>(1);

        list1.add(0.0);
        list2.add(1.0);

        assertEquals(1.0, distanceMeasure.distance(list1, list2), Double.MIN_NORMAL);
    }
}
