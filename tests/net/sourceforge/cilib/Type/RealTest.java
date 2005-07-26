/*
 * RealTest.java
 * 
 * Created on Jul 19, 2005
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
package net.sourceforge.cilib.Type;

import net.sourceforge.cilib.Type.Types.Real;
import junit.framework.TestCase;

public class RealTest extends TestCase {
	
	public void testClone() {
		Real r = new Real();
		r.setReal(-10.0);
		
		Real test = null;
		
		try {
			test = (Real) r.clone();
		}
		catch (CloneNotSupportedException c) {
			fail();
		}
		
		assertEquals(r.getReal(), test.getReal());
		assertNotSame(r, test);
	}
	
	public void testCompareTo() {
		Real r1 = new Real(0.0, 30.0);
		Real r2 = new Real(-30.0, 0.0);
		
		assertEquals(0, r1.compareTo(r1));
		assertEquals(0, r2.compareTo(r2));
		assertEquals(1, r1.compareTo(r2));
		assertEquals(-1, r2.compareTo(r1));
	}
	
	public void testRandomize() {
		Real r1 = new Real(-30.0, 30.0);
		Real r2 = null;
		
		try {
			r2 = (Real) r1.clone();
		}
		catch (CloneNotSupportedException clone) {
			fail();
		}
		
		assertTrue(r1.getReal() == r2.getReal());
		r1.randomize();
		assertTrue(r1.getReal() != r2.getReal());
	}
	
}
