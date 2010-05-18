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

import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

import org.junit.Test;

/**
 *
 * @author Gary Pampara
 */
public class RealTest {

    @Test
    public void cloning() {
        Real r = new Real(-10.0);
        Real test = r.getClone();

        assertEquals(r.getReal(), test.getReal(), Double.MIN_NORMAL);
        assertNotSame(r, test);
    }

    @Test
    public void equality() {
        Real i1 = new Real(10.0);
        Real i2 = new Real(10.0);
        Real i3 = new Real(-5.0);

        assertTrue(i1.equals(i1));
        assertTrue(i2.equals(i2));
        assertTrue(i3.equals(i3));

        assertTrue(i1.equals(i2));
        assertFalse(i1.equals(i3));
        assertTrue(i2.equals(i1));
        assertFalse(i2.equals(i3));
    }

    @Test
    public void hashValue() {
        Real r = new Real(-10.0, new Bounds(-30.0, 30.0));

        // This is the hasCode evaluation of the super classes bound information as well
        assertEquals(1195970368, r.hashCode());
    }

    @Test
    public void comparison() {
        Real r1 = new Real(15.0, new Bounds(0.0, 30.0));
        Real r2 = new Real(-15.0, new Bounds(-30.0, 0.0));

        assertEquals(0, r1.compareTo(r1));
        assertEquals(0, r2.compareTo(r2));
        assertEquals(1, r1.compareTo(r2));
        assertEquals(-1, r2.compareTo(r1));
    }

    @Test
    public void representation() {
        Real r = new Real(0.0, new Bounds(-30.0, 30.0));

        assertEquals("R(-30.0,30.0)", r.getRepresentation());
    }

    @Test
    public void randomizeWithinDefinedBounds() {
        Real r1 = new Real(0.0, new Bounds(-30.0, 30.0));
        Real r2 = r1.getClone();

        assertTrue(r1.getReal() == r2.getReal());
        r1.randomize(new MersenneTwister());
        assertTrue(r1.getReal() != r2.getReal());
    }

    @Test
    public void randomizeBetweenInfiniteBounds() {
        Real r = new Real(0.0);
        r.randomize(new MersenneTwister());
        Assert.assertThat(r.getReal(), not(equalTo(0.0)));
        Assert.assertThat(r.getReal(), not(equalTo(Double.NaN)));
    }

}
