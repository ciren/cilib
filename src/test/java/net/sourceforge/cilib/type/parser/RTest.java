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

import net.sourceforge.cilib.type.types.Bounds;
import static org.junit.Assert.assertTrue;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Gary Pampara
 */
public class RTest {

    private static R creator = null;

    @BeforeClass
    public static void setUp() {
        creator = new R();
    }

    @Test
    public void testCreateNoBounds() {
        Type r = creator.create(new Bounds(0, 2));

        assertTrue(r instanceof Real);
    }

    @Test
    public void testCreateBounds() {
        Type r = creator.create(new Bounds(0, 2));

        assertTrue(r instanceof Real);
    }

}
