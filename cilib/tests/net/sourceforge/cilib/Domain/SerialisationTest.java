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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.BitSet;
import java.util.Random;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author espeer
 */
public class SerialisationTest extends TestCase {
    
    public SerialisationTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(SerialisationTest.class);
        
        return suite;
    }
    
    public void setUp() {
    }
    
    public void testIntegerMeasurement() {
    	DomainComponent domain = factory.newComponent("M(net.sourceforge.cilib.Measurement.Time)");
    	
    	net.sourceforge.cilib.Measurement.Time time = new net.sourceforge.cilib.Measurement.Time();
    	time.algorithmFinished(null);
    	int tmp = ((Integer) time.getValue()).intValue();

    	try {
    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		ObjectOutputStream oos = new ObjectOutputStream(bos);
    	
    		domain.serialise(oos, time.getValue());
    		oos.close();
    		
    		byte[] data = bos.toByteArray();
    		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
    		
    		Object result = domain.deserialise(ois);
    		assertTrue(result instanceof Integer);
    		assertEquals(tmp, ((Integer) result).intValue());
    	}
    	catch (IOException ex) {
    		fail("IOException should not have been thrown, message = " + ex.getMessage());
    	}
    }

    public void testRealVector() {
    	DomainComponent domain = factory.newComponent("R^3");
    	
    	double[] tmp = new double[] { 1,2,3 };

    	try {
    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		ObjectOutputStream oos = new ObjectOutputStream(bos);
    	
    		domain.serialise(oos, tmp);
    		oos.close();
    		
    		byte[] data = bos.toByteArray();
    		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
    		
    		Object r = domain.deserialise(ois);
    		assertTrue(r instanceof double[]);
    		double[] result = (double[]) r;
    		assertEquals(3, result.length);
    		for (int i = 0; i < 3; ++i) {
    			assertTrue(tmp[i] == result[i]);
    		}
    	}
    	catch (IOException ex) {
    		fail("IOException should not have been thrown, message = " + ex.getMessage());
    	}
    }

    public void testMixedRealBitVector() {
    	DomainComponent domain = factory.newComponent("[R^100, B^512]");
    	Random random = new Random();
 
    	double[] first = new double[100];
    	BitSet second = new BitSet();
    	
    	for (int i = 0; i < 100; ++i) {
    		first[i] = random.nextDouble();
    	}
    	for (int i = 0; i < 512; ++i) {
    		second.set(i, random.nextBoolean());
    	}
      	Object[] tmp = new Object[] { first, second };
  	
    	try {
    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		ObjectOutputStream oos = new ObjectOutputStream(bos);
    	
    		domain.serialise(oos, tmp);
    		oos.close();
    		
    		byte[] data = bos.toByteArray();
    		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
    		
    		Object r = domain.deserialise(ois);
    		assertTrue(r instanceof Object[]);
    		assertEquals(2, ((Object[]) r).length);
    		
    		double[] firstResult = (double[] )((Object[]) r)[0];
    		BitSet secondResult = (BitSet) ((Object[]) r)[1];
    		
    		assertEquals(100, firstResult.length);
    		for (int i = 0; i < 100; ++i) {
    			assertTrue(first[i] == firstResult[i]);
    		}
    		for (int i = 0; i < 512; ++i) {
    			assertTrue(second.get(i) == secondResult.get(i));
    		}
    	}
    	catch (IOException ex) {
    		fail("IOException should not have been thrown, message = " + ex.getMessage());
    	}
    }

    
    private ComponentFactory factory = ComponentFactory.instance();
}
