/*
 * InstanceTest.java
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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author espeer
 */
public class InstanceTest extends TestCase {
    
    public InstanceTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(InstanceTest.class);
        
        return suite;
    }
    
    public void setUp() {
    }
    
    public void testSingleBit() {
    	DomainComponent domain = factory.newComponent("B");
    	
    	assertFalse(domain.isInside(null));
    	assertTrue(domain.isInside(new Byte((byte) 0)));
    	assertTrue(domain.isInside(new Byte((byte) 1)));
    	
    	for (int i = 2; i < 256; ++i) {
    		assertFalse(domain.isInside(new Byte((byte) i)));
    	}
    	
    	assertFalse(domain.isInside(new Object()));
    	assertFalse(domain.isInside(new BitSet()));
    	assertFalse(domain.isInside(new Double(0)));
    	assertFalse(domain.isInside(new double[1]));
    }

    public void testSingleInt() {
    	DomainComponent domain = factory.newComponent("Z(-1, 1)");
    	
    	assertTrue(domain.isInside(new Integer(-1)));
    	assertTrue(domain.isInside(new Integer(1)));
    	assertTrue(domain.isInside(new Integer(0)));
    	assertFalse(domain.isInside(new Integer(-2)));
    	assertFalse(domain.isInside(new Integer(2)));
    	
    	assertFalse(domain.isInside(null));
    	assertFalse(domain.isInside(new Byte((byte) 0)));
    	assertFalse(domain.isInside(new Object()));
    	assertFalse(domain.isInside(new BitSet()));
    	assertFalse(domain.isInside(new Double(0)));
    	assertFalse(domain.isInside(new int[1]));
    	assertFalse(domain.isInside(new double[1]));
    }
    
    public void testSingleReal() {
    	DomainComponent domain = factory.newComponent("R(-1, 1)");
    	
    	assertTrue(domain.isInside(new Double(-1)));
    	assertTrue(domain.isInside(new Double(1)));
    	assertTrue(domain.isInside(new Double(0)));
    	assertFalse(domain.isInside(new Double(-2.00000001)));
    	assertFalse(domain.isInside(new Double(2.00000001)));
    	
    	assertFalse(domain.isInside(null));
    	assertFalse(domain.isInside(new Integer(0)));
    	assertFalse(domain.isInside(new Byte((byte) 0)));
    	assertFalse(domain.isInside(new Object()));
    	assertFalse(domain.isInside(new BitSet()));
    	assertFalse(domain.isInside(new int[1]));
    	assertFalse(domain.isInside(new double[1]));
    }
    
    public void testRealVector() {
    	DomainComponent domain = factory.newComponent("[R^3, R(0.1,1)]");
    	
    	assertTrue(domain.isInside(new double[] { 0,0,0,0.1 }));
    	assertTrue(domain.isInside(new double[] { Double.MIN_VALUE, Double.MAX_VALUE, Double.POSITIVE_INFINITY, 1}));
    	
    	assertFalse(domain.isInside(new double[] { Double.MIN_VALUE, Double.MAX_VALUE, Double.POSITIVE_INFINITY, 0.01}));
    	assertFalse(domain.isInside(new double[] { Double.MIN_VALUE, Double.MAX_VALUE, Double.POSITIVE_INFINITY, 1.01}));

    	assertFalse(domain.isInside(new double[] { 0,0,0,0.1,0 }));
    	assertFalse(domain.isInside(null));
    	assertFalse(domain.isInside(new Integer(0)));
    	assertFalse(domain.isInside(new Byte((byte) 0)));
    	assertFalse(domain.isInside(new Object()));
    	assertFalse(domain.isInside(new BitSet()));
    	assertFalse(domain.isInside(new int[1]));
    	assertFalse(domain.isInside(new double[1]));
    	assertFalse(domain.isInside(new double[4]));
    	assertFalse(domain.isInside(new Double(0)));
    	
    }
    
    public void testMixedVector() {
    	
    	DomainComponent domain = factory.newComponent("[R^3, B^15, Z^2]");
    	
    	BitSet bits = new BitSet();
    	bits.set(15, true);
    	
    	assertTrue(domain.isInside(new Object[] { new double[] { 0, 1, 2 }, bits, new int[] { -1, 1 } }));

    	assertFalse(domain.isInside(new Object[] { bits, new double[] { 0, 1, 2 }, new int[] { -1, 1 } }));
    	assertFalse(domain.isInside(new Object[] { new int[] { -1, 1 }, bits, new double[] { 0, 1, 2 } }));
    	

    	assertFalse(domain.isInside(new Object[] { new double[] { 0, 1, 2, 3 }, bits, new int[] { -1, 1 } }));
    	
    	assertFalse(domain.isInside(new Object[] { new double[] { 0, 1, 2 }, bits }));
    	
    	assertFalse(domain.isInside(new Object[] { new double[] { 0, 1, 2 }, bits, new int[] { -1, 1, 0 } }));

    	
    	
    	bits.set(16, true);
    	assertFalse(domain.isInside(new Object[] { new double[] { 0, 1, 2 }, bits, new int[] { -1, 1 } }));
    	
    	assertFalse(domain.isInside(new Object[3]));
		assertFalse(domain.isInside(new Object[20]));
    	assertFalse(domain.isInside(new double[] { 0,0,0,0.1,0 }));
    	assertFalse(domain.isInside(null));
    	assertFalse(domain.isInside(new Integer(0)));
    	assertFalse(domain.isInside(new Byte((byte) 0)));
    	assertFalse(domain.isInside(new Object()));
    	assertFalse(domain.isInside(new BitSet()));
    	assertFalse(domain.isInside(new int[1]));
    	assertFalse(domain.isInside(new double[1]));
    	assertFalse(domain.isInside(new double[4]));
    	assertFalse(domain.isInside(new Double(0)));

    }
    
    private ComponentFactory factory = ComponentFactory.instance();
}
