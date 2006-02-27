/*
 * FunctionTest.java
 * JUnit based test
 *
 * Created on January 21, 2003, 2:21 PM
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

package net.sourceforge.cilib.Functions;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sourceforge.cilib.Problem.Domain;

/**
 *
 * @author espeer
 */
public class FunctionTest extends TestCase {
    
    private Domain domain;
    private Function function;
    
    public FunctionTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(FunctionTest.class);
        
        return suite;
    }
    
    protected void setUp() {
        domain = new Domain(-1, 1);
        function = new FunctionImpl(1, domain, 1, 1);
    }
    
    /** Test of getDimension method, of class za.ac.up.cs.ailib.Functions.Function. */
    public void testGetDimension() {
        assertEquals(1, function.getDimension());
    }
    
    /** Test of setDimension method, of class za.ac.up.cs.ailib.Functions.Function. */
    public void testSetDimension() {
        function.setDimension(2);
        assertEquals(2, function.getDimension());
    }
    
    /** Test of getDomain method, of class za.ac.up.cs.ailib.Functions.Function. */
    public void testGetDomain() {
        assertSame(domain, function.getDomain());
    }
    
    /** Test of setDomain method, of class za.ac.up.cs.ailib.Functions.Function. */
    public void testSetDomain() {
        Domain d = new Domain(0, 0);
        function.setDomain(d);
        assertSame(d, function.getDomain());
    }
    
    /** Test of getMinimum method, of class za.ac.up.cs.ailib.Functions.Function. */
    public void testGetMinimum() {
        assertEquals(1.0, function.getMinimum(), 0.0);
    }
    
    public void testGetMaximum() {
    	assertEquals(1.0, function.getMaximum(), 0.0);
    }
    
    /** Generated implementation of abstract class za.ac.up.cs.ailib.Functions.Function. Please fill dummy bodies of generated methods. */
    private class FunctionImpl extends Function {
        
        public FunctionImpl(int dimension, Domain domain, double minimum, double maximum) {
            super(dimension, domain, minimum, maximum);
        }
        
        public double evaluate(double[] x) {
            return 0;
        }
        
    }    
    
}
