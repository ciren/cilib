/*
 * BitVectorTest.java
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

package net.sourceforge.cilib.type;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @deprecated
 * @author Edwin Peer
 */
@Deprecated
public class BitVectorTest extends TestCase {
    
    public BitVectorTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(BitVectorTest.class);
        
        return suite;
    }
       
    public void testClone() {
 /*   	try {
    		Random r = new Random();
        	
    		BitVector tmp = new BitVector(100);
    		for (int i = 0; i < 100; ++i) {
    			tmp.setBit(i, r.nextBoolean());
    		}
    		
    		BitVector clone = (BitVector) tmp.clone();
    		assertNotSame(tmp, clone);
    		assertTrue(tmp.equals(clone));
    		
    		for (int i = 0; i < 100; ++i) {
    			tmp.setBit(i, r.nextBoolean());
    		}
    		
    		assertFalse(tmp.equals(clone));
    	}
    	catch (CloneNotSupportedException ex) {
    		fail("Failed to clone Bit");
    	}*/
    }
    
    public void testEquals() {
    	/*Random r = new Random();
    	
    	BitVector a = new BitVector(100);
    	BitVector b = new BitVector(100);
    	BitVector different = new BitVector(100);
    	BitVector shorter = new BitVector(50);
    	BitVector longer = new BitVector(150);
    	
    	for (int i = 0; i < 150; ++i) {
    		boolean tmp = r.nextBoolean();
    		if (i < 100) {
    			if (i < 50) {
    				shorter.setBit(i, tmp);
    			}
    			a.setBit(i, tmp);
    			b.setBit(i, tmp);
    			different.setBit(i, r.nextBoolean());
    		}
    		longer.setBit(i, tmp);
    	}
    	
    	assertTrue(different.equals(different));
    	
    	assertTrue(a.equals(b));
    	assertTrue(b.equals(a));
    	
    	assertFalse(a.equals(different));
    	assertFalse(different.equals(a));
    	
    	assertFalse(a.equals(longer));
    	assertFalse(a.equals(shorter));
    	
    	assertFalse(different.equals(null));*/
    }
    
    public void testHashCode() {
/*    	Random r = new Random();
    	BitSet template = new BitSet();
    	BitVector tmp = new BitVector(100);
    	
    	for (int i = 0; i < 100; ++i) {
    		boolean rtmp = r.nextBoolean(); 
    		template.set(i, rtmp);
    		tmp.setBit(i, rtmp);
    	}
    	// Underlying BitSet implementation uses last bit to denote the dimension 
    	// Probably shouldn't assume this implentation detail (or even that it is an underlying BitSet for that matter)
    	// At least this test will probably fail if the underlying implentation does change and it can be fixed then - for now this is fine
    	template.set(100, true); 
    	
    	assertEquals(template.hashCode(), tmp.hashCode());*/
    }
    
    public void testDefaultConstructor() {
/*    	BitVector tmp = new BitVector();
    	
    	assertEquals(0, tmp.getDimension());
    	
    	try {
    		boolean junk = tmp.getBit(1);
    		fail("ArrayIndexOutOfBoundsException should have been thown");
    		System.err.println(junk);
    	}
    	catch (ArrayIndexOutOfBoundsException ex) { }
    	
    	try {
    		tmp.setBit(1, true);
    		fail("ArrayIndexOutOfBoundsException should have been thown");
    	}
    	catch (ArrayIndexOutOfBoundsException ex) { }*/
    }
    
    public void testIntegerConstructor() {
    	/*Random r = new Random();
    	int dimension = r.nextInt(100000);
    	BitVector tmp = new BitVector(dimension);
    	
    	assertEquals(dimension, tmp.getDimension());*/
    }
    
    public void testGetDimension() {
    	/*BitVector tmp = new BitVector();
    	assertEquals(0, tmp.getDimension());
    	
    	tmp = new BitVector(1);
    	assertEquals(1, tmp.getDimension());
    	
    	tmp = new BitVector(10);
    	assertEquals(10, tmp.getDimension());*/
    }
    
    public void testSetBit() {
    	/*BitSet local = new BitSet();
    	BitVector tmp = new BitVector(100);
    	Random r = new Random();
    	
    	for (int i = 0; i < 100; ++i) {
    		boolean rtmp = r.nextBoolean();
    		local.set(i, rtmp);
    		tmp.setBit(i, rtmp);
    	}
    	
    	for (int i = 0; i < 100; ++i) {
    		assertEquals(local.get(i), tmp.getBit(i));
    	}*/
    }
    
    public void testGetInt() {
    	/*BitVector tmp = new BitVector(2);
    	tmp.setBit(0, true);
    	tmp.setBit(1, false);
    	
    	assertEquals(1, tmp.getInt(0));
    	assertEquals(0, tmp.getInt(1));*/
    }
    
    public void testSetInt() {
/*    	BitVector tmp = new BitVector(5);
    	
    	tmp.setInt(0, 1);
    	tmp.setInt(1, -1);
    	tmp.setInt(2, Integer.MAX_VALUE);
    	tmp.setInt(3, Integer.MIN_VALUE);
    	tmp.setInt(4, 0);
    	
    	assertTrue(tmp.getBit(0));
    	assertTrue(tmp.getBit(1));
    	assertTrue(tmp.getBit(2));
    	assertTrue(tmp.getBit(3));
    	assertFalse(tmp.getBit(4));*/
    }
    
    public void testGetReal() {
    	/*BitVector tmp = new BitVector(2);
    	tmp.setBit(0, true);
    	tmp.setBit(1, false);
    	
    	assertEquals(1.0, tmp.getReal(0), 0);
    	assertEquals(0.0, tmp.getReal(1), 0);*/
    }
    
    public void testSetReal() {
    	/*BitVector tmp = new BitVector(5); 
    	
       	tmp.setReal(0, Double.MAX_VALUE);
    	tmp.setReal(1, 1);
    	tmp.setReal(2, -1);
    	tmp.setReal(3, 1.01);
    	tmp.setReal(4, 0);
    	 
    	assertTrue(tmp.getBit(0));
    	assertTrue(tmp.getBit(1));
    	assertTrue(tmp.getBit(2));
    	assertTrue(tmp.getBit(3));
    	assertFalse(tmp.getBit(4));*/
    }
    
    /*
    public void testGetDomain() {
    	BitVector tmp = new BitVector();
    	assertEquals("[]", tmp.getDomain());
    	
    	tmp = new BitVector(1);
    	assertEquals(1, tmp.getDimension());
    	assertEquals("B^1", tmp.getDomain());
    	
    	tmp = new BitVector(50);
    	assertEquals("B^50", tmp.getDomain());
    }
    
    public void testGetBounds() {
    	BitVector tmp = new BitVector();
    	
    	assertTrue(tmp.getUpperBound(0).getBit());
    	assertFalse(tmp.getLowerBound(0).getBit());
    }
    
    public void testRandomise() {
    	Random r = new Random();
    	boolean foundOne = false;
    	boolean foundZero = false;
    	BitVector tmp = new BitVector(1000);
    	tmp.randomise(r);
  
    	for (int i = 0; i < 1000 && (! (foundOne && foundZero)); ++i) {
    		tmp.randomise(r);
    		if (tmp.getBit(i)) {
    			foundOne = true;
    		}
    		else {
    			foundZero = true;
    		}
    	}
    	
    	assertTrue(foundOne);
    	assertTrue(foundZero);
   }
    */
    
    public void testToString() {
    /*	BitVector tmp = new BitVector();
    	
    	assertEquals("[]", tmp.toString());
    	
    	tmp = new BitVector(1);
    	
    	assertEquals("[0]", tmp.toString());
    	
    	tmp = new BitVector(3);
    	tmp.setBit(1, true);
    	
    	assertEquals("[0,1,0]", tmp.toString());
    	*/
    }
    
    /*
    public void testSatisfiesDomain() {
    	assertTrue(new BitVector().satisfiesDomain());
    }
    */
    
    public void testGet() {
    	/*BitVector tmp = new BitVector(1);
    	tmp.setBit(0, true);
    	
    	Bit bit = (Bit) tmp.get(0);
    	
    	assertTrue(bit.getBit());*/
    }
    
    public void testSet() {
/*    	Int one = new Int(1);
    	Bit zero = new Bit(false);
    	
    	BitVector tmp = new BitVector(1);
    	
    	tmp.set(0, one);
    	
    	assertTrue(tmp.getBit(0));
    	
    	tmp.set(0, zero);
    	
    	assertFalse(tmp.getBit(0));*/
    }
    
    public void testSetVector() {
    	/*BitVector insert = new BitVector(3);
    	insert.setBit(1, true);
    	
    	BitVector tmp = new BitVector(1);
    	
    	tmp.set(0, insert);
    	
    	assertEquals(3, tmp.getDimension());
    	
    	assertFalse(tmp.getBit(0));
    	assertTrue(tmp.getBit(1));
    	assertFalse(tmp.getBit(2));
    	
    	tmp = new BitVector(3);
    	for (int i = 0; i < 3; ++i) {
    		tmp.setBit(i, true);
    	}
    	
    	tmp.set(1, insert);
    	
    	assertEquals(5, tmp.getDimension());
    	for (int i = 0; i < 5; ++i) {
    		if (i == 1 || i == 3) {
    			assertFalse(tmp.getBit(i));
    		}
    		else {
    			assertTrue(tmp.getBit(i));
    		}
    	}*/
    }
    
    public void testInsertIntoEmpty() {
    	/*Random r = new Random();
    	BitVector tmp = new BitVector();
    	
    	BitVector delta = new BitVector(5);
    	for (int i = 0; i < 5; ++i) {
    		delta.setBit(0, r.nextBoolean());
    	}
    	
    	tmp.insert(0, delta);
    	
    	assertEquals(5, tmp.getDimension());
    	assertEquals(delta, tmp);
    	*/
    }
    
    public void testInsertAtFront() {
    	/*BitVector tmp = new BitVector(3);
    	tmp.setBit(0, true);
    	tmp.setBit(2, true);
    	
    	BitVector delta = new BitVector(2);
    	delta.setBit(1, true);
    	
    	tmp.insert(0, delta);
    	
    	assertEquals(5, tmp.getDimension());
    	assertFalse(tmp.getBit(0));
    	assertTrue(tmp.getBit(1));
    	assertTrue(tmp.getBit(2));
    	assertFalse(tmp.getBit(3));
    	assertTrue(tmp.getBit(4));*/
    }
    
    public void testInsertMiddle() {
    	/*BitVector tmp = new BitVector(3);
    	tmp.setBit(0, true);
    	tmp.setBit(1, true);
    	tmp.setBit(2, true);
    	
    	BitVector delta = new BitVector(1);
    	
    	tmp.insert(2, delta);
    	
    	assertEquals(4, tmp.getDimension());
    	assertTrue(tmp.getBit(0));
    	assertTrue(tmp.getBit(1));
    	assertFalse(tmp.getBit(2));
    	assertTrue(tmp.getBit(3));
    	*/
    }
    
    public void testInsertAtBack() {
/*    	BitVector tmp = new BitVector(3);
    	
    	BitVector delta = new BitVector(4);
    	for (int i = 0; i < 4; ++i) {
    		delta.setBit(i, true);
    	}
    	
    	tmp.insert(3, delta);
    	
    	assertEquals(7, tmp.getDimension());
    	for (int i = 0; i < 3; ++i) {
    		assertFalse(tmp.getBit(i));
    	}
    	for (int i = 3; i < 7; ++i) {
    		assertTrue(tmp.getBit(i));
    	}*/
    }
 }
