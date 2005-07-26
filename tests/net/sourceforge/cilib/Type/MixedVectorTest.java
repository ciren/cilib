/*
 * MixedVectorTest.java
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

import junit.framework.TestCase;
import net.sourceforge.cilib.Type.Types.MixedVector;
import net.sourceforge.cilib.Type.Types.Real;
import net.sourceforge.cilib.Type.Types.Vector;

public class MixedVectorTest extends TestCase {
	
	private Vector vector;
	private Vector tmpVector;
	
	public void setUp() {
		vector = new MixedVector();
		
		vector.append(new Real(1));
		vector.append(new Real(2));
		vector.append(new Real(3));
		vector.append(new Real(4));
	}
	
	public void tearDown() {
		vector = null;
		tmpVector = null;
	}
	
	private void recreateTmpVector() {
		tmpVector = new MixedVector();
		tmpVector.append(new Real(1.0));
		tmpVector.append(new Real(2.0));
		tmpVector.append(new Real(3.0));
	}
	
	public void testClone() {
		Vector v = null;
		
		try {
			v = (Vector) vector.clone();
		}
		catch (CloneNotSupportedException c) {
			fail();
		}
		
		for (int i = 0; i < vector.getDimension(); i++) {
			assertEquals(vector.getReal(i), v.getReal(i));
			assertNotSame(vector.get(i), v.get(i));
		}
	}
	
	public void testSet() {
		vector.setReal(0, 3.0);
		assertEquals(3.0, vector.getReal(0));
		vector.setReal(0, 1.0);
		assertEquals(1.0, vector.getReal(0));
	}
	
	public void testGet() {
		recreateTmpVector();
		
		assertEquals(1.0, tmpVector.getReal(0));
		assertEquals(2.0, tmpVector.getReal(1));
		assertEquals(3.0, tmpVector.getReal(2));
		
		Real t = (Real) tmpVector.get(0);
		
		assertEquals(1.0, t.getReal());
	}

}
