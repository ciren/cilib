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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 *
 * @author Gary Pampara
 *
 */
public class NumericTest {

    @Test
    public void testDimension() {
        Real r = new Real();
        Int i = new Int();
        Bit b = new Bit();

        assertEquals(1, Types.getDimension(r));
        assertEquals(1, Types.getDimension(i));
        assertEquals(1, Types.getDimension(b));
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testLowerBound() {
        Real r = new Real(-30.0, 30.0);
        Int i = new Int(-3, 3);
        Bit b = new Bit();

        assertEquals(-30.0, r.getBounds().getLowerBound(), Double.MIN_NORMAL);
        assertEquals(-3.0, i.getBounds().getLowerBound(), Double.MIN_NORMAL);
        assertEquals(0.0, b.getBounds().getLowerBound(), Double.MIN_NORMAL);

        r.setBounds(0.0, r.getBounds().getUpperBound());
        i.setBounds(1.0, i.getBounds().getUpperBound());

        assertEquals(0.0, r.getBounds().getLowerBound(), Double.MIN_NORMAL);
        assertEquals(1.0, i.getBounds().getLowerBound(), Double.MIN_NORMAL);

        b.setBounds(-8.0, b.getBounds().getUpperBound());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testUpperBound() {
        Real r = new Real(-30.0, 30.0);
        Int i = new Int(-3, 3);
        Bit b = new Bit();

        assertEquals(30.0, r.getBounds().getUpperBound(), Double.MIN_NORMAL);
        assertEquals(3.0, i.getBounds().getUpperBound(), Double.MIN_NORMAL);
        assertEquals(1.0, b.getBounds().getUpperBound(), Double.MIN_NORMAL);

        r.setBounds(r.getBounds().getLowerBound(), 0.0);
        i.setBounds(i.getBounds().getLowerBound(), 1);

        assertEquals(0.0, r.getBounds().getUpperBound(), Double.MIN_NORMAL);
        assertEquals(1.0, i.getBounds().getUpperBound(), Double.MIN_NORMAL);

        b.setBounds(b.getBounds().getLowerBound(), 8.0);
    }

}
