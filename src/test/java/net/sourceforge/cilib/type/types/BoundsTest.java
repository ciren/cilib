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
package net.sourceforge.cilib.type.types;

import net.sourceforge.cilib.math.Maths;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class BoundsTest {

    @Test
    public void createNumeric() {
        Real r1 = new Real();
        Real r2 = new Real();

        Assert.assertSame(r1.getBounds(), r2.getBounds());
    }

    @Test
    public void equals() {
        Bounds b1 = BoundsFactory.create(0.0, 2.0);
        Bounds b2 = BoundsFactory.create(0.0, 2.0);

        Assert.assertEquals(b1, b2);
        Assert.assertSame(b1, b2);
    }

    @Test
    public void boundsEdgeCases() {
        Bounds b1 = BoundsFactory.create(0.0, 2.0);
        Assert.assertTrue(b1.isInsideBounds(2.0));
        Assert.assertTrue(b1.isInsideBounds(0.0));

        Assert.assertFalse(b1.isInsideBounds(2.0 + Maths.EPSILON));
        Assert.assertFalse(b1.isInsideBounds(0.0 - Maths.EPSILON));
    }

}
