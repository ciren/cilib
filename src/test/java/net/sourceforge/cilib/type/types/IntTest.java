/*
 * IntTest.java
 * 
 * Created on Jul 27, 2005
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

import net.sourceforge.cilib.type.DomainParser;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Vector;

/**
 * 
 * @author Gary Pampara
 */
public class IntTest {
	
	@Test
	public void testClone() {
		Int i = new Int();
		i.setInt(-10);
		
		Int clone = i.clone();
		
		assertEquals(i.getInt(), clone.getInt());
		assertNotSame(i, clone);
	}
	
	@Test
	public void testEquals() {
		Int i1 = new Int(10);
		Int i2 = new Int(10);
		Int i3 = new Int(-5);
		
		assertTrue(i1.equals(i1));
		assertTrue(i2.equals(i2));
		assertTrue(i3.equals(i3));
		
		assertTrue(i1.equals(i2));
		assertFalse(i1.equals(i3));
		assertTrue(i2.equals(i1));
		assertFalse(i2.equals(i3));
	}
	
	@Test
	public void testCompareTo() {
		Int i1 = new Int(0, 30);
		Int i2 = new Int(-30, 0);
		
		assertEquals(0, i1.compareTo(i1));
		assertEquals(0, i2.compareTo(i2));
		assertEquals(1, i1.compareTo(i2));
		assertEquals(-1, i2.compareTo(i1));
	}
	
	@Test
	public void testRandomize() {
		Int i1 = new Int(-300, 300);
		Int i2 = i1.clone();
		
		assertTrue(i1.getInt() == i2.getInt());
		i1.randomise();
		assertTrue(i1.getInt() != i2.getInt());
	}
	
	@Test
	public void testSerialisation() {
		Int r = new Int();
		int target = r.getInt();
			
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			
			r.serialise(oos);
			oos.close();
			
			byte [] data = bos.toByteArray();
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			
			Int result = new Int();
			result.deserialise(ois);
			
			assertTrue(result instanceof Int);
			assertEquals(target, ((Int) result).getInt());
		}
		catch (IOException e) {
			fail("Serialisation fails for Types.Int!");
		}
		catch (ClassNotFoundException c) {
			fail();
		}
	}
	
	@Test
	public void testCiclops() {
		DomainParser parser = DomainParser.getInstance();
		parser.parse("Z");
		
		Vector t = (Vector) parser.getBuiltRepresentation();
		t.setReal(0, -5);
		
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			
			t.serialise(oos);
			oos.close();
			
			byte [] data = bos.toByteArray();
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			
			Int result = new Int();
			result.deserialise(ois);
			
			assertTrue(result instanceof Int);
			assertEquals(-5, ((Int) result).getInt());
		}
		catch (IOException e) {
			fail("Serialisation fails for Types.Int!");
		}
		catch (ClassNotFoundException c) {
			fail();
		}
	}

}
