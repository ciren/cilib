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
package net.sourceforge.cilib.entity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HarmonyTest {

    @Test
    public void equals() {
        Harmony h1 = new Harmony();
        Harmony h2 = new Harmony();

        assertFalse(h1.equals(h2));
        assertFalse(h2.equals(h1));
        assertTrue(h1.equals(h1));

        assertFalse(h1.equals(null));
    }

    @Test
    public void hashCodes() {
        Harmony h1 = new Harmony();
        Harmony h2 = new Harmony();

        assertTrue(h1.hashCode() != h2.hashCode());
    }
}
