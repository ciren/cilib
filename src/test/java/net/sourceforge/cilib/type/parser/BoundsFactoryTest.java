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
package net.sourceforge.cilib.type.parser;

import net.sourceforge.cilib.type.types.*;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class BoundsFactoryTest {

    @Test
    public void creation() {
        BoundsFactory factory = new BoundsFactory();
        Bounds b1 = factory.create(-1.0, 1.0);

        Assert.assertEquals(-1.0, b1.getLowerBound(), 0.0);
        Assert.assertEquals(1.0, b1.getUpperBound(), 0.0);
    }

    @Test
    public void returnSameInstance() {
        BoundsFactory factory = new BoundsFactory();
        Bounds b1 = factory.create(-1.0, 1.0);
        Bounds b2 = factory.create(-1.0, 1.0);

        Assert.assertSame(b1, b2);
        Assert.assertEquals(b1, b2);
    }

}
