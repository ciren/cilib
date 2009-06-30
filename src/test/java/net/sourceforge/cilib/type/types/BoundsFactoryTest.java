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

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class BoundsFactoryTest {

    @Test
    public void creation() {
        Bounds b1 = BoundsFactory.create(-1.0, 1.0);

        Assert.assertEquals(-1.0, b1.getLowerBound(), 0.0);
        Assert.assertEquals(1.0, b1.getUpperBound(), 0.0);
    }

    @Test
    public void returnSameInstance() {
        Bounds b1 = BoundsFactory.create(-1.0, 1.0);
        Bounds b2 = BoundsFactory.create(-1.0, 1.0);

        Assert.assertSame(b1, b2);
        Assert.assertEquals(b1, b2);
    }

}
