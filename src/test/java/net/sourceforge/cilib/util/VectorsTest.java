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

import net.sourceforge.cilib.type.types.BoundsFactory;
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
        vector = new Vector();

        for(int i = 1; i < 5; i++) {
            Numeric element = new Real(i);
            element.setBounds(BoundsFactory.create(i*-2, i*2));
            vector.append(element);
        }
    }

    @Test
    public void testUpperBounds() {
        int i = 1;
        for (Type element : Vectors.upperBoundVector(vector)) {
            Numeric numeric = (Numeric) element;
            assertTrue(Types.isInsideBounds(numeric));
            assertEquals(i++ * 2, numeric.getReal(), 0.0);
        }
    }

    @Test
    public void testLowerBounds() {
        int i = 1;
        for (Type element : Vectors.lowerBoundVector(vector)) {
            Numeric numeric = (Numeric) element;
            assertTrue(Types.isInsideBounds(numeric));
            assertEquals(i++ * -2, numeric.getReal(), 0.0);
        }
    }

    @Test
    public void vectorSum() {
        Vector v1 = Vectors.create(1.0);
        Vector v2 = Vectors.create(1.0);
        Vector v3 = Vectors.create(1.0);
        Vector v4 = Vectors.create(1.0);
        Vector v5 = Vectors.create(1.0);

        Vector result = Vectors.sumOf(v1, v2, v3, v4, v5);

        Assert.assertThat(result.getReal(0), is(5.0));
    }

}
