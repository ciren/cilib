/*
 * DomainTest.java
 * JUnit based test
 *
 * Created on January 21, 2003, 8:04 PM
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

package net.sourceforge.cilib.Problem;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author espeer
 */
public class DomainTest extends TestCase {
    
    private Domain infinite;
    private Domain finite;
    
    public DomainTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(DomainTest.class);
        
        return suite;
    }
    
    public void setUp() {
        infinite = new Domain();
        finite = new Domain(-1, 1);
    }
    
    /** Test of getLowerBound method, of class za.ac.up.cs.ailib.Domain. */
    public void testGetLowerBound() {
        assertEquals(- Double.MAX_VALUE, infinite.getLowerBound(), 0.0);
        assertEquals(-1, finite.getLowerBound(), 0.0);
    }
    
    /** Test of getUpperBound method, of class za.ac.up.cs.ailib.Domain. */
    public void testGetUpperBound() {
        assertEquals(Double.MAX_VALUE, infinite.getUpperBound(), 0.0);
        assertEquals(1, finite.getUpperBound(), 0.0);
    }
    
    /** Test of setLowerBound method, of class za.ac.up.cs.ailib.Domain. */
    public void testSetLowerBound() {
        finite.setLowerBound(0);
        assertEquals(0, finite.getLowerBound(), 0.0);
        assertEquals(1, finite.getUpperBound(), 0.0);
    }
    
    /** Test of setUpperBound method, of class za.ac.up.cs.ailib.Domain. */
    public void testSetUpperBound() {
        finite.setUpperBound(0);
        assertEquals(0, finite.getUpperBound(), 0.0);
        assertEquals(-1, finite.getLowerBound(), 0.0);
    }
    
    
    
}
