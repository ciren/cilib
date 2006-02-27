/*
 * BitTest.java
 * JUnit based test
 *
 * Created on January 21, 2003, 4:45 PM
 *
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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author espeer
 */
public class BitTest extends TestCase {
    
    public BitTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(BitTest.class);
        
        return suite;
    }
    
    public void setUp() {
    	one = new Bit(true);
    	zero = new Bit(false);
    }
       
    public void testClone() {
    	try {
    		Bit oneClone = (Bit) one.clone();
    		assertNotSame(one, oneClone);
    		assertEquals(one, oneClone);
    		
    		oneClone.setBit(false);
    		
    		Bit zeroClone = (Bit) zero.clone();
    		assertNotSame(zero, zeroClone);
    		assertEquals(zero, zeroClone);
    		
    		zeroClone.setBit(true);
    		
    		checkForSideEffects();
    	}
    	catch (CloneNotSupportedException ex) {
    		fail("Failed to clone Bit");
    	}
    }
    
    public void testEquals() {
    	Bit tmp = new Bit(true);
    	
    	assertTrue(tmp.equals(tmp));
    	
    	assertTrue(one.equals(tmp));
    	assertTrue(tmp.equals(one));
    	
    	assertFalse(one.equals(zero));
    	assertFalse(zero.equals(one));
    	
    	assertFalse(one.equals(null));
    	
    	checkForSideEffects();
    }
    
    public void testHashCode() {
    	assertEquals(0, zero.hashCode());
    	assertEquals(1, one.hashCode());
    	
    	checkForSideEffects();
    }
    
    public void testDefaultConstructor() {
    	Bit tmp = new Bit();
    	assertFalse(tmp.getBit());
    }
    
    public void testBooleanConstructorAndGetBit() {
    	Bit tmp = new Bit(true);
    	assertTrue(tmp.getBit());
    	tmp = new Bit(false);
    	assertFalse(tmp.getBit());
    }
    
    public void testSetBit() {
    	Bit tmp = new Bit();
    	tmp.setBit(true);
    	assertTrue(tmp.getBit());
    	tmp.setBit(false);
    	assertFalse(tmp.getBit());
    }
    
    public void testGetInt() {
    	assertEquals(1, one.getInt());
    	assertEquals(0, zero.getInt());
    	
    	checkForSideEffects();
    }
    
    public void testSetInt() {
    	Bit tmp = new Bit();
    	tmp.setInt(1);
    	assertTrue(tmp.getBit());
    	tmp.setInt(-1);
    	assertTrue(tmp.getBit());
    	tmp.setInt(Integer.MAX_VALUE);
    	assertTrue(tmp.getBit());
    	tmp.setInt(Integer.MIN_VALUE);
    	assertTrue(tmp.getBit());
    	tmp.setInt(0);
    	assertFalse(tmp.getBit());
    }
    
    public void testGetReal() {
    	assertEquals(1.0, one.getReal(), 0);
    	assertEquals(0.0, zero.getReal(), 0);
    	
    	checkForSideEffects();
    }
    
    public void testSetReal() {
    	Bit tmp = new Bit();
    	tmp.setReal(Double.MAX_VALUE);
    	assertTrue(tmp.getBit());
    	tmp.setReal(1);
    	assertTrue(tmp.getBit());
    	tmp.setReal(-1);
    	assertTrue(tmp.getBit());
    	tmp.setReal(1.01);
    	assertTrue(tmp.getBit());
    	tmp.setReal(0);
    	assertFalse(tmp.getBit());
    }
    
    /*
    public void testGetDomain() {
    	assertEquals("B", zero.getDomain());
    	
    	checkForSideEffects();
    }
    
    public void testGetBounds() {
    	Bit tmp = new Bit();
    	assertEquals(one, tmp.getUpperBound());
    	assertEquals(zero, tmp.getLowerBound());
    	
    	checkForSideEffects();
    }
    
    public void testRandomise() {
    	Random r = new Random();
    	boolean foundOne = false;
    	boolean foundZero = false;
    	Bit tmp = new Bit();
    	for (int i = 0; i < 1000 && (! (foundOne && foundZero)); ++i) {
    		tmp.randomise(r);
    		if (tmp.getBit()) {
    			foundOne = true;
    		}
    		else {
    			foundZero = true;
    		}
    	}
    	
    	assertTrue(foundOne);
    	assertTrue(foundZero);
    	
    	for (int i = 0; i < 100; ++i) {
    		tmp.randomise(r, new Bounds(tmp, null, new Bit(false)));
    		assertFalse(tmp.getBit());
    	}
    	
    	for (int i = 0; i < 100; ++i) {
    		tmp.randomise(r, new Bounds(tmp, new Bit(true), null));
    		assertTrue(tmp.getBit());
    	}
    	
    }
    */
    public void testToString() {
    	assertEquals("0", zero.toString());
    	assertEquals("1", one.toString());
    	
    	checkForSideEffects();
    }
    
    /*
    public void testSatisfiesDomain() {
    	assertTrue(one.satisfiesDomain());
    	assertTrue(zero.satisfiesDomain());
    	
    	checkForSideEffects();
    }
    */

    public void testCompareTo() {
    	Bit tmp = new Bit(true);
    	
    	assertEquals(0, tmp.compareTo(one));
    	assertEquals(0, one.compareTo(tmp));
    	
    	assertTrue(zero.compareTo(one) < 0);
    	assertTrue(one.compareTo(zero) > 0);
    	
    	checkForSideEffects();
    }
    
    private void checkForSideEffects() {
    	assertTrue(one.getBit());
    	assertFalse(zero.getBit());
    }
    
    
    private Bit one;
    private Bit zero;
    
 }
