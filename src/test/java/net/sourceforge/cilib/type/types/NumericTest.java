/*
 * NumericTest.java
 * 
 * Created on Feb 25, 2006
 *
 * Copyright (C) 2003 - 2006 
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
 *
 */
package net.sourceforge.cilib.type.types;

import org.junit.Test;
import static org.junit.Assert.*;

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
		
		assertEquals(1, r.getDimension());
		assertEquals(1, i.getDimension());
		assertEquals(1, b.getDimension());
	}
	
	@Test
	public void testLowerBound() {
		Real r = new Real(-30.0, 30.0);
		Int i = new Int(-3, 3);
		Bit b = new Bit();
		
		assertEquals(-30.0, r.getLowerBound(), Double.MIN_NORMAL);
		assertEquals(-3.0, i.getLowerBound(), Double.MIN_NORMAL);
		assertEquals(0.0, b.getLowerBound(), Double.MIN_NORMAL);
		
		r.setLowerBound(0.0);
		i.setLowerBound(1);
		
		assertEquals(0.0, r.getLowerBound(), Double.MIN_NORMAL);
		assertEquals(1.0, i.getLowerBound(), Double.MIN_NORMAL);
		
		try {
			b.setLowerBound(-8.0);
			fail("Error!! Bit values cannot have their lowerBound adjusted!");
		}
		catch (Exception e) {
		}
	}
	
	@Test
	public void testUpperBound() {
		Real r = new Real(-30.0, 30.0);
		Int i = new Int(-3, 3);
		Bit b = new Bit();
		
		assertEquals(30.0, r.getUpperBound(), Double.MIN_NORMAL);
		assertEquals(3.0, i.getUpperBound(), Double.MIN_NORMAL);
		assertEquals(1.0, b.getUpperBound(), Double.MIN_NORMAL);
		
		r.setUpperBound(0.0);
		i.setUpperBound(1);
		
		assertEquals(0.0, r.getUpperBound(), Double.MIN_NORMAL);
		assertEquals(1.0, i.getUpperBound(), Double.MIN_NORMAL);
		
		try {
			b.setUpperBound(8.0);
			fail("Error!! Bit values cannot have their lowerBound adjusted!");
		}
		catch (Exception e) {
		}
	}

}
