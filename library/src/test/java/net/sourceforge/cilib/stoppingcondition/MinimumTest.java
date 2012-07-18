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
package net.sourceforge.cilib.stoppingcondition;

import static org.junit.Assert.*;
import org.junit.Test;

public class MinimumTest {
    /**
     * Test of getPercentage method, of class Maximum.
     */
    @Test
    public void testGetPercentage() {
        Minimum instance = new Minimum();
        assertEquals(0.0, instance.getPercentage(8.0, 0.0), 0.0);
        assertEquals(0.5, instance.getPercentage(4.0, 0.0), 0.0);
        assertEquals(0.75, instance.getPercentage(2.0, 0.0), 0.0);
        assertEquals(1.0, instance.getPercentage(0.0, 0.0), 0.0);
        assertEquals(1.0, instance.getPercentage(-1.0, 0.0), 0.0);
    }

    /**
     * Test of apply method, of class Maximum.
     */
    @Test
    public void testApply() {
        Minimum instance = new Minimum();
        assertFalse(instance.apply(1.0, 0.0));
        assertTrue(instance.apply(-1.0, 2.0));
        assertTrue(instance.apply(0.0, 2.0));
        assertTrue(instance.apply(1.0, 2.0));
        assertTrue(instance.apply(2.0, 2.0));
        assertFalse(instance.apply(3.0, 2.0));
    }
}
