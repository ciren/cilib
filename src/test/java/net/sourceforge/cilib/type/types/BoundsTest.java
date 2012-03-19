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
package net.sourceforge.cilib.type.types;

import net.sourceforge.cilib.math.Maths;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class BoundsTest {

    @Test
    public void equals() {
        Bounds b1 = new Bounds(0.0, 2.0);
        Bounds b2 = new Bounds(0.0, 2.0);

        Assert.assertEquals(b1, b2);
    }

    @Test
    public void boundsEdgeCases() {
        Bounds b1 = new Bounds(0.0, 2.0);
        Assert.assertTrue(b1.isInsideBounds(2.0));
        Assert.assertTrue(b1.isInsideBounds(0.0));

        Assert.assertFalse(b1.isInsideBounds(2.0 + Maths.EPSILON));
        Assert.assertFalse(b1.isInsideBounds(0.0 - Maths.EPSILON));
    }

    @Test(expected=IllegalArgumentException.class)
    public void invalidLowerBound() {
        new Bounds(7.0, 4.0);
    }

}
