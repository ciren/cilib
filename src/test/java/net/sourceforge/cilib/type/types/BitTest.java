/*
 * BitTest.java
 * JUnit based test
 *
 * Created on January 21, 2003, 4:45 PM
 *
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;
import static org.junit.Assert.*;

import net.sourceforge.cilib.type.types.Bit;

/**
 *
 * @author Gary Pampara
 */
public class BitTest {
	
	@Test
	public void testClone() {
		Bit b = new Bit(false);
		Bit clone = (Bit) b.clone();
	
		assertNotSame(b, clone);
		assertTrue(b.equals(clone));
	}
	
	@Test
	public void testEquals() {
		Bit b1 = new Bit(false);
		Bit b2 = new Bit(false);
		Bit b3 = new Bit(true);
		Bit b4 = new Bit(true);
		
		assertTrue(b1.equals(b1));
		assertTrue(b2.equals(b2));
		assertTrue(b3.equals(b3));
		assertTrue(b4.equals(b4));
		
		assertTrue(b1.equals(b2));
		assertTrue(b3.equals(b4));
		assertFalse(b2.equals(b3));
		assertFalse(b1.equals(b4));
	}
	
	@Test
	public void testGet() {
		Bit b1 = new Bit(true);
		Bit b2 = new Bit(false);
		
		assertEquals(true, b1.getBit());
		assertEquals(false, b2.getBit());
	}
	
	@Test
	public void testSet() {
		Bit b1 = new Bit(true);
		Bit b2 = new Bit(false);
		
		b1.setBit(false);
		b2.setBit(true);
		
		assertEquals(false, b1.getBit());
		assertEquals(true, b2.getBit());		
	}
	
	@Test
	public void testCompareTo() {
		Bit b1 = new Bit(true);
		Bit b2 = new Bit(false);
		
		assertEquals(0, b1.compareTo(b1));
		assertEquals(-1, b2.compareTo(b1));
		assertEquals(1, b1.compareTo(b2));
	}
	
	@Test
	public void testRandomize() {
		Bit b1 = new Bit(true);
		Bit b2 = new Bit(true);
		b2.randomise();
		
		if (b2.getBit())
			assertTrue(b1.getBit() == b2.getBit());
		else
			assertTrue(b1.getBit() != b2.getBit());
	}
	
	@Test
	public void testSerialisation() {
		Bit b = new Bit();
		boolean target = b.getBit();
		
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			
			b.serialise(oos);
			oos.close();
			
			byte [] data = bos.toByteArray();
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			
			Bit result = new Bit();
			result.deserialise(ois);
			
			assertTrue(result instanceof Bit);
			assertEquals(target, ((Bit) result).getBit());
		}
		catch (IOException e) {
			fail("Serialisation fails for Types.Bit!");
		}
		catch (ClassNotFoundException c) {
			fail("ClassNotFound: Serialisation");
		}
	}
	
 }
