/*
 * BinaryAdapterTest.java
 * 
 * Created on Feb 28, 2006
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
package net.sourceforge.cilib.functions.discrete;

import junit.framework.TestCase;
import net.sourceforge.cilib.functions.continuous.Rastrigin;
import net.sourceforge.cilib.type.types.Bit;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Vector;

/**
 * 
 * @author Gary Pampara
 *
 */
public class BinaryAdapterTest extends TestCase {
	
	private BinaryAdapter adapter;
	
	public void setUp() {
		this.adapter = new BinaryAdapter();
		this.adapter.setFunction(new Rastrigin());
	}
	
	/**
	 * Test the process of setting the bits per dimension.
	 */
	public void testDimensionSettings() {
		this.adapter.setBitsPerDimension(5);
		assertEquals(5, this.adapter.getBitsPerDimension());
		
		this.adapter.setBitsPerDimension(0);
		assertEquals(0, this.adapter.getBitsPerDimension());
		
		try {
			this.adapter.setBitsPerDimension(-9);
			fail("Cannot use negative bits!!");
		}
		catch (Exception e) {}
	}
	
	
	/**
	 * Test the process of creating a bit vector and decoding it into real values.
	 */
	public void testSimpleDecoding() {
		this.adapter.setBitsPerDimension(4);
		
		Vector bitVector = new MixedVector();
		for (int i = 0; i < 4; i++) {
			bitVector.add(new Bit(true));
		}
		
		assertEquals(4, bitVector.getDimension());
		
		Vector converted = this.adapter.decodeBitString(bitVector);	
		assertEquals(1, converted.getDimension());
		
		Type t = converted.get(0);
		assertTrue(t instanceof Numeric);
		Numeric n = (Numeric) t;
		assertEquals(15.0, n.getReal());

		bitVector.setBit(3, false);
		converted = this.adapter.decodeBitString(bitVector);
		assertEquals(1, converted.getDimension());
		assertEquals(14.0, converted.getReal(0));
		
		bitVector.setBit(2, false);
		converted = this.adapter.decodeBitString(bitVector);
		assertEquals(1, converted.getDimension());
		assertEquals(12.0, converted.getReal(0));
		
		bitVector.setBit(1, false);
		converted = this.adapter.decodeBitString(bitVector);
		assertEquals(1, converted.getDimension());
		assertEquals(8.0, converted.getReal(0));
		
		bitVector.setBit(0, false);
		converted = this.adapter.decodeBitString(bitVector);
		assertEquals(1, converted.getDimension());
		assertEquals(0.0, converted.getReal(0));
	}
	
	
	public void testSimpleDoubleDecoding() {
		this.adapter.setBitsPerDimension(4);
		
		Vector bitVector = new MixedVector();
		for (int i = 0; i < 8; i++) {
			bitVector.add(new Bit(true));
		}
		
		assertEquals(8, bitVector.getDimension());
		
		Vector converted = this.adapter.decodeBitString(bitVector);	
		assertEquals(2, converted.getDimension());
		
		Type t = converted.get(0);
		assertTrue(t instanceof Numeric);
		Numeric n = (Numeric) t;
		assertEquals(15.0, n.getReal());

		bitVector.setBit(3, false);
		converted = this.adapter.decodeBitString(bitVector);
		assertEquals(2, converted.getDimension());
		assertEquals(14.0, converted.getReal(0));
		
		bitVector.setBit(2, false);
		converted = this.adapter.decodeBitString(bitVector);
		assertEquals(2, converted.getDimension());
		assertEquals(12.0, converted.getReal(0));
		
		bitVector.setBit(1, false);
		converted = this.adapter.decodeBitString(bitVector);
		assertEquals(2, converted.getDimension());
		assertEquals(8.0, converted.getReal(0));
		
		bitVector.setBit(0, false);
		converted = this.adapter.decodeBitString(bitVector);
		assertEquals(2, converted.getDimension());
		assertEquals(0.0, converted.getReal(0));
	}
	
	public void testComplex() {
		this.adapter.setBitsPerDimension(8);
		
		Vector bitVector = new MixedVector();
		for (int i = 0; i < 8; i++) {
			bitVector.add(new Bit(true));
		}
		
		assertEquals(8, bitVector.getDimension());
		
		Vector converted = this.adapter.decodeBitString(bitVector);	
		assertEquals(1, converted.getDimension());
		
		Type t = converted.get(0);
		assertTrue(t instanceof Numeric);
		Numeric n = (Numeric) t;
		assertEquals(255.0, n.getReal());

		bitVector.setBit(3, false);
		converted = this.adapter.decodeBitString(bitVector);
		assertEquals(1, converted.getDimension());
		assertEquals(239.0, converted.getReal(0));
		
		bitVector.setBit(2, false);
		converted = this.adapter.decodeBitString(bitVector);
		assertEquals(1, converted.getDimension());
		assertEquals(207.0, converted.getReal(0));
		
		bitVector.setBit(1, false);
		converted = this.adapter.decodeBitString(bitVector);
		assertEquals(1, converted.getDimension());
		assertEquals(143.0, converted.getReal(0));
		
		bitVector.setBit(0, false);
		converted = this.adapter.decodeBitString(bitVector);
		assertEquals(1, converted.getDimension());
		assertEquals(15.0, converted.getReal(0));
	}
	
	
	/**
	 * Cleanup
	 */
	public void tearDown() {
		this.adapter = null;
	}

}
