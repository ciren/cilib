/*
 * DomainInitialisationTest.java
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

package net.sourceforge.cilib.Domain;

import java.util.BitSet;
import java.util.Random;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author espeer
 */
public class DomainInitialisationTest extends TestCase {
    
    public DomainInitialisationTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(DomainInitialisationTest.class);
        
        return suite;
    }
    
    public void setUp() {
    }
    
    public void testSingleBit() {
    	DomainComponent domain = factory.newComponent("B");
    	assertEquals(1, domain.getDimension());
    	
    	Object tmp = domain.getRandom(randomiser);
    	
    	assertEquals(Byte.class, tmp.getClass());
    	
    	byte data = ((Byte) tmp).byteValue();
    	
    	assertTrue(data == 0 || data == 1);
    }
    
    public void testSingleInt() {
    	DomainComponent domain = factory.newComponent("Z(-5,3)");
    	assertEquals(1, domain.getDimension());
    	
    	Object tmp = domain.getRandom(randomiser);
    	
    	assertEquals(Integer.class, tmp.getClass());
    	
    	int data = ((Integer) tmp).intValue();
    	
    	assertTrue(data >= -5 && data <= 3);
    }
    
    public void testSingleReal() {
       	DomainComponent domain = factory.newComponent("R");
    	assertEquals(1, domain.getDimension());
    	
    	Object tmp = domain.getRandom(randomiser);
    	
    	assertEquals(Double.class, tmp.getClass());
    	
    	double data = ((Double) tmp).doubleValue();
    	
		assertFalse(Double.isNaN(data));
		assertFalse(Double.isInfinite(data));
    }
    
    public void testSingleString() {
    	DomainComponent domain = factory.newComponent("T");
    	assertEquals(1, domain.getDimension());
    	
    	Object tmp = domain.getRandom(randomiser);
    	
    	assertEquals(String.class, tmp.getClass());
    }
    
    public void testSimpleRealCompoundDomain() {
    	DomainComponent domain = factory.newComponent("R^1000");
    	assertEquals(1000, domain.getDimension());
    	
    	double[] data = (double[]) domain.getRandom(randomiser);
    	
    	assertEquals(1000, data.length);
    	
    	for (int i = 0; i < data.length; ++i) {
    		assertFalse(Double.isNaN(data[i]));
    		assertFalse(Double.isInfinite(data[i]));
    	}
    }
    
    public void testSimpleIntCompoundDomain() {
    	DomainComponent domain = factory.newComponent("Z(2,8)^500");
    	assertEquals(500, domain.getDimension());
    	
    	int[] data = (int[]) domain.getRandom(randomiser);
    	
    	assertEquals(500, data.length);
    	
    	for (int i = 0; i < data.length; ++i) {
    		assertTrue(data[i] >= 2 && data[i] <= 8);
    	}
    }
       
    public void testSimpleRealCompositeDomain() {
    	DomainComponent domain = factory.newComponent("[R,R,R,R,R]");
    	assertEquals(5, domain.getDimension());
    	
    	double[] data = (double[]) domain.getRandom(randomiser);
    	
    	assertEquals(5, data.length);
    	
    	for (int i = 0; i < data.length; ++i) {
    		assertFalse(Double.isNaN(data[i]));
    		assertFalse(Double.isInfinite(data[i]));
    	}
    }
    
    public void testSimpleBitCompoundDomain() {
    	DomainComponent domain = factory.newComponent("B^512");
    	assertEquals(512, domain.getDimension());
    	
    	BitSet data = (BitSet) domain.getRandom(randomiser);
    	
    	assertEquals(512, data.size());
    }
    
    public void testMixedRealBitVector() {
    	DomainComponent domain = factory.newComponent("[R(-1, 1)^30, B^20]");
    	assertEquals(50, domain.getDimension());
    	
    	Object[] parent = (Object[]) domain.getRandom(randomiser);
    	
    	assertEquals(2, parent.length);
    	
    	double[] reals = (double[]) parent[0];
    	BitSet bits = (BitSet) parent[1];
    	
    	assertEquals(30, reals.length);
    	assertEquals(20, bits.size());

    	for (int i = 0; i < 30; ++i) {
    		assertTrue(reals[i] <= 1 && reals[i] >= -1);
    	}
    		
    }
    
    public void testFlattenComposite() {
    	DomainComponent domain = factory.newComponent("[R(0,1),R,R^3,[[R,R], [R^10],R,R]^2,R]");
    	assertEquals(34, domain.getDimension());
    	
    	double[] data = (double[]) domain.getRandom(randomiser);
    	
    	assertEquals(34, data.length);
    	
    	assertTrue(data[0] >= 0 && data[0] <= 1);
    	
    	for (int i = 0; i < data.length; ++i) {
    		assertFalse(Double.isNaN(data[i]));
    		assertFalse(Double.isInfinite(data[i]));
    	}
    }
    
    public void testAlternatingComposite() {
    	DomainComponent domain = factory.newComponent("[R,B]^5");
    	assertEquals(10, domain.getDimension());
    	
    	Object[] data = (Object[]) domain.getRandom(randomiser);
    	assertEquals(10, data.length);
    	
    	for (int i = 0; i < 10; i +=2) {
    		double[] child = (double[]) data[i];
    		assertEquals(1, child.length);
    	}
    	
    	for (int i = 1; i < 10; i +=2) {
    		BitSet child = (BitSet) data[i];
    		assertEquals(1, child.size());
    	}
    }
    
    public void testRealMatrix() {
    	DomainComponent domain = factory.newComponent("R^3^3");
    	assertEquals(9, domain.getDimension());
    	
    	double[] data = (double[]) domain.getRandom(randomiser);
    	assertEquals(9, data.length);
    	
    	for (int i = 0; i < data.length; ++i) {
    		assertFalse(Double.isNaN(data[i]));
    		assertFalse(Double.isInfinite(data[i]));
    	}
    	    	
    }
    
    public void testTextVector() {
    	DomainComponent domain = factory.newComponent("T^5");
    	assertEquals(5, domain.getDimension());
    	
    	String[] data = (String[]) domain.getRandom(randomiser);
    	assertEquals(5, data.length);
    }
    
    public void testPathologicalCase() {
    	DomainComponent domain = factory.newComponent("[T^3,[R,B^10]^4^3, [R, [R], [[R]], [R,R, [R, R,[R, [[R]]]]^2], B], S{a,b,c},R,R,B^13,R(-10,10)^3 ]^10");
    	assertEquals(1680, domain.getDimension()); // (3 + (11 * 4 * 3) + 5 + 8 + 1 + 1 + 2 + 13 + 3) * 10 = 1680
    	
    	Object[] root = (Object[]) domain.getRandom(randomiser); 
    	assertEquals(310, root.length); // (1 + 24 + 1 <12> + 1 + 1 + 1 <2> + 1 <13> + 1 <3>) * 10 = 310   

    	for (int offset = 0; offset < 310; offset += 31) {
    	
    		String[] texts = (String[]) root[offset + 0];
    		assertEquals(3, texts.length);
    	
    		for (int i = 1; i < 25; i += 2) {
    			double[] reals = (double[]) root[offset + i];
    			assertEquals(1, reals.length);
    		}
    	
    		for (int i = 2; i < 25; i += 2) {
    			BitSet bits = (BitSet) root[offset + i];
    			assertEquals(10, bits.size());
    		}
    	
    		double[] reals = (double[]) root[offset + 25];
    		assertEquals(13, reals.length);
    	
    		BitSet bits = (BitSet) root[offset + 26];
    		assertEquals(1, bits.size());
    	
    		java.util.Set[] sets = (java.util.Set[]) root[offset + 27];
    		assertEquals(1, sets.length);
    	
    		reals = (double[]) root[offset + 28];
    		assertEquals(2, reals.length);
    	
    		bits = (BitSet) root[offset + 29];
    		assertEquals(13, bits.size());
    	
    		reals = (double[]) root[offset + 30];
    		assertEquals(3, reals.length);
    		for (int i = 0; i < 3; ++i) {
    			assertTrue(reals[0] >= -10 && reals[0] <= 10);
    		}
    		
    	}
    }
    
    private ComponentFactory factory = ComponentFactory.instance();
    private Random randomiser = new Random();
}
