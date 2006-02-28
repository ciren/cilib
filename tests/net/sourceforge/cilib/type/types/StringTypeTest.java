/*
 * StringTypeTest.java
 * 
 * Created on Oct 18, 2005
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
package net.sourceforge.cilib.type.types;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.sourceforge.cilib.type.types.StringType;

import junit.framework.TestCase;


/**
 * 
 * @author Gary Pampara
 */
public class StringTypeTest extends TestCase {
	
	public void testClone() {
		StringType t = new StringType("test string");
		StringType clone = t.clone();
		
		assertTrue(t.getString().equals(clone.getString()));
		assertEquals(t.getString(), clone.getString());
	}
	
	
	public void testDimensionality() {
		StringType s = new StringType("This is a StringType");
		
		assertEquals(1, s.getDimension());
	}
	
	/**
	 * 
	 *
	 */
	public void testSerialisation() {
		StringType testString = new StringType();
		testString.setString("This is a test string");
		String target = new String(testString.getString());
		
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			
			testString.serialise(oos);
			oos.close();
			
			byte [] data = bos.toByteArray();
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			
			StringType result = new StringType();
			result.deserialise(ois);
			
			assertTrue(result instanceof StringType);
			assertEquals(target, ((StringType) result).getString());
		}
		catch (IOException e) {
			fail("Serialisation fails for Types.StringType!");
		}
		catch (ClassNotFoundException c) {
			fail();
		}
	}

}
