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
package net.sourceforge.cilib.type.creator;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Gary Pampara
 */
public class TTest {

    private static T creator = null;

    @BeforeClass
    public static void setUp() {
        creator = new T();
    }

    @Test
    public void testCreateNoBounds() {
        Type t = creator.create();

        assertTrue(t instanceof StringType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateBounds() {
        Type t = creator.create(0, 3);
        assertTrue(t == null);
    }

}
